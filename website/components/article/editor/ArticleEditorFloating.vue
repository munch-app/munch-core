<template>
  <editor-floating-menu :editor="editor">
    <div class="Floating absolute index-elevation flex-align-center"
         slot-scope="{commands, isActive, menu}"
         v-on-clickaway="onClose"
         :class="{Active: menu.isActive || state}" :style="`top: ${menu.top}px`">

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
        <div v-for="button in buttons" :key="button.type" class="Button" @click="onType(commands, button.type)">
          <simple-svg fill="#000" :filepath="button.icon"/>
        </div>
      </div>

      <portal-dialog>
        <div class="dialog-large dialog-h80vh border p-0">
          <search-place input-hint="Search restaurant" :size="20" create fixed
                        @on-select="(document) => onPlaceSelect(commands, document)"
                        @on-create="(document) => onPlaceCreate(commands, document)"
          >
            <template v-slot:default="{document}">
              <div class="p-16 hover-bg-a10">
                <div class="flex">
                  <div class="wh-80px flex-no-shrink mr-16" v-if="document.image">
                    <cdn-img class="border-2 overflow-hidden" type="320x320" :image="document.image"/>
                  </div>
                  <div>
                    <h5 class="text-ellipsis-1l">{{document.name}}</h5>
                    <p class="text-ellipsis-1l m-0">{{document.location.address}}</p>
                    <div class="flex-align-center">
                      <div v-if="document.status.type !== 'OPEN'"
                           class="white tiny-bold bg-error p-6 lh-1 border-2 mr-8">
                        {{document.status.type}}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </search-place>
        </div>
      </portal-dialog>
    </div>
  </editor-floating-menu>
</template>

<script>
  import {EditorFloatingMenu} from 'tiptap'
  import ImageUploadDialog from "../../dialog/ImageUploadDialog";
  import PortalDialog from "../../dialog/PortalDialog";
  import SearchPlace from "../../places/SearchPlace";
  import CdnImg from "../../utils/image/CdnImg";

  export default {
    name: "ArticleEditorFloating",
    components: {CdnImg, SearchPlace, PortalDialog, ImageUploadDialog, EditorFloatingMenu},
    props: {
      editor: Object,
    },
    data() {
      return {
        state: null,
        buttons: [
          {type: 'line', icon: require('~/assets/icon/icons8-dashed-line.svg')},
          {type: 'image', icon: require('~/assets/icon/icons8-camera.svg')},
          {type: 'place', icon: require('~/assets/icon/icons8-map-pin.svg')},
          {type: 'avatar', icon: require('~/assets/icon/icons8-person.svg')},
        ]
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
            this.onPlace(commands)
            break

          case 'image':
            this.onImage(commands)
            break

          case 'avatar':
          case 'line':
            commands[type]()
            this.state = null
            break
        }
      },
      onImage(commands) {
        this.$store.commit('global/setDialog', {
          name: 'ImageUploadDialog', props: {
            onImage: (image) => {
              this.$store.commit('global/clearDialog');
              commands['image']({image, caption: null})
            }
          }
        })
      },
      onPlace(commands) {
        this.$store.commit('global/setDialog', {
          name: 'PortalDialog'
        })
      },
      onPlaceSelect(commands, document) {
        this.$store.commit('global/setDialog', 'LoadingDialog')
        this.$api.get(`/places/${document.id}`)
          .then(({data: place}) => {
            commands['place']({place})
          })
          .catch(err => {
            this.$store.dispatch('addError', err)
          })
          .finally(() => {
            this.$store.commit('global/clearDialog');
          })
      },
      onPlaceCreate(commands, document) {
        this.$store.commit('global/clearDialog');
        commands['place']({place: document})
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
