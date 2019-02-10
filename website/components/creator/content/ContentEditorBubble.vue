<template>
  <editor-menu-bubble :editor="editor">
    <div slot-scope="{commands,isActive,menu}"
         class="MenuBubble absolute bg-b85 index-top-elevation flex white weight-600"
         :class="{Active: menu.isActive}"
         :style="`left:${menu.left}px; bottom: ${menu.bottom}px;`">

      <!--<div class="BubbleButton" :class="{p500: isActive.bold()}" @click="commands.bold">B</div>-->
      <!--<div class="BubbleButton italic" :class="{p500: isActive.italic()}" @click="commands.italic">i</div>-->
      <div class="BubbleLine"></div>
      <div class="BubbleButton" :class="{p500: isActive.heading({level:1})}" @click="commands.heading({level:1})">
        H1
      </div>
      <div class="BubbleButton" :class="{p500: isActive.heading({level:2})}" @click="commands.heading({level:2})">
        H2
      </div>

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
    margin: 6px 8px;
  }
</style>
