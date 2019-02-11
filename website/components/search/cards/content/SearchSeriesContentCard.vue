<template>
  <div>
    <div class="aspect r-5-3 border-3 overflow-hidden">
      <image-sizes v-if="imageSizes" :sizes="imageSizes" :alt="content.subtitle"/>
      <div v-else class="bg-s500 flex-end wh-100"/>
      <div class="absolute-0 flex-center p-24 ImageOverlay">
        <h2 class="white text-ellipsis-2l">{{content.title}}</h2>
      </div>
    </div>

    <h5 class="text-ellipsis-1l mt-8 s700">{{content.subtitle}}</h5>
    <p class="text-ellipsis-3l">{{content.body}}</p>
  </div>
</template>

<script>
  import ImageSizes from "../../../core/ImageSizes";

  export default {
    name: "SearchSeriesContentCard",
    components: {ImageSizes},
    props: {
      content: {
        type: Object,
        required: true
      },
      options: {
        type: Object,
      }
    },
    computed: {
      imageSizes() {
        return this.content && this.content.image && this.content.image.sizes
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageOverlay {
    background: rgba(0, 0, 0, 0.4);
  }
</style>
