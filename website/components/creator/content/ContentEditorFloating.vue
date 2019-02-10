<template>
  <editor-floating-menu :editor="editor">
    <div slot-scope="{commands,isActive,menu}" class="MenuFloating absolute index-top-elevation flex-align-center"
         v-on-clickaway="onCloseBubble"
         :class="{Active:menu.isActive || show}" :style="`top: ${menu.top}px`">

      <div class="Menu">
        <div class="ButtonFloating" @click="show = !show">
          <simple-svg v-if="!show" fill="black" :filepath="require('~/assets/icon/creator/add.svg')"/>
          <simple-svg v-else fill="black" :filepath="require('~/assets/icon/creator/close.svg')"/>
        </div>
      </div>

      <div class="Panel flex" v-if="show">
        <div class="ButtonFloating" @click="onClickType(commands, 'place')">
          <simple-svg fill="black" :filepath="require('~/assets/icon/creator/place.svg')"/>
        </div>
        <div class="ButtonFloating" @click="onClickType(commands, 'image')">
          <simple-svg fill="black" :filepath="require('~/assets/icon/creator/image.svg')"/>
        </div>
        <div class="ButtonFloating" @click="onClickType(commands, 'line')">
          <simple-svg fill="black" :filepath="require('~/assets/icon/creator/line.svg')"/>
        </div>
      </div>

      <div class="absolute" v-show="false">
        <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg"
               @change="(e) => onFileChanged(commands, e)">
      </div>

      <div v-if="place">
        <content-editor-place-dialog @selected="(p) => onPlaceSelected(commands, p)" @close="onClosePlace"/>
      </div>
    </div>
  </editor-floating-menu>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {EditorFloatingMenu} from 'tiptap'
  import ImageSizes from "../../core/ImageSizes";
  import ContentEditorPlaceDialog from "./ContentEditorPlaceDialog";

  export default {
    name: "ContentEditorFloating",
    components: {ContentEditorPlaceDialog, ImageSizes, EditorFloatingMenu},
    props: {
      editor: Object
    },
    data() {
      return {
        show: false,
        place: false,
      }
    },
    computed: {
      ...mapGetters('creator', ['creatorId']),
    },
    methods: {
      onCloseBubble() {
        this.show = false
      },
      onClosePlace() {
        this.place = false
        this.show = false
      },
      onClickType(commands, type) {
        this.show = false

        switch (type) {
          case 'image':
            return this.$refs.fileInput.click()

          case 'place':
            this.place = true
            break

          default:
            return commands[type]()
        }
      },
      onFileChanged(commands, event) {
        const file = event.target.files[0]

        const form = new FormData()
        form.append('file', file, file.name)

        return this.$axios.$post(`/files/creators/${this.creatorId}/images`, form)
          .then(({data}) => {
            console.log(data)
            commands['image']({image: data, caption: null})

            this.$emit('save')
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
      onPlaceSelected(commands, place) {
        this.show = false
        this.place = false
        console.log(place)
        commands['place']({placeId: place.placeId, placeName: place.name})
      }
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

    border: 1px solid rgba(0, 0, 0, 0.7);

    &:hover {
      cursor: pointer;
    }
  }

  .Menu {
    margin-right: 12px;
  }
</style>
