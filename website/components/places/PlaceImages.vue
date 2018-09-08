<template>
  <div class="PlaceImages">
    <image-size class="Image Elevation2 Border3 BorderImage" :image="current">
      <div class="ImageBox">
        <div v-if="source" class="Small UsernameButton Border24 Elevation1">{{source}}</div>
      </div>
    </image-size>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "PlaceImages",
    props: {
      images: Array
    },
    components: {ImageSize},
    data() {
      return {
        index: 0
      }
    },
    computed: {
      current() {
        return this.images[this.index] || []
      },
      source() {
        let current = this.current
        if (current && current.profile) {
          return current.profile.name
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceImages {
    width: 100%;
    padding-top: 62.5%;
    position: relative;

    .Image {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }

    .ImageBox {
      display: flex;
      justify-content: flex-end;
      align-items: flex-end;
    }

    .UsernameButton {
      background-color: rgba(255, 255, 255, 0.75);
      padding: 6px 10px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
