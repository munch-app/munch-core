<template>
  <div>
    <div class="Address text">{{location.address}}</div>
    <div class="Landmark text" v-if="landmark">{{landmark.min}} min from <span>{{landmark.name}}</span></div>

    <div class="GoogleMap">
      <a class="Interaction" target="_blank" :href="`https://www.google.com/maps?q=${location.latLng}`" style="height: 224px"></a>
      <google-embed-map :lat-lng="location.latLng" height="224"/>
    </div>
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
      placeId: {
        type: String,
        required: true
      },
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

  .GoogleMap {
    position: relative;
    margin-top: 32px;

    .Interaction {
      z-index: 1;
      width: 100%;
      position: absolute;
      background-color: rgba(0, 0, 0, 0);

      &:hover {
        cursor: pointer;
      }
    }
  }
</style>
