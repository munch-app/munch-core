<template>
  <div class="fixed position-0 bg-overlay flex-center index-dialog">
    <div>
      <div class="flex-justify-end">
        <div class="CloseButton absolute hover-pointer" @click="onClose">
          <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>

      <div class="Dialog dialog-xlarge flex-row wh-100" v-on-clickaway="onClose">
        <div class="hr-right">
          <div v-for="select in selectors" :key="select.type" @click="onSelect(select.type)"
               class="p-16-24 hover-pointer hr-bot flex-column-align-center">
            <simple-svg class="wh-24px" :fill="select.type === selected ? '#07F' : '#000'" :filepath="select.icon"/>
            <div :class="{'blue': select.type === selected}" class="mt-4 small-bold">{{select.text}}</div>
          </div>
        </div>

        <div class="flex-grow p-32 flex-column">
          <h2 class="h2">Media library</h2>

          <div class="overflow-y-auto flex-grow">
            <div class="MediaList flex-wrap">
              <div class="MediaItem p-12" v-for="image in images" :key="image.id" @click="onImage(image)">
                <div class="aspect r-1-1 ">
                  <cdn-img class="border-3 overflow-hidden" type="320x320" :image="image">
                    <div class="hover-bg-a40 hover-pointer">
                    </div>
                  </cdn-img>
                </div>
              </div>
            </div>

            <div class="flex-center ptb-48" v-if="next">
              <button class="blue-outline" @click="loadRecentMore">Load More</button>
            </div>

            <div v-if="!loaded" class="flex-center p-24">
              <beat-loader color="#07F" size="16px"/>
            </div>

            <div class="flex-center ptb-48" v-if="loaded && images.length === 0">
              <p>You don't have any image available.</p>
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
      },
      place: {
        type: Object,
        default: null
      }
    },
    data() {
      return {
        selectors: [
          {type: 'upload', text: 'UPLOAD', icon: require('~/assets/icon/icons8-camera.svg')},
          {type: 'recent', text: 'RECENT', icon: require('~/assets/icon/icons8-time-machine.svg')},
          ...(this.place != null ? [
            {type: 'place', text: 'PLACE', icon: require('~/assets/icon/icons8-map-pin.svg')}
          ] : []),
        ],
        selected: 'recent',
        loaded: false,

        images: [],
        cursor: {},
      }
    },
    computed: {
      next() {
        return this.cursor?.next
      },
    },
    mounted() {
      this.loadRecent()
    },
    methods: {
      onSelect(type) {
        switch (type) {
          case 'upload':
            return this.onUploadImage()

          case 'recent':
            return this.loadRecent()

          case 'place':
            return this.loadPlace()
        }
      },
      loadRecent() {
        this.selected = 'recent'
        this.images.splice(0)
        this.loaded = false

        this.$api.get('/me/images', {params: {size: 20}})
          .then(({data: images, cursor}) => {
            this.images.push(...images)
            this.cursor = cursor
            this.loaded = true
          })
      },
      loadRecentMore() {
        this.$api.get('/me/images', {params: {size: 20, cursor: this.next}})
          .then(({data: images, cursor}) => {
            this.images.push(...images)
            this.cursor = cursor
          })
      },
      loadPlace() {
        this.selected = 'place'
        this.images.splice(0)
        this.loaded = false

        this.$api.get(`/migrations/places/${this.place.id}/images`)
          .then(({data: placeImages}) => {
            const images = placeImages.map(pi => pi.image)
            this.images.push(...images)
            this.cursor = null
            this.loaded = true
          })
      },
      onClose() {
        this.$emit('on-close')
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
    padding: 0;
  }

  .CloseButton {
    margin-top: -48px;
  }


  .MediaList {
    margin: 12px -12px;
  }

  .MediaItem {
    flex: 0 0 100%;
    max-width: 100%;

    @media (min-width: 1000px) {
      flex: 0 0 25%;
      max-width: 25%;
    }
  }
</style>
