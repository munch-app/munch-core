<!--
  Image served from cdn.munch.app

  For CdnImg.vue,
  Image controls the size of the container.
-->
<template>
  <div class="relative wh-100 lh-0">
    <img v-if="url" :src="url" :alt="alt" :style="{objectFit, width, height}"/>
    <div class="slot absolute-0 wh-100">
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
      /**
       * cover
       * contain
       * fill
       * none
       * scale-down
       */
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
  .slot > * {
    height: 100%;
    width: 100%;
  }
</style>
