<template>
  <div>
    <div v-if="background" :style="backgroundStyle">
      <slot></slot>
    </div>
    <b-img v-else :style="imageStyle" center fluid-grow :src="url" :alt="alt"/>
  </div>
</template>

<script>
  export default {
    name: "ImageSize",
    props: {
      background: Boolean,
      min: Number,
      max: Number,
      height: Number,
      alt: String,
      image: Object,
      sizeUrls: Object

    },
    computed: {
      sizes() {
        if (this.image && this.image) {
          return this.image.sizes
        } else if (this.sizeUrls) {
          let sizes = []
          for (const size in this.sizeUrls) {
            if (this.sizeUrls.hasOwnProperty(size)) {
              let wh = size.split('x')
              sizes.push({width: parseInt(wh[0]), height: parseInt(wh[1]), url: this.sizeUrls[size]})
            }
          }
          return sizes
        } else {
          return []
        }
      },

      url() {
        const min = parseInt(this.min) || 600
        const max = parseInt(this.max) || 800
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
      },
      imageStyle() {
        return {
          'object-fit': 'cover',
          'height': this.height + 'px'
        }
      },
      backgroundStyle() {
        return {
          'background': 'url("' + this.url + '") no-repeat center center',
          'background-size': 'cover'
        }
      }
    }
  }
</script>

<style scoped lang="less">
</style>
