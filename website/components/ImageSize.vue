<template>
  <div>
    <img :src="url">
  </div>
</template>

<script>
  export default {
    name: "ImageSize",
    props: ['min', 'max', 'image'],
    computed: {
      url() {
        const min = parseInt(this.min) || 600
        const max = parseInt(this.max) || 800

        if (!this.image) {
          return ''
        }

        let maxUrl = ''
        let maxWidth = 0

        for (let size in this.image.sizes) {
          if (this.image.sizes.hasOwnProperty(size) && size.hasOwnProperty('width') && size.hasOwnProperty('height') && size.hasOwnProperty('url')) {
            if (min <= size.width <= max) return size.url
            if (min <= size.height <= max) return size.url

            if (maxWidth > size.width) {
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
  img {

  }
</style>
