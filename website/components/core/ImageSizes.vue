<!-- @deprecated -->

<!--
This is a rewrite of ImageSize with emphasis on
- More fluid rendering by width or height
- More lightweight component that focus on rendering sizes from munch-file.Image
- Cleaner approach for slotting of content into the image
-->
<template>
  <div class="relative">
    <img v-if="url" :src="url" :alt="alt" :style="style.image"/>
    <div class="ImageSlot absolute-0">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  const findUrl = (sizes, minWidth, minHeight) => {
    // Better Validate This Method
    const selected = {width: 0, height: 0, url: null}

    sizes = _.sortBy(sizes, (s) => s.width)
    sizes.forEach(size => {
      // Found width & height
      if (selected.width && selected.height) return

      if (minWidth <= size.width) {
        selected.width = size.width
        selected.url = size.url
      }
      if (minHeight <= size.height) {
        selected.height = size.height
        selected.url = size.url
      }
    })

    return selected.url || sizes && sizes[sizes.length - 1].url
  }

  export default {
    $$findUrl: findUrl,
    name: "ImageSizes",
    props: {
      alt: String,
      sizes: {
        type: Array,
        required: true
      },

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
        return findUrl(this.sizes, width, height)
      },
      style() {
        const image = {
          objectFit: this.objectFit,

          width: this.maxWidth ? 'auto' : '100%',
          maxWidth: this.maxWidth || 'initial',

          height: this.maxHeight ? 'auto' : '100%',
          maxHeight: this.maxHeight || 'initial',
        }

        return {image}
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageSlot > * {
    height: 100%;
    width: 100%;
  }
</style>
