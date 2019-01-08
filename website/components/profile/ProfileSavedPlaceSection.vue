<template>
  <section class="Places mt-24 mb-64 flex-wrap">
    <div class="Card" v-for="item in items" :key="item.placeId">
      <place-card :place="item.place" :saved="true"/>
    </div>

    <no-ssr class="w-100 flex-center mtb-24">
      <beat-loader color="#084E69" size="14px" v-if="more"
                   v-observe-visibility="{callback: (v) => v && append(),throttle:300}"/>
    </no-ssr>
  </section>

</template>

<script>
  import {mapGetters} from 'vuex'
  import PlaceCard from "../places/PlaceCard";

  export default {
    name: "ProfileSavedPlaceSection",
    components: {PlaceCard},
    computed: {
      ...mapGetters('user/places', ['items', 'more']),
    },
    mounted() {
      this.append()
    },
    methods: {
      append() {
        this.$store.dispatch('user/places/append')
      }
    }
  }
</script>

<style scoped lang="less">
  .Places {
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

    @media (min-width: 500px) {
      flex: 0 0 50%;
      max-width: 50%;
    }

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
  }
</style>
