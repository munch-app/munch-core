<template>
  <vue-static-map :google-api-key="googleApiKey" format="png"
                  :markers="markers" :zoom="17" :center="latLng"
                  :size="[930, height]"/>
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
  import VueStaticMap from 'vue-static-map'

  export default {
    name: "GoogleStaticMap",
    components: {VueStaticMap},
    data() {
      return {googleApiKey: 'AIzaSyDnaI9tCw54qWibZ7MMA_tYZF121Mw2h5E'};
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
    },
    computed: {
      markers() {
        let latLng = this.latLng.split(',')
        return [
          {
            lat: parseFloat(latLng[0]), lng: parseFloat(latLng[1])
          }
        ]
      }
    }
  }
</script>

<style scoped lang="less">

</style>
