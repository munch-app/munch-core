<template>
  <div class="mt-24">
    <div class="flex-between flex-justify-center">
      <div>
        <h2>Discover {{area.name}}</h2>
      </div>

      <div class="none tablet-b desktop-b">
        <div class="weight-600 s700 hover-pointer" @click="onMore">Show all places in {{area.name}}</div>
      </div>
    </div>

    <horizontal-scroll-view class="CardList PlaceList container-remove-gutter mt-24" :items="card.places"
                            :map-key="a => a.placeId"
                            :padding="24">
      <template slot-scope="{item}">
        <place-card class="CardItem" :place="item"/>
      </template>
    </horizontal-scroll-view>

    <div class="none mobile-b mt-24">
      <button class="blue-outline w-100" @click="onMore">
        <span class="text-ellipsis-1l">Show all places in {{area.name}}</span>
      </button>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../../places/PlaceCard";
  import HorizontalScrollView from "../../../core/HorizontalScrollView";

  export default {
    name: "SearchCardLocationArea",
    components: {HorizontalScrollView, PlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      area() {
        return this.card.area
      }
    },
    methods: {
      onMore() {
        const area = this.card.area

        this.$store.dispatch('filter/location', {areas: [area], type: 'Where'})
        this.$store.dispatch('search/start')
      }
    }
  }
</script>

<style scoped lang="less">

</style>
