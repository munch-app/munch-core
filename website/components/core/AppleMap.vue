<template>
  <div>
    <div ref="map" class="AppleMap"/>
  </div>
</template>

<script>
  let map;
  export default {
    name: "AppleMap",
    props: {
      annotations: {
        type: Array,
        required: false
      }
    },
    mounted() {
      map = new mapkit.Map(this.$refs.map, {
        showsUserLocation: false,
        showsUserLocationControl: false,
        showsCompass: false,
        showsScale: false,
        showsZoomControl: false,
        isRotationEnabled: false,
        showsMapTypeControl: false,
        isZoomEnabled: false
      });

      map.region = new mapkit.CoordinateRegion(
        new mapkit.Coordinate(1.38, 103.8),
        new mapkit.CoordinateSpan(0.167647972, 0.354985255)
      )

      if (this.annotations) {
        this.updateAnnotations(this.annotations)
      }
    },
    watch: {
      annotations(annotations) {
        this.updateAnnotations(annotations)
      }
    },
    methods: {
      updateAnnotations(annotations) {
        if (annotations && map) {
          const calloutDelegate = {
            calloutElementForAnnotation: function (annotation) {
              const div = document.createElement("div");
              div.className = "CalloutNamed elevation-1"
              div.textContent = annotation.data.name

              return div;
            }
          };

          const list = annotations.map(annotation => {
            const coordinate = new mapkit.Coordinate(annotation.lat, annotation.lng)

            return new mapkit.MarkerAnnotation(coordinate, {
              color: "#F05F3B",
              callout: calloutDelegate,
              data: {
                name: annotation.name
              }
            });
          })
          console.log('Rendered: Annotations')
          map.removeAnnotations(map.annotations)
          map.showItems(list, {
            animate: true,
            padding: new mapkit.Padding(64, 64, 64, 64)
          })
        }
      }
    }
  }
</script>

<style lang="less">
  .AppleMap {
    width: 100%;
    height: 100%;

    .CalloutNamed {
      background: white;
      padding: 8px;
      border-radius: 3px;

      font-size: 13px;
      line-height: 13px;

      margin-bottom: 4px;
    }
  }
</style>
