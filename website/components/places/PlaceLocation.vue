<template>
  <div>
    <h2>Location</h2>
    <div class="Address text">{{location.address}}</div>
    <div class="Landmark text" v-if="landmark">{{landmark.min}} min from <span>{{landmark.name}}</span></div>
    <google-embed-map class="Map" :lat-lng="location.latLng" height="224"/>
  </div>
</template>

<script>
  // Distance in km
  function getDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // Radius of the earth in km
    const dLat = deg2rad(lat2 - lat1);  // deg2rad below
    const dLon = deg2rad(lon2 - lon1);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  function deg2rad(deg) {
    return deg * (Math.PI / 180)
  }

  import GoogleEmbedMap from "../core/GoogleEmbedMap";

  export default {
    name: "PlaceLocation",
    components: {GoogleEmbedMap},
    props: {
      location: {
        type: Object,
        required: true
      }
    },
    computed: {
      landmark() {
        const landmark = this.location.landmarks[0]
        if (landmark) {
          const latLng = this.location.latLng.split(',')
          const landmarkLatLng = landmark.location.latLng.split(',')
          const distance = getDistance(parseFloat(latLng[0]), parseFloat(latLng[1]), parseFloat(landmarkLatLng[0]), parseFloat(landmarkLatLng[1]))
          const min = distance / 0.070

          return {
            min: Math.ceil(min),
            ...landmark
          }
        }
      }
    }
  }
</script>

<!-- • -->

<style scoped lang="less">
  .Address {
    margin-top: 16px;
  }

  .Landmark {
    margin-bottom: 16px;

    span {
      font-weight: 600;
    }
  }

  .Map {
    margin-top: 16px;
  }
</style>
