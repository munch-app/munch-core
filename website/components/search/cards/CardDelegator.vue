<template>
  <!-- Default Cards -->
  <search-card-place
    class="Card Initial Pointer"
    :class="{WithMap: showsMap}"
    v-if="isCardId('Place_2018-12-29')"
    :card="card"
  />
  <search-card-header
    class="Card Initial FullWidth NoBottom"
    v-else-if="isCardId('Header_2018-11-29')"
    :card="card"
  />
  <search-card-no-result
    class="Card Initial FullWidth"
    v-else-if="isCardId('NoResult_2017-12-08')"
  />

  <!-- Area Cluster Card -->
  <search-card-area-cluster-list
    class="Card Initial FullWidth"
    v-else-if="isCardId('AreaClusterList_2018-06-21')"
    :areas="card.areas"
  />
  <search-card-area-cluster-header
    class="Card Initial FullWidth"
    v-else-if="isCardId('AreaClusterHeader_2018-06-21')"
    :area="card.area"
  />

  <!-- Suggestion Card -->
  <search-card-suggestion-tag
    class="Card Initial FullWidth"
    v-else-if="isCardId('SuggestedTag_2018-05-11')"
    :card="card"
  />

  <!-- Between Cards -->
  <search-card-between-header
    class="Card Initial FullWidth"
    v-else-if="isCardId('BetweenHeader_2018-12-13')"
    :card="card"
  />

  <!-- Home Cards -->
  <search-card-home-tab
    class="Card Initial FullWidth"
    v-else-if="isCardId('HomeTab_2018-11-29')"
  />
  <search-card-home-recent-place
    class="Card Initial FullWidth"
    v-else-if="isCardId('HomeRecentPlace_2018-12-10')"
    :card="card"
  />
  <search-card-home-popular-place
    class="Card Initial FullWidth"
    v-else-if="isCardId('HomePopularPlace_2018-12-10')"
    :card="card"
  />
  <search-card-home-award-collection
    class="Card Initial FullWidth"
    v-else-if="isCardId('HomeAwardCollection_2018-12-10')"
    :card="card"
  />

  <!-- Series Card -->
  <search-card-series-list
    class="Card Initial FullWidth"
    v-else-if="isCardId('SeriesList_2019-02-25')"
    :card="card"
  />

  <!-- Location Cards -->
  <search-card-location-banner
    class="Card Initial FullWidth"
    v-else-if="isCardId('LocationBanner_2018-12-10')"
    :card="card"
  />
  <search-card-location-area
    class="Card Initial FullWidth"
    v-else-if="isCardId('LocationArea_2018-12-10')"
    :card="card"
  />

  <!-- Collection Cards -->
  <search-card-collection-header
    class="Card Initial FullWidth"
    v-else-if="isCardId('CollectionHeader_2018-12-11')"
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
  import SearchCardAreaClusterHeader from "./SearchCardAreaClusterHeader";
  import SearchCardSuggestionTag from "./SearchCardSuggestionTag";
  import SearchCardBetweenHeader from "./SearchCardBetweenHeader";
  import SearchCardHomeTab from "./home/SearchCardHomeTab";
  import SearchCardHomeRecentPlace from "./home/SearchCardHomeRecentPlace";
  import SearchCardHomePopularPlace from "./home/SearchCardHomePopularPlace";
  import SearchCardHomeAwardCollection from "./home/SearchCardHomeAwardCollection";
  import SearchCardLocationBanner from "./location/SearchCardLocationBanner";
  import SearchCardLocationArea from "./location/SearchCardLocationArea";
  import SearchCardCollectionHeader from "./collection/SearchCardCollectionHeader";
  import SearchCardSeriesList from "./content/SearchCardSeriesList";

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
      SearchCardSeriesList,
      SearchCardCollectionHeader,
      SearchCardLocationArea,
      SearchCardLocationBanner,
      SearchCardHomeAwardCollection,
      SearchCardHomePopularPlace,
      SearchCardHomeRecentPlace,
      SearchCardHomeTab,
      SearchCardBetweenHeader,
      SearchCardSuggestionTag,
      SearchCardAreaClusterHeader,
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
<style lang="less">
  .CardList.PlaceList {
    height: calc(60vw * 0.6 + 132px);

    @media (min-width: 500px) {
      height: calc((100vw - 48px - 24px) / 2 * 0.6 + 132px);
    }

    @media (min-width: 768px) {
      height: calc((100vw - 48px - 48px) / 3 * 0.6 + 132px);
    }

    @media (min-width: 1200px) {
      height: calc((100vw - 160px - 72px) / 4 * 0.6 + 132px);
    }

    @media (min-width: 1600px) {
      height: calc((100vw - 160px - 96px) / 5 * 0.6 + 132px);
    }

    @media (min-width: 1920px) {
      height: calc((100vw - 160px - 120px) / 6 * 0.6 + 132px);
    }
  }

  .CardList .CardItem {
    width: 60vw;

    @media (min-width: 500px) {
      width: calc((100vw - 48px - 24px) / 2);
    }

    @media (min-width: 768px) {
      width: calc((100vw - 48px - 48px) / 3);
    }

    @media (min-width: 1200px) {
      width: calc((100vw - 160px - 72px) / 4);
    }

    @media (min-width: 1600px) {
      width: calc((100vw - 160px - 96px) / 5);
    }

    @media (min-width: 1920px) {
      width: calc((100vw - 160px - 120px) / 6);
    }
  }

  .CardList.Square, .CardList.Square .CardItem {
    height: calc(60vw * 1.2);

    @media (min-width: 500px) {
      height: calc(((100vw - 48px - 24px) / 2) * 1.2);
    }

    @media (min-width: 768px) {
      height: calc(((100vw - 48px - 48px) / 3) * 1.2);
    }

    @media (min-width: 1200px) {
      height: calc(((100vw - 160px - 72px) / 4) * 1.2);
    }

    @media (min-width: 1600px) {
      height: calc(((100vw - 160px - 96px) / 5) * 1.2);
    }

    @media (min-width: 1920px) {
      height: calc(((100vw - 160px - 120px) / 6) * 1.2);
    }
  }
</style>

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

    @media (min-width: 1920px) {
      flex: 0 0 16.666667%;
      max-width: 16.666667%;
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
