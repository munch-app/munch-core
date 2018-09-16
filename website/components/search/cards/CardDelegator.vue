<template>
  <search-card-place
    class="Card NoSelect Initial Pointer"
    v-if="isCardId('basic_Place_20171211')"
    :place="card.place"
  />
  <search-card-place-small
    class="Card NoSelect Initial Pointer"
    v-else-if="isCardId('basic_SmallPlace_20180129')"
    :place="card.place"
  />
  <search-card-area-cluster-list
    class="Card NoSelect Initial FullWidth"
    v-else-if="isCardId('injected_AreaClusterList_20180621')"
    :areas="card.areas"
  />
  <search-card-header
    class="Card NoSelect Initial FullWidth NoBottom"
    v-else-if="isCardId('injected_Header_20180120')"
    :title="card.title"
  />
  <search-card-no-result
    class="Card NoSelect Initial FullWidth"
    v-else-if="isCardId('injected_NoResult_20171208')"
  />
  <search-card-no-result-location
    class="Card NoSelect Initial FullWidth"
    v-else-if="isCardId('injected_NoResultLocation_20171208')"/>
  <search-card-area-cluster-header
    class="Card NoSelect Initial FullWidth"
    v-else-if="isCardId('injected_AreaClusterHeader_20180621')"
    :area="card.area"
  />
  <search-card-suggestion-tag
    class="Card NoSelect Initial FullWidth"
    v-else-if="isCardId('injected_SuggestedTag_20180511')"
    :card="card"
  />
  <div v-else style="display: none"></div>
</template>

<script>
  import SearchCardPlace from "./SearchCardPlace";
  import SearchCardAreaClusterList from "./SearchCardAreaClusterList";
  import SearchCardHeader from "./SearchCardHeader";
  import SearchCardNoResult from "./SearchCardNoResult";
  import SearchCardNoResultLocation from "./SearchCardNoResultLocation";
  import SearchCardAreaClusterHeader from "./SearchCardAreaClusterHeader";
  import SearchCardPlaceSmall from "./SearchCardPlaceSmall";
  import SearchCardSuggestionTag from "./SearchCardSuggestionTag";

  export default {
    name: "CardDelegator",
    components: {
      SearchCardSuggestionTag,
      SearchCardPlaceSmall,
      SearchCardAreaClusterHeader,
      SearchCardNoResultLocation,
      SearchCardNoResult, SearchCardHeader, SearchCardAreaClusterList, SearchCardPlace
    },
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

    &.FullWidth {
      -ms-flex: 0 0 100%;
      flex: 0 0 100%;
      max-width: 100%;
    }

    &.NoBottom {
      padding-bottom: 0;
    }

    &.NoTop {
      padding-top: 0;
    }
  }
</style>
