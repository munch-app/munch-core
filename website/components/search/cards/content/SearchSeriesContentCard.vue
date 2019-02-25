<template>
  <div class="hover-pointer">
    <div class="aspect r-5-3 border-3 overflow-hidden">
      <image-sizes v-if="imageSizes" :sizes="imageSizes" :alt="content.subtitle"/>
      <div v-else class="bg-s500 flex-end wh-100"/>
      <div class="absolute-0 flex-center p-24 ImageOverlay">
        <h2 class="white text-ellipsis-2l text-center">{{content.title}}</h2>
      </div>
    </div>

    <div class="Content">
      <h5 class="text-ellipsis-1l s700">{{content.subtitle}}</h5>
      <p class="mt-4 text-ellipsis-3l">{{content.body}}</p>
    </div>
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
      },
      imageCredit() {
        const image = this.content && this.content.image
        return null
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageOverlay {
    background: rgba(0, 0, 0, 0.45);
  }

  .Content {
    margin-top: 12px;
  }

  p {
    font-size: 14px;
  }
</style>
