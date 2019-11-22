<!--
  Image served from cdn.munch.app

  For CdnBgImage.vue,
  Content control the size of the image.
-->
<template>
  <div :style="{backgroundImage: `url('${url}')`}">
    <slot></slot>
  </div>
</template>

<script>
  export default {
    name: "CdnBgImg",
    props: {
      image: {
        type: Object,
        default: undefined
      },
      /**
       * 320x320
       * 640x640
       * 1080x1080
       */
      type: {
        type: String,
        default: '640x640'
      },
    },
    computed: {
      url() {
        const sizes = this.image?.sizes
        if (!sizes) {
          return null
        }

        const url = sizes[this.type]
        if (url) {
          return url
        }

        if (sizes.size === 0) {
          return null
        }

        return sizes.values().next().value
      }
    }
  }
</script>

<style scoped lang="less">
  div {
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
  }
</style>
