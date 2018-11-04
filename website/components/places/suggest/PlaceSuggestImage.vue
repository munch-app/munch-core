<template>
  <div>
    <p>Own an Instagram account, want it published on munch? <a class="text-underline s700"
                                                                href="https://partner.munch.app"
                                                                target="_blank">partner.munch.app</a></p>

    <div class="Existing" v-if="data.images.length > 0">
      <h2>Existing Images</h2>

      <div class="List flex-wrap">
        <div class="Image" v-for="image in data.images" :key="image.imageId">
          <div class="aspect r-1-1">
            <image-sizes class="overflow-hidden border-3" :sizes="image.sizes" width="1" height="1"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "PlaceSuggestImage",
    components: {ImageSizes},
    props: {
      data: Object
    }
  }
</script>

<style scoped lang="less">
  .Existing {
    margin-top: 32px;

    .List {
      margin: 8px -8px;
    }
  }

  .Image {
    padding: 8px;

    flex: 0 0 25%;
    width: 25%;
  }
</style>
