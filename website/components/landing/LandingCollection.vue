<template>
  <horizontal-scroll-view class="LandingCollection" :items="collections" :map-key="i => i.collectionId">
    <template slot-scope="{item}">
      <nuxt-link class="initial hover-pointer" :to="`/collections/${item.collectionId}`">
        <image-size class="Image" :image="item.image">
          <div class="flex-center ImageContainer">
            <h3 class="white text-center">{{item.name}}</h3>
          </div>
        </image-size>
      </nuxt-link>
    </template>
  </horizontal-scroll-view>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "LandingCollection",
    components: {HorizontalScrollView, ImageSize},
    props: {
      collections: {
        type: Array,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  @media (max-width: 575.98px) {
    @length: 160px;

    .LandingCollection {
      height: @length;
    }

    .ImageContainer {
      width: @length;
      height: @length;
      padding: 16px;
    }
  }

  @media (min-width: 576px) {
    @length: 200px;

    .LandingCollection {
      height: @length;
    }

    .ImageContainer {
      width: @length;
      height: @length;
      padding: 24px;
    }
  }

  .Image {
    border-radius: 4px;
    overflow: hidden;
  }

  .ImageContainer {
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    background-color: rgba(0, 0, 0, 0.55);

    &:hover {
      background-color: rgba(0, 0, 0, 0.33);
    }
  }
</style>
