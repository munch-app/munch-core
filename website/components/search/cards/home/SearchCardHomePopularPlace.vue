<template>
  <div class="mt-24">
    <div class="flex-between flex-justify-center">
      <div>
        <h2>Popular Places in Singapore</h2>
        <p class="b-a75">Where the cool kids and food geeks go.</p>
      </div>

      <div class="none tablet-b desktop-b">
        <div class="weight-600 s700 hover-pointer" @click="onMore">Show all popular places</div>
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
      <button class="secondary-outline w-100" @click="onMore">Show all popular places</button>
    </div>
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
    },
    methods: {
      onMore() {
        const collectionId = this.card.collection.collectionId
        this.$router.push({path: `/collections/${collectionId}`})
      }
    }
  }
</script>

<style scoped lang="less">

</style>
