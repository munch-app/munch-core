<template>
  <div>
    <div class="Header">
      <h2>{{area.name}}</h2>
    </div>

    <div class="Places">
      <div class="Card Initial hover-pointer" v-for="place in places" :key="place.placeId">
        <place-card :place="place"/>
      </div>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../places/PlaceCard";
  export default {
    name: "SearchCardBetweenArea",
    components: {PlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      area() {
        return this.card.area
      },
      places() {
        return this.card.places
      }
    }
  }
</script>

<style scoped lang="less">
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
