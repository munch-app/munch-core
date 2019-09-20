<!--
  Image served from cdn.munch.app
-->
<template>
  <div class="relative">
    <img v-if="url" :src="url" :alt="alt" :style="{objectFit, width, height}"/>
    <div class="slot absolute-0">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: "CdnImg",
    props: {
      alt: String,
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
      objectFit: {
        type: String,
        default: 'cover'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '100%'
      }
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
  div.relative {
    height: 100%;
    width: 100%;
  }

  .slot > * {
    height: 100%;
    width: 100%;
  }
</style>
