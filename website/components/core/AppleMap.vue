<template>
  <div>
    <script src="https://cdn.apple-mapkit.com/mk/5.x.x/mapkit.js"></script>
    <div id="map"/>
  </div>
</template>

<script>
  export default {
    name: "AppleMap",
    mounted() {
      mapkit.init({
        authorizationCallback: done => {
          this.$axios.$get('/apple-maps/token')
            .then(done)
        },
        language: "en"
      });

      const lat = 1.38;
      const lng = 103.8;

      const region = new mapkit.CoordinateRegion(
        new mapkit.Coordinate(lat, lng),
        new mapkit.CoordinateSpan(3, 3)
      );

      const map = new mapkit.Map("map", {
        center: new mapkit.Coordinate(lat, lng),
        region: region,
        showsUserLocation: true,
        showsUserLocationControl: false,
        showsCompass: false,
        showsScale: false,
        showsZoomControl: false,
        isRotationEnabled: false,
        showsMapTypeControl: false,
        isZoomEnabled: false
      });
    }
  }
</script>

<style scoped lang="less">
  #map {
    width: 100%;
    height: 100%;
  }
</style>
