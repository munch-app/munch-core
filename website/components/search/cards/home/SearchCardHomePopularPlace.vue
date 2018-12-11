<template>
  <div>
    <h2 class="mt-24">Popular Places in Singapore</h2>
    <p class="b-a75">Where the cool kids and food geeks go.</p>

    <horizontal-scroll-view class="CardList PlaceList container-remove-gutter mt-24" :items="card.places" :map-key="a => a.placeId"
                            :padding="24">
      <template slot-scope="{item}">
        <place-card class="CardItem" :place="item"/>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import PlaceCard from "../../../places/PlaceCard";
  import HorizontalScrollView from "../../../core/HorizontalScrollView";

  export default {
    name: "SearchCardHomePopularPlace",
    components: {HorizontalScrollView, PlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">

</style>
