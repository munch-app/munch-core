<!-- @deprecated -->

<template>
  <!-- Deprecated: use ImageSizes instead -->
  <div class="ImageSize" :style="style">
    <div class="ImageSlot index-0">
      <slot></slot>
    </div>
    <img class="Image index-image wh-100" v-if="size.url" :src="size.url" :alt="alt" :style="imageStyle"/>
  </div>
</template>

<script>
  export default {
    name: "ImageSize",
    props: {
      min: {
        type: Number,
        default: () => 600
      },
      max: {
        type: Number,
        default: () => 800
      },
      objectFit: {
        require: false,
        type: String,
        default: () => 'cover'
      },
      height: Number, // required for width grow
      grow: String, // 'height' or 'width'
      alt: String,
      image: Object,
    },
    computed: {
      sizes() {
        if (this.image && this.image.sizes && Array.isArray(this.image.sizes)) {
          return this.image.sizes
        }
        return []
      },
      size() {
        let selected = {width: 0}

        for (let i = 0; i < this.sizes.length; i++) {
          let size = this.sizes[i]
          if (size.hasOwnProperty('width') && size.hasOwnProperty('height') && size.hasOwnProperty('url')) {
            if (this.min <= size.width && size.width <= this.max) return size
            if (this.min <= size.height && size.height <= this.max) return size

            if (selected.width < size.width) {
              selected = size
            }
          }
        }

        return selected
      },
      style() {
        if (this.grow === 'height') {
          return {
            paddingTop: this.heightPercent,
          }
        } else if (this.grow === 'width') {
          return {
            height: `${this.height}px`,
            width: `${this.height * this.widthPercent}px`
          }
        }
      },
      imageStyle() {
        return {objectFit: this.objectFit}
      },
      heightPercent() {
        if (this.size.url) {
          const width = parseFloat(this.size.width)
          const height = parseFloat(this.size.height)
          return `${height / width * 100}%`
        }
      },
      widthPercent() {
        if (this.size.url) {
          const width = parseFloat(this.size.width)
          const height = parseFloat(this.size.height)
          return width / height
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageSize {
    background-color: transparent;
    overflow: hidden;

    width: 100%;
    position: relative;

    .ImageSlot, .Image {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }
</style>
