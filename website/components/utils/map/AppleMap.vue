<template>
  <div>
    <div ref="map" class="AppleMap"/>
    <div style="display: none">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  import AppleMapPlaceAnnotation from "../../core/AppleMapPlaceAnnotation";

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

  let onMapReadyQueue = []

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
      if (!mapkit.$isInited) {
        mapkit.init({
          authorizationCallback: done => {
            this.$axios.$get('/apple-maps/token')
              .then(done)
          },
          language: "en"
        })
        mapkit.$isInited = true
      }

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
      onMapReadyQueue.forEach((func) => {
        func(this.$map)
      })
      onMapReadyQueue.splice(0)
    },
    methods: {
      centerAnnotations(options) {
        // Change priority for existing
        this.$map?.annotations?.forEach(annotation => {
          annotation.displayPriority = 500
        })

        const annotations = options?.annotations || this.$map.annotations

        annotations.forEach(annotation => {
          annotation.displayPriority = 1000
        })

        this.$map.showItems(annotations, {
          animate: true,
          padding: new mapkit.Padding(64, 64, 64, 64),
          minimumSpan: options?.minimumSpan || new mapkit.CoordinateSpan(0.015, 0.025)
        })
      },
      onMapLoaded(func) {
        if (this.$map) {
          func(this.$map)
        } else {
          onMapReadyQueue.push(func)
        }
      },
      getAnnotations(filter) {
        if (filter) {
          return this.$map.annotations.filter(annotation => filter(annotation.data))
        }
        return this.$map.annotations
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
