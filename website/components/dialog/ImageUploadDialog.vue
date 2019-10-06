<template>
  <div class="dialog-xlarge dialog-h80vh p-0 flex">
    <div class="hr-right">
      <div v-for="select in selectors" :key="select.type" @click="onSelect(select)"
           class="p-16-24 hover-pointer hr-bot flex-column-align-center">
        <div class="wh-40px border-circle flex-center" :class="select.type === selected ? 'bg-blue' : 'bg-steam'">
          <simple-svg class="wh-24px" :fill="select.type === selected ? '#FFF' : '#000'" :filepath="select.icon"/>
        </div>
        <div :class="{'blue': select.type === selected}" class="mt-4 tiny-bold">{{select.text}}</div>
      </div>
    </div>

    <div class="flex-grow flex-column p-24">
      <div class="overflow-y-auto flex-grow">
        <div class="flex-wrap">
          <div class="Image p-12" v-for="image in images" :key="image.id" @click="onImage(image)">
            <div class="aspect r-1-1">
              <cdn-img class="border-3 overflow-hidden" type="320x320" :image="image">
                <div class="hover-bg-a40 hover-pointer">
                </div>
              </cdn-img>
            </div>
          </div>
        </div>

        <div class="flex-center ptb-48" v-if="next">
          <button class="blue-outline" @click="loadMore">Load More</button>
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
      <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg"
             @change="(e) => onFileChanged(e)">
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

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
      },
      onImage: Function,
    },
    data() {
      return {
        selectors: [
          {type: 'upload', text: 'UPLOAD', icon: require('~/assets/icon/icons8-camera.svg')},
          {type: 'recent', text: 'RECENT', icon: require('~/assets/icon/icons8-time.svg')},
          {type: 'instagram', text: 'INSTAGRAM', icon: require('~/assets/icon/icons8-instagram.svg')},
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
      this.load()
    },
    methods: {
      onSelect(select) {
        switch (select.type) {
          case 'upload':
            return this.onUploadImage()

          case 'recent':
          case 'instagram':
          case 'place':
            if (this.selected !== select.type) {
              this.selected = select.type
              return this.load()
            }
        }
      },
      load() {
        const request = () => {
          switch (this.selected) {
            case 'place':
              return this.$api.get(`/migrations/places/${this.place.id}/images`)
                .then(({data: placeImages}) => {
                  return {data: placeImages.map(pi => pi.image), cursor: null}
                })

            case 'recent':
              return this.$api.get('/me/images', {params: {size: 20}})

            case 'instagram':
              return this.$api.get('/me/images', {params: {sources: 'INSTAGRAM', size: 20}})
          }
        }

        this.images.splice(0)
        this.loaded = false

        request().then(({data: images, cursor}) => {
          this.images.push(...images)
          this.cursor = cursor
          this.loaded = true
        })
      },
      loadMore() {
        const request = () => {
          switch (this.selected) {
            case 'instagram':
              return this.$api.get('/me/images', {params: {sources: 'INSTAGRAM', size: 20, cursor: this.next}})

            case 'recent':
              return this.$api.get('/me/images', {params: {size: 20, cursor: this.next}})
          }
        }

        request().then(({data: images, cursor}) => {
          this.images.push(...images)
          this.cursor = cursor
        })
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
  .Image {
    flex: 0 0 100%;
    max-width: 100%;

    @media (min-width: 1000px) {
      flex: 0 0 25%;
      max-width: 25%;
    }
  }
</style>
