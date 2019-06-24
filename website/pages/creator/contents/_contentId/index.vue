<template>
  <div class="container-768 mtb-48 relative">
    <content-editor-bubble :editor="editor"/>
    <content-editor-floating :editor="editor" :content-id="contentId" @save="saveContent"/>

    <editor-content class="Editor" :editor="editor" :class="{Started: started, Active: active}"/>

    <!--<code class="lh-1">-->
    <!--<pre>{{draft}}</pre>-->
    <!--</code>-->

    <content-nav-header @delete="deleteContent" @save="saveContent" @publish="publishContent" :content-id="contentId"/>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"
  import {Editor, EditorContent} from 'tiptap'
  import ContentEditorBubble from "../../../../components/creator/content/ContentEditorBubble";
  import ContentEditorFloating from "../../../../components/creator/content/ContentEditorFloating";
  import {HardBreak, Heading, History, Italic, Bold, Underline, Link} from 'tiptap-extensions'

  import Line from '../../../../components/creator/content/ContentEditorLine'
  import Image from '../../../../components/creator/content/ContentEditorImage'
  import Place from '../../../../components/creator/content/ContentEditorPlace'
  import ContentNavHeader from "../../../../components/creator/content/ContentNavHeader";

  export default {
    layout: 'creator',
    components: {ContentNavHeader, ContentEditorFloating, ContentEditorBubble, EditorContent},
    head() {
      const title = this.content && this.content.title || 'Content'
      return {title: `Editing ${title} · ${this.creatorName}`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName']),
      creatorId() {
        return this.content && this.content.creatorId || this.$store.getters['creator/creatorId']
      },
      contentId() {
        return this.content && this.content.contentId
      }
    },
    data() {
      return {editor: null}
    },
    asyncData({$api, params: {contentId}, $error}) {
      if (contentId === 'new') {
        return {
          started: false,
          active: false,
          error: false,
          savedTime: 0,
          changedTime: 0,
          content: {},
          draft: {
            type: "doc",
            content: [
              {type: "heading", attrs: {level: 1}},
              {type: "paragraph",},
            ],
          }
        }
      }

      return $api.get(`/creators/_/contents/${contentId}/drafts`)
        .then(({data: {content, draft}}) => {
          return ({content, draft, started: true, active: false});
        })
        .catch((err) => $error(err))
    },
    mounted() {
      this.$$autoUpdate = setInterval(this.saveContent, 10000)
      this.$$autoWatcher = setInterval(this.onAutoWatcher, 1000)
      window.onbeforeunload = this.onBeforeUnload

      this.editor = new Editor({
        extensions: [
          new History(),
          new Heading({levels: [1, 2]}),
          new HardBreak(),
          new Bold(),
          new Italic(),
          new Link(),
          new Underline(),
          new Line(),
          new Image(),
          new Place(),
        ],
        content: this.draft,
        onUpdate: ({getJSON}) => {
          this.draft = getJSON()
          this.started = true
          this.active = true
          this.changedTime = new Date().getTime()

          // Check if last line is empty.
          const last = _.last(this.draft.content)
          if (!(last.type === 'paragraph' && !last.content)) {
            const paragraph = this.editor.schema.node('paragraph')
            const tr = this.editor.state.tr
            this.editor.view.dispatch(tr.insert(tr.doc.content.size, paragraph))
          }
        },
      })
    },
    beforeDestroy() {
      clearInterval(this.$$autoUpdate)
      clearInterval(this.$$autoWatcher)
      window.onbeforeunload = undefined

      this.saveContent()
      this.editor.destroy()
    },
    methods: {
      onBeforeUnload() {
        if (!this.started) return
        if (this.changedTime === this.savedTime) return

        return 'You have unsaved changes!'
      },
      onAutoWatcher() {
        if (this.changedTime) {
          const diff = new Date().getTime() - this.changedTime
          if (diff > 4000)  {
            this.active = false
          }
        }
      },
      saveContent() {
        if (this.error) {
          return this.$store.dispatch('addMessage', {
            title: 'Error Saving',
            message: 'Unable to save the article, please refresh the page. (Note that any of your current changes will be lost.)'
          })
        }
        if (!this.started) return

        if (this.changedTime === this.savedTime) return Promise.resolve()

        const draft = {
          type: "doc",
          content: this.draft.content,
          version: '2019-02-11'
        }

        console.log("Saving")

        const time = this.changedTime
        if (this.contentId) {
          return this.$api.put(`/creators/${this.creatorId}/contents/${this.contentId}/drafts`, draft)
            .then(({data: {content,}}) => {
              this.content = content
              this.savedTime = time
            })
            .catch((err) => {
              this.error = true
              this.$store.dispatch('addError', err)
            })
        } else {
          return this.$api.post(`/creators/${this.creatorId}/contents/_/drafts`, draft)
            .then(({data: {content,}}) => {
              this.content = content
              this.savedTime = time

              const url = `/creator/contents/${content.contentId}`
              window.history.replaceState({}, document.title, url);
            })
            .catch((err) => {
              this.error = true
              this.$store.dispatch('addError', err)
            })
        }
      },
      deleteContent() {
        if (!this.contentId) return

        return this.$api.delete(`/creators/${this.creatorId}/contents/${this.contentId}`).then(() => {
          this.$router.push({path: '/creator/contents'})
        }).catch((err) => this.$store.dispatch('addError', err))
      },
      publishContent() {
        if (!this.started) return
        if (!this.contentId) return

        this.saveContent().then(() => {
          this.$router.push({path: `/creator/contents/${this.contentId}/publish`})
        })
      }
    }
  }
</script>

<style lang="less">
  .ProseMirror {
    &:focus {
      outline: none;
    }
  }

  .Editor {
    p {
      font-size: 19px;
      line-height: 1.8;
      color: rgba(0, 0, 0, 0.8);

      margin-top: 16px;
      margin-bottom: 32px;
    }

    h1 {
      margin-top: 64px;
      margin-bottom: 16px;

      &:first-child {
        margin-top: 0;
      }
    }

    h2 {
      margin-bottom: 16px;
    }

    a {
      color: #F05F3B;
      text-decoration: underline;
      cursor: pointer;
    }


    &:not(.Started) {
      h1:first-child::before {
        content: "Title";
        float: left;
        color: #aaa;
        pointer-events: none;
        height: 0;
      }

      p::before {
        content: "Write something …";
        float: left;
        color: #aaa;
        pointer-events: none;
        height: 0;
        font-style: italic;
      }
    }

    &.Started:not(.Active) {
      > div > p:last-child::before {
        content: "Continue writing …";
        float: left;
        color: #aaa;
        pointer-events: none;
        height: 0;
        font-style: italic;
      }
    }
  }
</style>
