<template>
  <div>
    <div ref="annotation">
      <nuxt-link class="PlaceAnnotation hover-pointer" :to="`/places/${place.placeId}`">
        <div class="AnnotationDialog elevation-1 flex-align-center flex-justify-between bg-white">
          <div>
            <div class="Name text text-ellipsis-1l">{{name}}</div>
            <div class="Location text">{{location}}</div>
          </div>
          <div>
            <simple-svg class="Regular wh-20px ml-8" :filepath="require('~/assets/icon/Map_Next.svg')"/>
            <simple-svg class="Small wh-16px ml-4" :filepath="require('~/assets/icon/Map_Next.svg')"/>
          </div>
        </div>
      </nuxt-link>

      <div class="AnnotationMarker flex-center">
        <simple-svg class="Regular wh-32px" :filepath="require('~/assets/icon/Map_Place_Marker.svg')" fill="#0A6284"/>
        <simple-svg class="Small wh-20px" :filepath="require('~/assets/icon/Map_Place_Marker.svg')" fill="#0A6284"/>
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from '../utils/map/AppleMap'

  export default {
    name: "AppleMapPlaceMarkerAnnotation",
    props: {
      place: {
        type: Object,
        required: true,
      },
    },
    computed: {
      name() {
        return this.place.name
      },
      location() {
        return this.place.location.neighbourhood || this.place.location.street
      },
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.place.location.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      }, {data: this.place})

      this.$parent.onMapLoaded((map) => {
        map.addAnnotation(this.$annotation)
      })
    },
    beforeDestroy() {
      const map = this.$parent.$map
      if (map && this.$annotation) {
        map.removeAnnotation(this.$annotation)
      }
    }
  }
</script>

<style scoped lang="less">
  .AnnotationDialog {
    border-radius: 5px;
    position: absolute;

    width: 188px;
    bottom: 38px;

    padding: 10px 4px 10px 14px;
  }

  .AnnotationMarker {
    width: 188px;
    position: absolute;
    bottom: 0;
  }

  .Name {
    font-size: 16px;
    font-weight: 600;
  }

  .Location {
    font-size: 13px;
  }

  .Small {
    display: none;
  }

  /*@media (max-width: 991.98px) {*/
    /*.Regular {*/
      /*display: none;*/
    /*}*/
    /*.Small {*/
      /*display: block;*/
    /*}*/

    /*.AnnotationDialog {*/
      /*border-radius: 4px;*/

      /*width: 136px;*/
      /*bottom: 22px;*/

      /*padding: 5px 4px 5px 8px;*/
    /*}*/

    /*.AnnotationMarker {*/
      /*width: 136px;*/
    /*}*/

    /*.Name {*/
      /*font-size: 13px;*/
    /*}*/

    /*.Location {*/
      /*font-size: 11px;*/
    /*}*/
  /*}*/
</style>
