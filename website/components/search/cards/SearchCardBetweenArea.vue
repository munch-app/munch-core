<template>
  <div>
    <div class="Header">
      <div>
        <h2>{{header}}</h2>

        <apple-map class="Map" :annotations="annotations"/>
      </div>
    </div>

    <div class="Places container-width">
      <div class="Card Initial hover-pointer" v-for="place in places" :key="place.placeId">
        <place-card :place="place"/>
      </div>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../places/PlaceCard";
  import AppleMap from "../../core/AppleMap";

  export default {
    name: "SearchCardBetweenArea",
    components: {AppleMap, PlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      count() {
        return this.card.count
      },
      area() {
        return this.card.area
      },
      index() {
        return this.card.index
      },
      places() {
        return this.card.places
      },
      header() {
        return `An ideal spot for everyone: ${this.card.area.name}`
      },
      annotations() {
        return this.card.places.map(place => {
          const latLng = place.location.latLng.split(",")

          return {
            lat: parseFloat(latLng[0]),
            lng: parseFloat(latLng[1]),
            name: place.name
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .Header {
    margin-bottom: 24px;

    .Map {
      margin-top: 24px;
      width: 100%;
      height: 224px;

      border-radius: 3px;
      overflow: hidden;
    }
  }

  .Places {
    display: flex;
    flex-wrap: wrap;

    margin-right: -12px;
    margin-left: -12px;
  }

  .Card {
    position: relative;
    width: 100%;
    min-height: 1px;
    padding: 18px 12px;

    flex: 0 0 100%;
    max-width: 100%;

    @media (min-width: 768px) {
      flex: 0 0 33.333333%;
      max-width: 33.333333%;
    }

    @media (min-width: 1200px) {
      flex: 0 0 25%;
      max-width: 25%;
    }

    @media (min-width: 1600px) {
      flex: 0 0 20%;
      max-width: 20%;
    }

    &.Initial {
      color: initial;
      text-decoration: initial;
    }
  }
</style>
