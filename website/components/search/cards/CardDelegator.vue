<template>
  <search-card-place
    class="Card no-select Initial Pointer"
    :class="{WithMap: showsMap}"
    v-if="isCardId('Place_2018-12-29')"
    :card="card"
  />
  <search-card-area-cluster-list
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_AreaClusterList_20180621')"
    :areas="card.areas"
  />
  <search-card-header
    class="Card no-select Initial FullWidth NoBottom"
    v-else-if="isCardId('Header_2018-11-29')"
    :card="card"
  />
  <search-card-navigation-header
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_NavigationHeader_20181102')"
    :card="card"
  />
  <search-card-no-result
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_NoResult_20171208')"
  />
  <search-card-no-result-location
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_NoResultLocation_20171208')"/>
  <search-card-area-cluster-header
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_AreaClusterHeader_20180621')"
    :area="card.area"
  />
  <search-card-suggestion-tag
    class="Card no-select Initial FullWidth"
    v-else-if="isCardId('injected_SuggestedTag_20180511')"
    :card="card"
  />
  <div v-else style="display: none"></div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import SearchCardPlace from "./SearchCardPlace";
  import SearchCardAreaClusterList from "./SearchCardAreaClusterList";
  import SearchCardHeader from "./SearchCardHeader";
  import SearchCardNoResult from "./SearchCardNoResult";
  import SearchCardNoResultLocation from "./SearchCardNoResultLocation";
  import SearchCardAreaClusterHeader from "./SearchCardAreaClusterHeader";
  import SearchCardSuggestionTag from "./SearchCardSuggestionTag";
  import SearchCardNavigationHeader from "./SearchCardNavigationHeader";

  function isElementInViewport(el) {
    const topBar = 56 + 48 // Top Bar
    const height = (window.innerHeight || document.documentElement.clientHeight)
    const rect = el.getBoundingClientRect()

    const padding = 80 // Padding for visibility

    function isWithin(y) {
      return (topBar + padding) <= y && y <= (height - padding)
    }
    return isWithin(rect.top) || isWithin(rect.bottom)
  }

  export default {
    name: "CardDelegator",
    components: {
      SearchCardNavigationHeader,
      SearchCardSuggestionTag,
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
    computed: {
      ...mapGetters('search', ['showsMap']),
    },
    methods: {
      isCardId(cardId) {
        return this.card['_cardId'] === cardId
      },
      isVisible() {
        return isElementInViewport(this.$el)
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

    &.WithMap {
      @media (min-width: 768px) {
        flex: 0 0 33.333333%;
        max-width: 33.333333%;
      }
    }

    &.FullWidth {
      flex: 0 0 100%;
      max-width: 100%;
    }

    &.NoBottom {
      padding-bottom: 0;
    }

    &.NoTop {
      padding-top: 0;
    }

    &.Initial {
      color: initial;
      text-decoration: initial;
    }

    &.Pointer:hover {
      cursor: pointer;
    }
  }
</style>
