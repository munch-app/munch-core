<template>
  <div class="Changes" v-if="hasChanges">
    <h2>Edits</h2>
    <div class="List">
      <div v-for="(value, key) in payload.removes.images" :key="key" class="flex-align-center">
        <div class="wh-32px">
          <div class="aspect r-1-1">
            <image-sizes class="overflow-hidden border-3" :sizes="value.image.sizes" width="1" height="1"/>
          </div>
        </div>
        <div class="ml-16">
          <h5>Image marked as <span class="error">{{value.flag}}</span></h5>
        </div>
      </div>

      <div v-for="(value, key) in payload.removes.articles" :key="key" class="flex-align-center">
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
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";
  export default {
    name: "PlaceSuggestChanges",
    components: {ImageSizes},
    props: {
      payload: {
        type: Object
      }
    },
    data() {
      return {hasChanges: false}
    },
    watch: {
      'payload.removes': {
        handler(val){
          const {images, articles} = val
          this.hasChanges = Object.keys(images).length > 0 || Object.keys(articles).length > 0
        },
        deep: true
      },
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
