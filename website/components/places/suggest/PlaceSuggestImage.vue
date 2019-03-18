<template>
  <div>
    <h2 class="mt-48">Images</h2>
    <div class="mt-16">
      <button class="small secondary-outline" @click="showImages" v-if="images.length > 0">Edit Images</button>
      <button class="small primary-outline ml-16" @click="showUploadImage">Upload Image</button>
    </div>

    <no-ssr>
      <portal to="dialog-full" v-if="show.images">
        <div class="Existing bg-white elevation-1 p-16-24 overflow-y-auto" v-on-clickaway="onClose">
          <h2>Images</h2>
          <p>Own an Instagram account, want it published on munch? <a class="text-underline s700 weight-600"
                                                                      href="https://partner.munch.app"
                                                                      target="_blank">partner.munch.app</a></p>

          <div class="List flex-wrap">
            <div class="Image hover-pointer" v-for="image in images" :key="image.imageId" @click="onDialog(image)">
              <div class="aspect r-1-1">
                <image-sizes class="overflow-hidden border-3" :sizes="image.sizes" width="1" height="1">
                  <div class="OverlayA20 relative hover-bg-a60 wh-100 flex-center">
                    <simple-svg class="wh-32px" fill="white"
                                :filepath="require('~/assets/icon/place/suggest/flag.svg')"/>
                  </div>
                </image-sizes>
              </div>
            </div>
          </div>

          <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
            <beat-loader color="#084E69" v-if="next.sort" size="14px"
                         v-observe-visibility="{callback: (v) => v && onMore(),throttle:300}"
            />
          </no-ssr>
        </div>
      </portal>
    </no-ssr>

    <no-ssr>
      <portal to="dialog-w768" v-if="show.uploadImages">
        <h2>Upload Images</h2>
        <p class="small mt-4">Upload images of <b>{{payload.place.name}}</b>. <span class="">Maximum of 4 images</span>
        </p>

        <div class="mtb-16">
          <dropzone id="imageDropzone" ref="imageDropzone" :options="dropzoneOptions"></dropzone>
        </div>

        <div class="flex-justify-end mt-16">
          <button class="" @click="onCloseImageUpload">Cancel</button>
          <button class="primary ml-24" @click="onImageUpload">Upload</button>
        </div>
      </portal>
    </no-ssr>

    <no-ssr>
      <portal to="dialog-styled" v-if="dialog" class="Dialog">
        <h3>Flag Image</h3>
        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotRelatedContent')">
          <div class="text">Not related image</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotRelatedPlace')">
          <div class="text">Does not belong to place</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'ExplicitContent')">
          <div class="text">Explicit content</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'TypeFoodGood')">
          <div class="text">Good food image</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'TypeFoodBad')">
          <div class="text">Bad food image</div>
          <div class="checkbox"/>
        </div>

        <div class="right">
          <button class="elevated" @click="onDialogCancel">Cancel</button>
        </div>
      </portal>
    </no-ssr>
  </div>
</template>

<script>
  import Vue from 'vue'
  import _ from 'lodash'
  import ImageSizes from "../../core/ImageSizes";
  import Dropzone from 'nuxt-dropzone'
  import 'nuxt-dropzone/dropzone.css'

  export default {
    name: "PlaceSuggestImage",
    components: {ImageSizes, Dropzone},
    props: {
      payload: {
        type: Object,
        twoWay: true
      }
    },
    data() {
      const {images} = this.payload
      if (!images || images.length === 0) {
        return {
          dialog: null,
          loading: false,
          selected: false,
          images: [], next: {},
        }
      }
      const last = images[images.length - 1]
      const next = {sort: last.sort || null}

      return {
        dialog: null,
        loading: false,
        images, next,
        selected: false,
        show: {
          images: false,
          uploadImages: false
        },
        dropzoneOptions: {
          url: "http://httpbin.org/anything",
          addRemoveLinks: true,
          acceptedFiles: "image/jpeg,image/png",
          maxFilesize: 10,
          maxFiles: 4,
          autoProcessQueue: false,
        }
      }
    },
    mounted() {
      // Push Place Images
      if (this.payload.origin) {
        this.payload.origin.images.forEach(image => {
          if (!_.filter(this.images, (i) => i.imageId === image.imageId)) {
            this.images.push(image)
          }
        })
      }
    },
    methods: {
      onMore() {
        if (this.loading) return
        if (!this.next.sort) return

        this.loading = true
        const params = {
          size: 20,
          'next.sort': this.next.sort
        }

        return this.$axios.$get(`/api/places/${this.payload.place.placeId}/images`, {params})
          .then(({data, next}) => {
            this.images.push(...data)
            this.loading = false
            this.next.sort = next && next.sort || null
          })
      },
      onDialog(image) {
        const flag = this.$route.query.flag
        if (flag === 'NotRelated') {
          this.onFlag(image, flag)
        } else {
          this.dialog = image
        }
      },
      onFlag(image, flag) {
        this.dialog = null
        this.selected = true

        this.$store.dispatch('addMessage', {title: `Flagged as ${flag}`})
        Vue.set(this.payload.removes.images, image.imageId, {flag, image})
      },
      showImages() {
        this.show.images = true
      },
      showUploadImage() {
        this.show.uploadImages = true
      },
      onImageUpload() {
        this.show.uploadImages = false
        let dropzoneInstance = this.$refs.imageDropzone
        const files = dropzoneInstance.getAcceptedFiles()
        let overMax = false

        files.forEach((file) => {
          if (Object.keys(this.payload.uploads.images).length + 1 < 5) {
            Vue.set(this.payload.uploads.images, file.upload.uuid, file)
          } else {
            overMax = true
          }
        });

        if (overMax) {
          this.$store.dispatch('addMessage', {
            title: "Maximum Upload Reached",
            message: "There are more than 4 images selected for upload. Some images have been omitted."
          })
        }
      },
      onDialogCancel() {
        this.dialog = null
        this.selected = true
      },
      onClose() {
        if (this.dialog == null && !this.selected) {
          this.show.images = false
        }
        this.selected = false
      },
      onCloseImageUpload() {
        this.show.uploadImages = false
      },
    }
  }
</script>

<style scoped lang="less">

  .vue-dropzone {
    line-height: 1;
  }

  .Existing {
    .List {
      margin: 8px -8px;
    }

    margin-left: auto;
    margin-right: auto;
    margin-top: 10vh;
    max-height: 100% - 20vh;
    @media (min-width: 768px) {
      width: 600px;
    }
  }

  .Image {
    padding: 8px;

    flex: 0 0 25%;

    @media (max-width: 576px) {
      flex: 0 0 33.333%;
    }

    .OverlayA20 {
      background: rgba(0, 0, 0, 0.2);
    }
  }

  .Dialog {
    > div {
      padding-top: 8px;
      padding-bottom: 8px;
      margin-bottom: 0;
    }
  }

  .FileUpload {
    width: 400px;
    height: 400px;
  }

  .file-uploads {
    overflow: hidden;
    position: relative;
    text-align: center;
    display: inline-block;
  }

  .file-uploads.file-uploads-html4 input[type="file"] {
    opacity: 0;
    font-size: 20em;
    z-index: 1;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    position: absolute;
    width: 100%;
    height: 100%;
  }

  .file-uploads.file-uploads-html5 input[type="file"] {
    overflow: hidden;
    position: fixed;
    width: 1px;
    height: 1px;
    z-index: -1;
    opacity: 0;
  }
</style>
