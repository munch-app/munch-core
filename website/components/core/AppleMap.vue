<template>
  <div>
    <div ref="map" class="AppleMap"/>
    <div style="display: none">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  import AppleMapPlaceAnnotation from "./AppleMapPlaceAnnotation";

  function Coordinate(latLng) {
    const ll = latLng.split(",")
    const lat = parseFloat(ll[0])
    const lng = parseFloat(ll[1])
    return new mapkit.Coordinate(lat, lng)
  }

  function CoordinateRegion(region) {
    const ll = region.delta.split(",")
    const lat = parseFloat(ll[0])
    const lng = parseFloat(ll[1])

    return new mapkit.CoordinateRegion(
      Coordinate(region.latLng),
      new mapkit.CoordinateSpan(lat, lng)
    )
  }

  export default {
    Coordinate,
    name: "AppleMap",
    components: {AppleMapPlaceAnnotation},
    props: {
      region: {
        type: Object,
        default: () => ({latLng: '1.38,103.8', delta: '0.167647972,0.354985255'})
      },
      options: {
        type: Object,
        default: () => ({})
      }
    },
    mounted() {
      this.$map = new mapkit.Map(this.$refs.map, {
        showsUserLocation: this.options.showsUserLocation || false,
        showsUserLocationControl: this.options.showsUserLocationControl || true,

        showsCompass: this.options.showsCompass || mapkit.FeatureVisibility.Hidden,
        showsScale: this.options.showsScale || mapkit.FeatureVisibility.Adaptive,
        showsZoomControl: this.options.showsZoomControl || true,

        isZoomEnabled: this.options.isZoomEnabled || true,
        isScrollEnabled: this.options.isScrollEnabled || true,
        isRotationEnabled: this.options.isRotationEnabled || false,

        mapType: this.options.mapType || mapkit.Map.MapTypes.Standard,
        showsMapTypeControl: this.options.showsMapTypeControl || false,
      })
      this.$map.region = CoordinateRegion(this.region)
    },
    methods: {
      centerAnnotations() {
        this.$map.showItems(this.$map.annotations, {
          animate: true,
          padding: new mapkit.Padding(64, 64, 64, 64)
        })
      }
    }
  }
</script>

<style lang="less">
  .AppleMap {
    width: 100%;
    height: 100%;
  }
</style>
