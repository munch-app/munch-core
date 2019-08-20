<template>
  <editor-floating-menu :editor="editor">
    <div class="Floating absolute index-top-elevation flex-align-center"
         slot-scope="{commands,isActive,menu}"
         v-on-clickaway="onClose"
         :class="{Active:menu.isActive || state}" :style="`top: ${menu.top}px`">

      <div class="LeftButton">
        <div class="Button" @click="onLeftBtn">
          <div v-if="state === 'reveal'">
            <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
          </div>
          <div v-else>
            <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-plus-math.svg')"/>
          </div>
        </div>
      </div>

      <div class="Panel bg-white flex" v-if="state === 'reveal'">
        <div class="Button" @click="onType(commands, 'line')">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-dashed-line.svg')"/>
        </div>

        <div class="Button" @click="onType(commands, 'image')">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
        </div>

        <div class="Button" @click="onType(commands, 'place')">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-map-pin.svg')"/>
        </div>

        <div class="Button" @click="onType(commands, 'avatar')">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-person.svg')"/>
        </div>
      </div>

      <image-upload-dialog v-if="state === 'image'" @on-image="(image) => onImage(commands, image)" @on-close="onClose"/>
    </div>
  </editor-floating-menu>
</template>

<script>
  import {EditorFloatingMenu} from 'tiptap'
  import ImageUploadDialog from "../../utils/image/ImageUploadDialog";

  export default {
    name: "ArticleEditorFloating",
    components: {ImageUploadDialog, EditorFloatingMenu},
    props: {
      editor: Object,
    },
    data() {
      return {
        state: null,
      }
    },
    methods: {
      onLeftBtn() {
        if (this.state === 'reveal') {
          this.state = null
        } else {
          this.state = 'reveal'
        }
      },
      onClose() {
        this.state = null
      },
      onType(commands, type) {
        switch (type) {
          case 'place':
          case 'image':
            this.state = type
            break

          case 'avatar':
          case 'line':
            commands[type]()
            this.state = null
            break
        }
      },
      onImage(commands, image) {
        this.state = null
        commands['image']({image: image, caption: null})
      }
    }
  }
</script>

<style scoped lang="less">
  .Floating {
    border-radius: 4px;
    padding: 0 8px;

    margin-top: -5px;
    margin-left: -64px;

    visibility: hidden;
    opacity: 0;
    transition: opacity 0.2s, visibility 0.2s;

    &.Active {
      opacity: 1;
      visibility: visible;
    }
  }

  .Button {
    height: 34px;
    width: 34px;
    border-radius: 50%;

    padding: 6px;
    margin-right: 12px;

    border: 2px solid rgba(0, 0, 0, 0.7);
    background: white;

    &:hover {
      cursor: pointer;
    }
  }

  .LeftButton {
    margin-right: 10px;
  }

  .Panel {
    padding-right: 80px;
  }
</style>
