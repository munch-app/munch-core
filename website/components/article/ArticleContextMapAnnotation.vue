<template>
  <div>
    <div ref="annotation">
      <nuxt-link class="PlaceAnnotation hover-pointer" :to="item.path">
        <div class="Dialog border-5 absolute elevation-1 flex-align-center flex-justify-between bg-white p-6-12">
          <div>
            <h6 class="text-ellipsis-1l">{{item.name}}</h6>
            <div class="tiny text-ellipsis-2l">{{item.address}}</div>
          </div>
          <div>
            <simple-svg class="wh-16px ml-8" fill="#000" :filepath="require('~/assets/icon/icons8-chevron-right.svg')"/>
          </div>
        </div>
      </nuxt-link>

      <div class="Marker absolute flex-center">
        <simple-svg class="wh-32px" fill="#F07" :filepath="require('~/assets/icon/icons8-map-pin.svg')" />
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from '../utils/map/AppleMap'

  export default {
    name: "ArticleContextMapAnnotation",
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.item.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      }, {data: this.item})

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
  .Dialog {
    width: 188px;
    bottom: 38px;
  }

  .Marker {
    width: 188px;
    bottom: 0;
  }
</style>
