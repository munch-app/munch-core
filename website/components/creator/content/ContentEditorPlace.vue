<template>
  <div class="ContentPlace">
    <div v-if="loading">
      <div>Loading...</div>
    </div>
    <div v-else class="PlaceCard relative">
      <div @click.capture.prevent.stop>
        <content-place :place="place" :place-id="placeId" :place-name="placeName" :options="options"/>
      </div>
      <div class="PlaceOption cubic-bezier absolute-0 flex-center">
        <div class="flex-center">
          <button @click="image.dialog = true" class="small">Edit Image</button>
          <button @click="style.dialog = true" class="small">Change Style</button>
        </div>
      </div>
    </div>

    <portal to="dialog-action-sheet" v-if="style.dialog">
      <div @click="styleUpdate(1)">Rectangle Card</div>
      <div @click="styleUpdate(2)">Large Banner</div>
      <hr>
      <div @click="style.dialog = false">Cancel</div>
    </portal>

    <portal to="dialog-w768" v-if="image.dialog">
      <div v-on-clickaway="closeAll">
        <div v-if="image.list.length > 0">
          <h4 class="mb-24 text-center">Choose from restaurant</h4>
          <div class="ImageList flex">
            <div class="Image border-3 flex-no-shrink overflow-hidden hover-pointer" v-for="image in image.list"
            @click="imageUpdate(image)">
              <div class="aspect r-1-1">
                <image-sizes :sizes="image.sizes" :alt="place.name" :height="100" :width="100"/>
              </div>
            </div>
          </div>
        </div>
        <div class="flex-center">
          <div class="w-100">
            <hr>
          </div>
          <div class="p-24">or</div>
          <div class="w-100">
            <hr>
          </div>
        </div>
        <div class="flex-center">
          <button class="border" @click="imageUpload">upload your own</button>
        </div>


        <div class="absolute" v-show="false">
          <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg"
                 @change="(e) => imageOnFileChanged(e)">
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import ContentPlace from "../../contents/ContentPlace";
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "ContentEditorPlace",
    components: {ImageSizes, ContentPlace},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      placeId: {
        get() {
          return this.node.attrs.placeId
        },
        set(placeId) {
          this.updateAttrs({
            placeId,
          })
        },
      },
      placeName: {
        get() {
          return this.node.attrs.placeName
        },
        set(placeName) {
          this.updateAttrs({
            placeName,
          })
        },
      },
      options: {
        get() {
          return this.node.attrs.options || {}
        },
        set(options) {
          this.updateAttrs({
            options,
          })
        },
      },
      ...mapGetters('creator', ['creatorId']),
    },
    data() {
      return {
        place: null,
        loading: true,
        style: {
          dialog: false
        },
        image: {
          dialog: false,
          list: []
        }
      }
    },
    mounted() {
      this.$api.get(`/places/${this.placeId}`)
        .then(({data: {place, images}}) => {
          this.place = place
          this.image.list = images
          this.loading = false
        })
        .catch((err) => {
          if (err.statusCode === 404) {
            this.loading = false
          } else {
            this.$store.dispatch('addError', err)
          }
        })
    },
    methods: {
      closeAll() {
        this.style.dialog = false
        this.image.dialog = false
      },
      styleUpdate(style) {
        this.options = {
          ...this.options,
          style: style
        }
        this.style.dialog = false
      },
      imageUpdate(image) {
        this.options = {
          ...this.options,
          image: image
        }
        this.image.dialog = false
      },
      imageUpload() {
        return this.$refs.fileInput.click()
      },
      imageOnFileChanged(event) {
        const file = event.target.files[0]

        const form = new FormData()
        form.append('file', file, file.name)

        return this.$api.post(`/creators/_/contents/_/images`, form)
          .then(({data}) => {
            console.log(data)
            this.imageUpdate(data)
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .PlaceCard {
    white-space: initial;
  }

  .PlaceOption {
    background: rgba(0, 0, 0, 0.4);
    opacity: 0;

    &:hover {
      opacity: 1;
    }

    button {
      margin: 0 12px;
    }
  }

  .ImageList {
    overflow-x: auto;
    max-width: 768px;
    width: 100%;

    .Image {
      width: 100px;
      height: 100px;

      margin-right: 16px;
    }
  }
</style>
