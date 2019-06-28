<template>
  <div class="bg-whisper050 p-8-16 border-4 flex-align-center">
    <div class="Image overflow-hidden">
      <image-sizes width="200" height="200" v-if="images && images.length > 0" :sizes="images[0].sizes"></image-sizes>
      <div v-else class="bg-white wh-100"></div>
    </div>

    <div class="flex-column ml-16 zero">
      <div class="text text-ellipsis-1l">{{line1}}</div>
      <div class="h5 text text-ellipsis-1l">{{line2}}</div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../core/ImageSizes";

  export default {
    name: "ContentAvatar",
    components: {ImageSizes},
    props: {
      images: {
        required: true,
      },
      line1: {
        required: true,
      },
      line2: {
        required: true,
      }
    },
  }
</script>

<style scoped lang="less">
  .Image {
    width: 64px;
    height: 64px;

    border-radius: 50%;
  }
</style>
