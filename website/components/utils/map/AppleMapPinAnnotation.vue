<template>
  <div>
    <div ref="annotation">
      <div class="border-circle wh-32px flex-center bg-pink">
        <simple-svg class="wh-24px" fill="#FFF" :filepath="require('~/assets/icon/icons8-map-pin.svg')"/>
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from './AppleMap'

  export default {
    name: "AppleMapPinAnnotation",
    props: {
      latLng: {
        type: String,
        required: true
      }
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      })

      this.$parent.onMapLoaded((map) => {
        map.addAnnotation(this.$annotation)
      })
    },
    beforeDestroy() {
      const map = this.$parent.$map
      if (map && this.$annotation) {
        map.removeAnnotation(this.$annotation)
      }
    },
    watch: {
      latLng(val, oldVal) {
        this.$annotation.coordinate = AppleMap.Coordinate(val)
      }
    }
  }
</script>
