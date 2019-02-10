<template>
  <div class="container-768 mtb-48 relative">
    <content-editor-bubble :editor="editor"/>
    <content-editor-floating :editor="editor" :content-id="contentId" @save="saveContent"/>

    <editor-content class="Editor" :editor="editor" :class="{Started: started}"/>

    <!--<code class="lh-1">-->
      <!--<pre>{{draft}}</pre>-->
    <!--</code>-->

    <portal to="creator-header-right">
      <div class="flex-align-center mr-8 relative">
        <button @click="publishContent" class="secondary-outline tiny mr-8">Ready to publish?</button>
        <button @click="header.more = !header.more" class="small">
          <simple-svg class="wh-20px" fill="black" :filepath="require('~/assets/icon/more.svg')"/>
        </button>

        <div class="CreatorHeader absolute no-select" v-if="header.more"
             @click="header.more = false" v-on-clickaway="() => {if(header.more) header.more = false}">
          <div class="border-3 bg-white w-100 elevation-2 text index-top-elevation border lh-1">
            <div @click="saveContent">Save content</div>
            <div @click="header.deleting = true">Delete content</div>
            <div>Add to series</div>
          </div>
        </div>
      </div>

      <div>
        <portal to="dialog-styled" v-if="header.deleting">
          <h3>Are you sure?</h3>
          <p>Once the content is deleted, you cannot recover it.</p>
          <div class="right">
            <button class="border" @click="header.deleting = false">Cancel</button>
            <button class="secondary" @click="deleteContent">Confirm</button>
          </div>
        </portal>
      </div>
    </portal>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"
  import {Editor, EditorContent} from 'tiptap'
  import ContentEditorBubble from "../../../../components/creator/content/ContentEditorBubble";
  import ContentEditorFloating from "../../../../components/creator/content/ContentEditorFloating";
  import {HardBreak, Heading, History} from 'tiptap-extensions'

  import Line from '../../../../components/creator/content/ContentEditorLine'
  import Image from '../../../../components/creator/content/ContentEditorImage'
  import Place from '../../../../components/creator/content/ContentEditorPlace'

  export default {
    layout: 'creator',
    components: {ContentEditorFloating, ContentEditorBubble, EditorContent},
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
      return {
        editor: null,
        header: {
          more: false,
          deleting: false,
        },
      }
    },
    asyncData({$api, params: {contentId}, $error}) {
      if (contentId === 'new') {
        return {
          started: false,
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
        .then(({data: {content, draft}}) => ({content, draft, started: true}))
        .catch((err) => $error(err))
    },
    mounted() {
      this.$$autoUpdate = setInterval(this.saveContent, 10000)
      window.onbeforeunload = this.onBeforeUnload

      this.editor = new Editor({
        extensions: [
          new History(),
          new Heading({levels: [1, 2]}),
          new HardBreak(),
          // new Bold(),
          // new Italic(),
          new Line(),
          new Image(),
          new Place(),
        ],
        content: this.draft,
        onUpdate: ({getJSON}) => {
          this.draft = getJSON()
          this.started = true
          this.changedTime = new Date().getTime()
        },
      })
    },
    beforeDestroy() {
      clearInterval(this.$$autoUpdate)
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
      saveContent() {
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
            .catch((err) => this.$store.dispatch('addError', err))
        } else {
          return this.$api.post(`/creators/${this.creatorId}/contents/_/drafts`, draft)
            .then(({data: {content,}}) => {
              this.content = content
              this.savedTime = time

              const url = `/creator/contents/${content.contentId}`
              window.history.replaceState({}, document.title, url);
            })
            .catch((err) => this.$store.dispatch('addError', err))
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
          this.$router.push({path: `/creator/contents/${this.contentId}/publishing`})
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .CreatorHeader {
    top: 40px;
    right: 0;

    > div {
      padding: 8px 0;

      > div {
        padding: 8px 24px;
        font-size: 14px;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
</style>

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
      margin-bottom: 24px;
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
  }
</style>
