<template>
  <a v-if="images.length > 0" v-scroll-to="{el: '#PlaceImageWall',offset: -120}" class="hover-pointer">
    <div class="container">
      <div class="ImagePipe border-3">
        <image-size class="Image border-3" v-for="image in images" :image="image" :key="image.imageId" grow="width" :height="160"/>
      </div>
    </div>

    <div class="ImageBanner">
      <image-size class="Image" :image="images[0]"/>
    </div>

    <div class="container">
      <div class="Navigation">
        <div class="ShowButton elevation-1 border-3 border hover-pointer elevation-hover-2 white-bg">
          <simple-svg class="Icon" fill="rgba(0, 0, 0, 0.75)" filepath="/img/places/grid.svg"/>
          <div class="Label text-uppercase">Show Images</div>
        </div>
      </div>
    </div>
  </a>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  export default {
    name: "PlaceImageBanner",
    components: {ImageSize},
    props: {
      images: {
        type: Array,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ImagePipe {
    margin-top: 24px;
    overflow-y: hidden;
    overflow-x: hidden;

    display: flex;
    /*align-items: flex-start;*/

    .Image {
      flex-shrink: 0;
      margin-right: 20px;
    }

    @media(max-width: 767.98px) {
      display: none;
    }
  }

  .ImageBanner {
    .Image {
      height: 180px;
    }

    @media(min-width: 768px) {
      display: none;
    }
  }

  .Navigation {
    position: relative;

    .ShowButton {
      position: absolute;
      left: 0;
      bottom: 0;

      margin: 8px;
      padding: 6px 10px;

      display: inline-flex;
      align-items: center;
      line-height: 0;

      .Icon {
        width: 14px;
        height: 14px;
      }

      .Label {
        margin-left: 8px;
        margin-right: 2px;

        height: 16px;
        line-height: 16px;
        font-weight: 600;
        font-size: 13px;

        color: rgba(0, 0, 0, 0.75);
      }
    }
  }
</style>
