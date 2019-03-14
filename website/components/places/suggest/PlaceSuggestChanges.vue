<template>
  <div>
    <div class="Changes" v-if="hasRemoveChanges">
      <h2>Suggest Removal</h2>
      <div class="List">
        <div v-for="(value, key) in payload.removes.images" :key="key" class="flex-align-center hover-pointer"
             @click="onRemove('images', key)">
          <div class="wh-32px">
            <div class="aspect r-1-1">
              <image-sizes class="overflow-hidden border-3" :sizes="value.image.sizes" width="1" height="1"/>
            </div>
          </div>
          <div class="ml-16">
            <h5>Image marked as <span class="error">{{value.flag}}</span></h5>
          </div>
        </div>

        <div v-for="(value, key) in payload.removes.articles" :key="key" class="flex-align-center hover-pointer"
             @click="onRemove('images', key)">
          <div class="wh-32px">
            <div class="aspect r-1-1 overflow-hidden border-3">
              <image-sizes v-if="value.article.thumbnail" :sizes="value.article.thumbnail.sizes" width="1" height="1"/>
              <div v-else class="bg-whisper200 wh-100"></div>
            </div>
          </div>
          <div class="ml-16">
            <h5>Article marked as <span class="error">{{value.flag}}</span></h5>
          </div>
        </div>
      </div>
    </div>

    <div class="Changes" v-if="hasUploadsChanges">
      <h2 class="mb-24">Image Upload</h2>
      <div class="flex-wrap">
        <div v-for="(value, key) in payload.uploads.images" :key="key" class="hover-pointer"
             @click="onRemoveUpload(key)">
          <div style="width: 150px" class="">
            <div class="aspect r-1-1 overflow-hidden border-3">
              <img :src="value.dataURL" alt="" style='height: 100%; width: 100%; object-fit: contain' class="p-8">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";
  import Vue from 'vue'

  export default {
    name: "PlaceSuggestChanges",
    components: {ImageSizes},
    props: {
      payload: {
        type: Object
      }
    },
    data() {
      return {hasRemoveChanges: false, hasUploadsChanges: false,}
    },
    watch: {
      'payload.removes': {
        handler(val) {
          const {images, articles} = val
          this.hasRemoveChanges = Object.keys(images).length > 0 || Object.keys(articles).length > 0
        },
        deep: true
      },
      'payload.uploads': {
        handler(val) {
          const {images} = val
          this.hasUploadsChanges = Object.keys(images).length > 0
        },
        deep: true
      },
    },
    methods: {
      onRemove(type, key) {
        Vue.delete(this.payload.removes[type], key)
      },
      onRemoveUpload(key) {
        Vue.delete(this.payload.uploads.images, key)
      }
    }
  }
</script>

<style scoped lang="less">
  .Changes {
    margin-top: 48px;

    .List {
      margin-top: 16px;
      margin-bottom: 16px;

      > div {
        padding-top: 8px;
        padding-bottom: 8px;
      }
    }
  }
</style>
