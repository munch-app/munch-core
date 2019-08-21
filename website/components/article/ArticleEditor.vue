<template>
  <div class="pb-64">
    <div class="relative">
      <article-editor-bubble :editor="editor"/>
      <article-editor-floating :editor="editor"/>
    </div>
    <editor-content class="Editor article-content" :editor="editor" :class="hints"/>

<!--    <code>-->
<!--      <pre>{{revision}}</pre>-->
<!--    </code>-->
  </div>
</template>

<script>
  import {Editor, EditorContent} from 'tiptap'

  import {HardBreak, History, Italic, Bold, Underline, Link, Focus} from 'tiptap-extensions'
  import ArticleEditorBubble from "./editor/ArticleEditorBubble";
  import ArticleEditorFloating from "./editor/ArticleEditorFloating";
  import Heading from './node/ArticleHeadingNode'
  import Image from './node/ArticleImageNode'
  import Line from './node/ArticleLineNode'
  import Avatar from './node/ArticleAvatarNode'
  import Place from './node/ArticlePlaceNode'

  export default {
    name: "ArticleEditor",
    components: {ArticleEditorFloating, ArticleEditorBubble, EditorContent},
    props: {
      value: {
        type: Object,
      },
    },
    computed: {
      hints() {
        const content = this.value.content
        return {
          'HintTitle': content[0]?.type === "heading"
            && !content[0]?.content || content[0]?.content?.length === 0,
          'HintParagraph': content[1]?.type === "paragraph"
            && !content[1]?.content || content[1]?.content?.length === 0,
          'HintContinue': this.isInactive
        }
      },
      isInactive() {
        if (this.lastEditedTime == null) return true

        const millisSinceLastEdit = this.time - this.lastEditedTime
        if (millisSinceLastEdit > 4000) {
          return true
        }
      },
      revision() {
        return this.value
      }
    },
    data() {
      return {
        editor: null,

        lastSavedTime: null,
        lastEditedTime: null,
        time: new Date().getTime(),
      }
    },
    mounted() {
      this.$$autoSave = setInterval(this.save, 10000)
      this.$$activeWatcher = setInterval(this.onActiveWatcher, 2000)
      window.onbeforeunload = this.onBeforeUnload

      this.editor = new Editor({
        extensions: [
          new History(),
          new Focus({className: 'Focused', nested: false,}),
          new HardBreak(),
          new Bold(),
          new Italic(),
          new Link(),
          new Underline(),
          new Heading(),
          new Line(),
          new Image(),
          new Avatar(),
          new Place(),
        ],
        content: {type: 'doc', content: this.revision.content},
        onUpdate: ({getJSON}) => {
          const revision = {
            ...this.revision,
            content: getJSON().content
          }

          // Check if last line is empty.
          const last = _.last(revision.content)
          if (!(last.type === 'paragraph' && !last.content)) {
            const paragraph = this.editor.schema.node('paragraph')
            const tr = this.editor.state.tr
            this.editor.view.dispatch(tr.insert(tr.doc.content.size, paragraph))
          }

          this.$emit('input', revision)
          this.onEditorUpdate()
        },
      })
    },
    beforeDestroy() {
      clearInterval(this.$$autoSave)
      clearInterval(this.$$activeWatcher)
      window.onbeforeunload = undefined

      this.save()
      if (this.editor) {
        this.editor.destroy()
      }
    },
    methods: {
      onBeforeUnload() {
        if (this.lastEditedTime == null) return
        if (this.lastSavedTime === this.lastEditedTime) return

        this.save()
        return 'You have unsaved changes!'
      },
      onActiveWatcher() {
        this.time = new Date().getTime()
      },
      onEditorUpdate() {
        this.lastEditedTime = new Date().getTime()
      },
      save() {
        // if (!this.started) return

        const lastEditedTime = this.lastEditedTime
        if (lastEditedTime == null || lastEditedTime === this.lastSavedTime) {
          return
        }

        console.log('ArticleEditor.save()')
        this.$emit('on-save', {
          callback: () => {
            this.lastSavedTime = lastEditedTime
          }
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

  .EditorPlaceholder {
    float: left;
    color: #aaa;
    pointer-events: none;
    height: 0;
  }

  .HintTitle {
    h1:first-child::before:extend(.EditorPlaceholder) {
      content: "Title";
    }
  }

  .HintContinue {
    > div > p:last-child::before:extend(.EditorPlaceholder) {
      content: "Continue writing …";
      font-style: italic;
    }
  }

  .HintParagraph {
    > div > p:last-child::before:extend(.EditorPlaceholder) {
      content: "Write something …";
      font-style: italic;
    }
  }
</style>
