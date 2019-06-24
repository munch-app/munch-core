<template>
  <editor-menu-bubble :editor="editor">
    <div slot-scope="{commands,isActive,getMarkAttrs,menu}"
         class="MenuBubble absolute bg-b85 index-top-elevation  white weight-600"
         :class="{Active: menu.isActive && (isActive.paragraph() || isActive.heading()) || link.menu}"
         :style="`left:${menu.left}px; bottom: ${menu.bottom}px;`">

      <form v-if="link.menu" @submit.prevent="linkSetUrl(commands.link, link.url)"
            class="flex-align-center">
        <input class="LinkInput" type="text" v-model="link.url" placeholder="https://" ref="linkInput"
               @keydown.esc="linkMenuHide" @focusout="linkSetUrl(commands.link, link.url)"/>


        <div @click="linkSetUrl(commands.link, null)" class="ml-8">
          <simple-svg class="wh-20px" fill="white" :filepath="require('~/assets/icon/close.svg')"/>
        </div>
      </form>

      <template v-else>
        <div class="flex-align-center">
          <div class="BubbleButton" :class="{p500: isActive.bold()}" @click="commands.bold">B</div>
          <div class="BubbleButton italic" :class="{p500: isActive.italic()}" @click="commands.italic">i</div>
          <div class="BubbleButton text-underline" :class="{p500: isActive.underline()}" @click="commands.underline">U
          </div>
          <div class="BubbleButton" @click="linkMenuShow(getMarkAttrs('link'))">
            <simple-svg class="wh-20px"
                        :fill=" isActive.link() ?'#F05F3B' : 'white'"
                        :filepath="require('~/assets/icon/icons8-link.svg')"/>
          </div>
          <div class="BubbleLine"></div>
          <div class="BubbleButton" :class="{p500: isActive.heading({level:1})}" @click="commands.heading({level:1})">
            H1
          </div>
          <div class="BubbleButton" :class="{p500: isActive.heading({level:2})}" @click="commands.heading({level:2})">
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
    name: "ContentEditorBubble",
    components: {EditorMenuBubble},
    props: {
      editor: Object
    },
    data() {
      return {
        link: {
          url: null,
          menu: false,
        },
        place: {
        },
      }
    },
    methods: {
      linkMenuShow(attrs) {
        this.link.url = attrs.href
        this.link.menu = true
        this.$nextTick(() => {
          this.$refs.linkInput.focus()
        })
      },
      linkMenuHide() {
        this.link.url = null
        this.link.menu = false
      },
      linkSetUrl(command, url) {
        command({href: url})
        this.linkMenuHide()
        this.editor.focus()
      },
      placeSetImage() {

      },
      placeSetOption(number) {

      }
    }
  }
</script>

<style scoped lang="less">
  .MenuBubble {
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

  .BubbleButton {
    font-size: 17px;
    line-height: 1;

    padding: 10px;
    background-color: initial;

    &:hover {
      cursor: pointer;
    }
  }

  .BubbleLine {
    background-color: rgba(255, 255, 255, 0.25);
    width: 1px;
    height: 24px;
    margin: 6px 8px;
  }

  .LinkInput {
    background: transparent;
    color: white;
    border: 0;

    font-size: 17px;
    padding: 10px 16px 10px 6px;
  }
</style>
