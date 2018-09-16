<template>
  <search-card-place
    class="Card NoSelect Initial Pointer"
    v-if="isCardId('basic_Place_20171211')"
    :place="card.place"
  />
  <search-card-area-cluster-list
    class="Card NoSelect Initial"
    v-else-if="isCardId('injected_AreaClusterList_20180621')"
    :areas="card.areas"
  />
  <div v-else style="display: none"></div>
</template>

<script>
  import SearchCardPlace from "./SearchCardPlace";
  import SearchCardAreaClusterList from "./SearchCardAreaClusterList";

  export default {
    name: "CardDelegator",
    components: {SearchCardAreaClusterList, SearchCardPlace},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    methods: {
      isCardId(cardId) {
        return this.card['_cardId'] === cardId
      }
    }
  }
</script>

<style scoped lang="less">
  .Initial {
    color: initial;
    text-decoration: initial;
  }

  .Pointer:hover {
    cursor: pointer;
  }

  .Card {
    position: relative;
    width: 100%;
    min-height: 1px;
    padding: 18px 12px;

    -ms-flex: 0 0 100%;
    flex: 0 0 100%;
    max-width: 100%;

    @media (min-width: 768px) {
      -ms-flex: 0 0 33.333333%;
      flex: 0 0 33.333333%;
      max-width: 33.333333%;
    }

    @media (min-width: 1200px) {
      -ms-flex: 0 0 25%;
      flex: 0 0 25%;
      max-width: 25%;
    }
  }
</style>
