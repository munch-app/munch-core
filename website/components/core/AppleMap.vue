<template>
  <div>
    <div ref="map" class="AppleMap"/>
  </div>
</template>

<script>
  export default {
    name: "AppleMap",
    props: {
      annotations: {
        type: Array,
        required: false
      }
    },
    mounted() {
      const map = new mapkit.Map(this.$refs.map, {
        showsUserLocation: true,
        showsUserLocationControl: false,
        showsCompass: false,
        showsScale: false,
        showsZoomControl: false,
        isRotationEnabled: false,
        showsMapTypeControl: false,
        isZoomEnabled: false
      });


      if (this.annotations) {
        const calloutDelegate = {
          calloutElementForAnnotation: function(annotation) {
            const div = document.createElement("div");
            div.className = "CalloutNamed elevation-1"
            div.textContent = annotation.data.name

            return div;
          }
        };

        const list = this.annotations.map(annotation => {
          const coordinate = new mapkit.Coordinate(annotation.lat, annotation.lng)

          return new mapkit.MarkerAnnotation(coordinate, {
            color: "#F05F3B",
            callout: calloutDelegate,
            data: {
              name: annotation.name
            }
          });
        })
        map.showItems(list, {
          animate: true,
          padding: new mapkit.Padding(64, 24, 64, 24)
        })
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
