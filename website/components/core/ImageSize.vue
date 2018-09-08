<template>
  <div class="ImageSize">
    <slot class="ImageBox"></slot>
    <b-img class="Image" center fluid-grow :src="url" :alt="alt"/>
  </div>
</template>

<script>
  export default {
    name: "ImageSize",
    props: {
      min: {
        require: false,
        type: Number,
        default: () => 600
      },
      max: {
        require: false,
        type: Number,
        default: () => 800
      },
      alt: String,
      image: Object
    },
    computed: {
      sizes() {
        if (this.image && this.image.sizes && Array.isArray(this.image.sizes)) {
          return this.image.sizes
        }

        if (this.image) {
          let sizes = []
          for (const size in this.image) {
            if (this.image.hasOwnProperty(size)) {
              let wh = size.split('x')
              sizes.push({width: parseInt(wh[0]), height: parseInt(wh[1]), url: this.image[size]})
            }
          }
          return sizes
        }

        return []
      },

      url() {
        const min = this.min
        const max = this.max
        const sizes = this.sizes

        let maxUrl = ''
        let maxWidth = 0

        for (let i = 0; i < sizes.length; i++) {
          let size = sizes[i]
          if (size.hasOwnProperty('width') && size.hasOwnProperty('height') && size.hasOwnProperty('url')) {
            if (min <= size.width && size.width <= max) return size.url
            if (min <= size.height && size.height <= max) return size.url

            if (maxWidth < size.width) {
              maxWidth = size.width
              maxUrl = size.url
            }
          }
        }

        return maxUrl
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageSize {
    position: relative;
    background-color: transparent;
    overflow: hidden;

    .ImageBox {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      z-index: 0;
    }

    .Image {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      z-index: -1;

      height: 100%;
      object-fit: cover;
    }
  }
</style>
