<template>
  <div>
    <div vclass="google-map">
      <GmapMap
        :center="{lat:1.290270,lng:103.851959}"
        :zoom="16"
        map-type-id="terrain"
        style="width: 1170px; height: 223px; top: 100px;"
      >
        <GmapMarker
          :key="index"
          :position="{lat:1.290270,lng:103.851959}"
          :clickable="false"
          :draggable="false"
          @click="center=m.position"
        />
      </GmapMap>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import * as VueGoogleMaps from 'vue2-google-maps'

  export default {
    name: "GoogleMap"
  }
  Vue.use(VueGoogleMaps, {
    load: {
      key: 'AIzaSyBG09Xl-gyy4sIOLF8vPqWwBFy1Zi-nHA0',
      libraries: 'places', // This is required if you use the Autocomplete plugin
      // OR: libraries: 'places,drawing'
      // OR: libraries: 'places,drawing,visualization'
      // (as you require)

      //// If you want to set the version, you can do so:
      // v: '3.26',
    },

    //// If you intend to programmatically custom event listener code
    //// (e.g. `this.$refs.gmap.$on('zoom_changed', someFunc)`)
    //// instead of going through Vue templates (e.g. `<GmapMap @zoom_changed="someFunc">`)
    //// you might need to turn this on.
    // autobindAllEvents: false,

    //// If you want to manually install components, e.g.
    //// import {GmapMarker} from 'vue2-google-maps/src/components/marker'
    //// Vue.component('GmapMarker', GmapMarker)
    //// then disable the following:
    // installComponents: true,
  })

</script>

<style scoped lang="less">
  .google-map {
    width: 1170px;
    height: 223px;
    margin: 0 auto;
    background: gray;
  }
</style>
