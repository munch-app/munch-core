<template>
  <editor-menu-bubble :editor="editor">
    <div class="Bubble absolute bg-b85 index-top-elevation white weight-600"
         slot-scope="{commands,isActive,getMarkAttrs,menu}"
         :class="{Active: state || menu.isActive && (isActive.paragraph() || isActive.heading())}"
         :style="`left:${menu.left}px; bottom: ${menu.bottom}px;`">

      <template v-if="state === 'link'">
        <form @submit.prevent="linkSetUrl(commands.link, link.url)" class="Link flex-align-center">
          <input type="text" v-model="link.url" placeholder="https://" ref="linkInput"
                 @keydown.esc="state = null" @focusout="linkSetUrl(commands.link, link.url)"/>

          <div @click="linkSetUrl(commands.link, null)" class="ml-8">
            <simple-svg class="wh-20px" fill="white" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
          </div>
        </form>
      </template>

      <template v-else>
        <div class="flex-align-center">
          <template v-if="isActive.paragraph()">
            <div class="Button bold"
                 :class="{blue: isActive.bold()}"
                 @click="commands.bold"
            >
              B
            </div>
            <div class="Button italic"
                 :class="{blue: isActive.italic()}"
                 @click="commands.italic"
            >
              I
            </div>
            <div class="Button text-underline"
                 :class="{blue: isActive.underline()}"
                 @click="commands.underline"
            >
              U
            </div>

            <div class="Button" @click="setStateLink(getMarkAttrs('link'))">
              <simple-svg class="wh-20px"
                          :fill=" isActive.link() ?'#07F' : 'white'"
                          :filepath="require('~/assets/icon/icons8-link.svg')"/>
            </div>

            <div class="Separator"/>
          </template>

          <div class="Button weight-600"
               :class="{blue: isActive.heading({level:1})}"
               @click="commands.heading({level:1})"
          >
            H1
          </div>
          <div class="Button weight-600"
               :class="{blue: isActive.heading({level:2})}"
               @click="commands.heading({level:2})"
          >
            H2
          </div>
        </div>
      </template>

    </div>
  </editor-menu-bubble>
</template>

<script>
  import {EditorMenuBubble} from 'tiptap'

  export default {
    name: "ArticleEditorBubble",
    components: {EditorMenuBubble},
    props: {
      editor: Object
    },
    data() {
      return {
        state: null,
        link: {
          url: null,
        },
      }
    },
    methods: {
      setStateLink(attrs) {
        this.state = 'link'
        this.link.url = attrs.href

        this.$nextTick(() => {
          this.$refs.linkInput.focus()
        })
      },
      linkSetUrl(command, url) {
        command({href: url})
        this.link.url = null
        this.state = null

        this.linkMenuHide()
        this.editor.focus()
      },
    }
  }
</script>

<style scoped lang="less">
  .Bubble {
    border-radius: 4px;
    padding: 0 8px;

    transform: translateX(-50%);
    visibility: hidden;
    opacity: 0;
    transition: opacity 0.2s, visibility 0.2s;

    &.Active {
      opacity: 1;
      visibility: visible;
    }
  }

  .Link {
    input {
      background: transparent;
      color: white;
      border: 0;

      font-size: 17px;
      padding: 10px 16px 10px 6px;
    }
  }

  .Separator {
    background-color: rgba(255, 255, 255, 0.5);
    width: 1px;
    height: 24px;
    margin: 6px 8px;
  }

  .Button {
    font-size: 17px;
    line-height: 1;

    padding: 10px;
    background-color: initial;

    &:hover {
      cursor: pointer;
    }
  }
</style>
