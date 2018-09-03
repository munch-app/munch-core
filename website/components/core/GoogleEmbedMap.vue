<template>
  <iframe width="100%" :height="height" frameborder="0" style="border:0"
          :src="`https://www.google.com/maps/embed/v1/place?q=${latLng}&key=${key}`"
          />
</template>

<!--http://maps.googleapis.com/maps/api/staticmap?center=52.569916,13.408571&zoom=8&format=png&sensor=false&size=300x200&maptype=roadmap-->

<!--
mapTypeControl: false,
          gestureHandling: 'none',
  keyboardShortcuts: true,
  zoomControl: false,
  streetViewControl: false,
  fullscreenControl: false
-->

<script>
  export default {
    name: "GoogleEmbedMap",
    data() {
      return {key: 'AIzaSyDnaI9tCw54qWibZ7MMA_tYZF121Mw2h5E'};
    },
    props: {
      height: {
        type: String,
        default: () => '224'
      },
      latLng: {
        type: String,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">

</style>
