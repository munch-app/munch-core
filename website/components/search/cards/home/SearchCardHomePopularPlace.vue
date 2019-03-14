<template>
  <div>
    <search-card-place-list :card="computedCard"/>

    <div class="mt-48">
      <div class="none mobile-b">
        <button class="secondary-outline w-100" @click="onMore">Show all popular places</button>
      </div>

      <div class="none tablet-b desktop-b">
        <button class="secondary-outline" @click="onMore">Show all popular places</button>
      </div>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../../places/PlaceCard";
  import HorizontalScrollView from "../../../core/HorizontalScrollView";
  import SearchCardPlaceList from "../places/SearchCardPlaceList";

  export default {
    name: "SearchCardHomePopularPlace",
    components: {SearchCardPlaceList, HorizontalScrollView, PlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      computedCard() {
        return {
          places: this.card.places,
          title: 'Popular Places in Singapore',
          subtitle: 'Where the cool kids and food geeks go.',
        }
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
