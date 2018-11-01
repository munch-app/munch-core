<!--
This is a rewrite of ImageSize with emphasis on
- More fluid rendering by width or height

Personally I think there will still a need to use ImageSize for slotting of content in the middle
-->
<template>
  <div class="ImageSizes relative">
    <img v-if="url" :src="url" :alt="alt" :style="style.image"/>
    <div class="ImageSlot absolute-tblr">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'

  export default {
    name: "ImageSizes",
    props: {
      alt: String,
      sizes: {
        type: Array,
        required: true
      },

      fixed: Boolean, // Not yet implemented

      /**
       * Max width, height enabled width, height grow
       */
      maxWidth: String,
      maxHeight: String,

      /**
       * Providing weight & height allows the Component to find the best image size
       */
      width: {
        type: [Number, String],
        default: 800
      },
      height: {
        type: [Number, String],
        default: 600
      },

      objectFit: {
        type: String,
        default: 'cover'
      },
    },
    computed: {
      url() {
        const width = parseInt(this.width, 10)
        const height = parseInt(this.height, 10)
        const selected = {width: 0, height: 0, url: null}

        const sizes = _.sortBy(this.sizes, (s) => s.width)
        sizes.forEach(size => {
          // Found width & height
          if (selected.width && selected.height) return

          if (width <= size.width) {
            selected.width = size.width
            selected.url = size.url
          }
          if (height <= size.height) {
            selected.height = size.height
            selected.url = size.url
          }
        })

        return selected.url || sizes && sizes[sizes.length - 1].url
      },
      style() {
        const image = {
          objectFit: this.objectFit,

          width: this.maxWidth ? 'auto': '100%',
          maxWidth: this.maxWidth,

          height: this.maxHeight ? 'auto': '100%',
          maxHeight: this.maxHeight,
        }

        return {image}
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageSlot {
    > * {
      height: 100%;
      width: 100%;
    }
  }
</style>
