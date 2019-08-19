<template>
  <editor-floating-menu :editor="editor">
    <div slot-scope="{commands,isActive,menu}" class="MenuFloating absolute index-top-elevation flex-align-center"
         v-on-clickaway="onClose"
         :class="{Active:menu.isActive || state}" :style="`top: ${menu.top}px`">

      <div class="Menu">
        <div class="ButtonFloating" @click="state = 'reveal'">
          <simple-svg v-if="state === 'reveal'" fill="#000" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
          <simple-svg v-else fill="black" :filepath="require('~/assets/icon/icons8-plus-math.svg')"/>
        </div>
      </div>

      <div class="Panel flex" v-if="state === 'reveal'">
        <div class="ButtonFloating bg-white" @click="onClickType(commands, 'line')">
          <simple-svg fill="black" :filepath="require('~/assets/icon/icons8-dashed-line.svg')"/>
        </div>
      </div>
    </div>
  </editor-floating-menu>
</template>

<script>
  import {EditorFloatingMenu} from 'tiptap'

  export default {
    name: "ArticleEditorFloating",
    components: {EditorFloatingMenu},
    props: {
      editor: Object,
    },
    data() {
      return {
        state: null,
      }
    },
    methods: {
      onClose() {
        this.menu = false
      },
      onClickType(commands, type) {
        this.menu = false
        return commands[type]()
      },
    }
  }
</script>

<style scoped lang="less">
  .MenuFloating {
    border-radius: 4px;
    padding: 0 8px;
    margin-left: -64px;

    visibility: hidden;
    opacity: 0;
    transition: opacity 0.2s, visibility 0.2s;

    &.Active {
      opacity: 1;
      visibility: visible;
    }
  }

  .ButtonFloating {
    height: 32px;
    width: 32px;
    border-radius: 16px;

    padding: 4px;
    margin-right: 10px;

    border: 2px solid rgba(0, 0, 0, 0.7);

    &:hover {
      cursor: pointer;
    }
  }

  .Panel {
    padding-right: 80px;
  }

  .Menu {
    margin-right: 12px;
  }
</style>
