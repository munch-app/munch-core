<template>
  <div v-if="images">
    <div class="container">
      <div class="ImageRow mt-24">
        <div class="ImageList flex mt-24 overflow-hidden border-3">
          <div class="Image hover-pointer" v-for="(image, index) in list"
               :key="index" @click="$emit('onClickImage', index)">
            <div class="aspect r-1-1">
              <image-sizes class="overflow-hidden border-3" :sizes="image.sizes" width="1000"/>
            </div>
          </div>
        </div>
      </div>
    </div>

    <image-sizes class="none ImageBanner" :sizes="images[0].sizes"/>

    <div class="container">
      <div class="relative lh-0">
        <div class="ShowButton flex-align-center border-3 border hover-pointer elevation-hover-2 bg-white"
             v-scroll-to="{el: '#PlaceImageWall',offset: -120}">

          <simple-svg class="wh-16px" fill="rgba(0,0,0,.75)" :filepath="require('~/assets/icon/place/grid.svg')"/>
          <div class="ml-8 text-uppercase small-bold">SHOW IMAGES</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../core/ImageSizes";

  export default {
    name: "PlaceImageBanner",
    components: {ImageSizes},
    props: {
      images: Array
    },
    mounted() {
      console.log(this.images)
    },
    computed: {
      list() {
        return this.images.slice(0, 10)
      }
    }
  }
</script>

<style scoped lang="less">
  @media (max-width: 375.98px) {
    .ImageRow {
      display: none;
    }

    .ImageBanner {
      height: 200px;
      display: block;
    }
  }

  .ImageList {
    margin: -12px;
  }

  .Image {
    padding: 12px;
  }

  @media (min-width: 376px) {
    .Image {
      flex: 0 0 50%;
    }
  }

  @media (min-width: 576px) {
    .Image {
      flex: 0 0 33.333%;
    }
  }

  @media (min-width: 768px) {
    .Image {
      flex: 0 0 25%;
    }
  }

  @media (min-width: 992px) {
    .Image {
      flex: 0 0 20%;
    }
  }

  @media (min-width: 1200px) {
    .Image {
      flex: 0 0 16.666667%;
    }
  }

  .ShowButton {
    position: absolute;
    right: 0;
    bottom: 0;

    margin: 12px;
    padding: 8px 10px;

    @media (max-width: 767.98px) {
      margin-right: 0;
      margin-bottom: 24px;
    }
  }
</style>
