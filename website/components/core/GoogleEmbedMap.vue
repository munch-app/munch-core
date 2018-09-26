<template>
  <div class="GoogleEmbedMap">
    <iframe width="100%" :height="height" frameborder="0" style="border:0" :src="url" allowfullscreen/>
  </div>
</template>

<script>
  export default {
    name: "GoogleEmbedMap",
    data() {
      return {key: 'AIzaSyDnaI9tCw54qWibZ7MMA_tYZF121Mw2h5E'};
    },
    props: {
      height: {
        type: String,
        default: () => '216'
      },
      latLng: {
        type: String,
        required: true
      },
      interaction: {
        type: Boolean,
        default: () => false
      }
    },
    computed: {
      url() {
        return `https://www.google.com/maps/embed/v1/place?q=${this.latLng}&zoom=16&key=${this.key}`
      }
    }
  }
</script>

<style scoped lang="less">
  .GoogleEmbedMap {
    position: relative;
  }
</style>
