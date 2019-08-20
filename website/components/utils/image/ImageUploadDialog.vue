<template>
  <div class="fixed position-0 bg-overlay flex-center index-dialog">
    <div>
      <div class="flex-justify-end">
        <div class="CloseButton absolute hover-pointer" @click="onClose">
          <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>

      <div class="Dialog dialog-xlarge flex-row wh-100" v-on-clickaway="onClose">
        <div class="flex-grow">
          <h2>Media library</h2>

          <div class="mtb-24">
            <div class="MediaList flex-wrap flex-1-2-3-4 overflow-y-auto" >
              <div class="p-12" v-for="image in images" :key="image.id" @click="onImage(image)">
                <div class="aspect r-1-1">
                  <cdn-img class="border-3 overflow-hidden" type="320x320" :image="image">
                    <div class="hover-bg-a40 hover-pointer">
                    </div>
                  </cdn-img>
                </div>
              </div>
            </div>

            <div class="flex-center ptb-48" v-if="next">
              <button class="blue-outline" @click="onLoadMore">Load More</button>
            </div>

            <div class="flex-center ptb-48" v-if="!images || images.length === 0">
              <p>You don't have any image available.</p>
            </div>
          </div>
        </div>
        <div class="ml-32 MediaSource">
          <button @click="onUploadImage" class="w-100 blue-outline small">Upload Image</button>
          <div class="flex-column mt-24">
            <div class="hr-bot pb-8">
              <h5>Media source</h5>
            </div>
            <div class="mt-8">
              <div class="ptb-6" v-for="source in sources" :key="source.type">
                <div class="lh-1 small p-12 border-3 hover-pointer" @click="onSource(source)"
                     :class="{'bg-steam black': !source.selected, 'bg-blue white': source.selected}">
                  {{source.name}}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="absolute" v-show="false">
          <!-- Must be placed inside v-on-clickaway="onClose" -->
          <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg"
                 @change="(e) => onFileChanged(e)">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "./CdnImg";

  export default {
    name: "ImageUploadDialog",
    components: {CdnImg},
    props: {
      source: {
        type: String,
        default: 'LIBRARY'
      }
    },
    data() {
      return {
        sources: [
          {name: 'Uploaded', type: 'LIBRARY', selected: false},
          {name: 'From Profile', type: 'PROFILE', selected: false},
          {name: 'From Article', type: 'ARTICLE', selected: false},
          {name: 'From Instagram', type: 'INSTAGRAM', selected: false}
        ],
        images: [],
        cursor: {}
      }
    },
    computed: {
      next() {
        return this.cursor?.next
      },
      selectSources() {
        return this.sources.filter(s => s.selected)
          .map(s => s.type)
          .join(",")
      }
    },
    mounted() {
      this.reload()
    },
    methods: {
      reload() {
        this.images.splice(0)

        this.$api.get('/me/images', {params: {sources: this.selectSources, size: 20}})
          .then(({data: images, cursor}) => {
            this.images.splice(0)
            this.images.push(...images)
            this.cursor = cursor
          })
      },
      onLoadMore() {
        this.$api.get('/me/images', {params: {sources: this.selectSources, size: 20, cursor: this.next}})
          .then(({data: images, cursor}) => {
            this.images.push(...images)
            this.cursor = cursor
          })
      },
      onClose() {
        this.$emit('on-close')
      },
      onSource(source) {
        source.selected = !source.selected
        this.reload()
      },
      onImage(image) {
        this.$emit('on-image', image)
      },
      onUploadImage() {
        this.$refs.fileInput.click()
      },
      onFileChanged(event) {
        const file = event.target.files[0]

        return this.$api.postImage(file, this.source)
          .then(({data: image}) => {
            this.images.splice(0, 0, image)
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .Dialog {
    height: 600px;
  }

  .CloseButton {
    margin-top: -48px;
  }

  .MediaList {
    margin: -12px;
  }

  .MediaSource {
    min-width: 160px;
  }
</style>
