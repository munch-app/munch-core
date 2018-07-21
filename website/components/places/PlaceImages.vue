<template>
  <div class="PlaceImages">
    <image-size class="Elevation2" :image="current" background>
      <div class="Container">
        <div v-if="source" class="Small SourceButton Border24 Elevation1">{{source}}</div>
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
    .Container {
      width: 100%;
      padding-top: 62.5%;

      display: flex;
      justify-content: flex-end;
      align-items: flex-end;
    }

    .SourceButton {
      background-color: rgba(255, 255, 255, 0.75);
      padding: 6px 10px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
