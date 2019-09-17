<template>
  <div class="border-2 overflow-hidden">
    <div class="relative">
      <div class="absolute position-r-0 mr-8 h-100 flex-center">
        <div @click="copyMapCenter" v-if="editing !== mapCenter"
             class="bg-blue border-2 white small-bold p-4-8 hover-pointer">
          Copy Map Center
        </div>
      </div>

      <input v-model="editing">
    </div>
    <div class="w-100 Map relative">
      <apple-map ref="map" class="wh-100" @on-change="onMapChange" :options="map.options">
        <apple-map-pin-annotation :lat-lng="editing"/>
      </apple-map>
    </div>
  </div>
</template>

<script>
  import AppleMap from "../../utils/map/AppleMap";
  import AppleMapPinAnnotation from "../../utils/map/AppleMapPinAnnotation";

  export default {
    name: "PlaceEditorLatLng",
    components: {AppleMapPinAnnotation, AppleMap},
    props: {
      value: String
    },
    data() {
      return {
        editing: this.value,
        mapCenter: this.value,
        map: {
          options: {
            showsZoomControl: true,
          }
        }
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.$refs.map.centerAnnotations({
          minimumSpan: new mapkit.CoordinateSpan(0.00005, 0.00005)
        })
      })
    },
    methods: {
      onMapChange(map) {
        const coordinate = map.center
        this.mapCenter = `${coordinate.latitude.toFixed(5)},${coordinate.longitude.toFixed(5)}`
      },
      copyMapCenter() {
        this.editing = this.mapCenter
      }
    }
  }
</script>

<style scoped lang="less">
  input, .TextAuto {
    outline: none;
    border: none;

    background: #FAFAFA;
    color: black;

    width: 100%;
    font-size: 17px;
    padding: 12px;

    &:focus {
      background: #F0F0F0;
    }
  }

  .Map {
    height: 160px;
  }
</style>
