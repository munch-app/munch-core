<template>
  <div v-if="false">
    <h2 class="mt-24">Your Recent Places</h2>
    <p class="b-a75">Don't worry, we won't tell anybody.</p>
    <horizontal-scroll-view class="CardList PlaceList container-remove-gutter mt-24" :items="card.places" :map-key="a => a.placeId"
                            :padding="24">
      <template slot-scope="{item}">
        <place-card class="CardItem" :place="item"/>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import HorizontalScrollView from "../../../core/HorizontalScrollView";
  import PlaceCard from "../../../places/PlaceCard";

  export default {
    name: "SearchCardHomeRecentPlace",
    components: {PlaceCard, HorizontalScrollView},
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
