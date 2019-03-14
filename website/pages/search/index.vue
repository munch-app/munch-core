<template>
  <div class="zero Page">
    <div class="SearchResult flex container" v-if="cards && query">
      <div class="Result flex-wrap flex-grow w-100">
        <card-delegator v-for="card in cards" :key="card._uniqueId" :card="card" ref="cards"
                        @mouseover.native="onMouse(card, true)" @mouseleave.native="onMouse(card, false)"
        />

        <div class="flex-center w-100" style="height: 64px">
          <no-ssr>
            <div class="p-24" v-if="more" v-observe-visibility="{callback: (v) => visibleLoading(v),throttle:300}">
              <beat-loader color="#084E69" size="14px"/>
            </div>
          </no-ssr>
        </div>
      </div>

      <div class="MapView ml-24 relative flex-no-shrink index-content-overlay" v-if="showsMap">
        <apple-map ref="map" class="Map fixed border-3 overflow-hidden">
          <apple-map-place-annotation v-for="place in map.places" :key="place.placeId" :place="place"
                                      :focused="map.focusedPlaceId === place.placeId"
          />
        </apple-map>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import CardDelegator from "../../components/search/cards/CardDelegator";
  import AppleMap from "../../components/core/AppleMap";
  import AppleMapPlaceAnnotation from "../../components/core/AppleMapPlaceAnnotation";

  export default {
    components: {AppleMapPlaceAnnotation, AppleMap, CardDelegator},
    head() {
      return {title: 'Search · Munch Singapore'}
    },
    asyncData({store, route}) {
      if (!store.state.search.query && route.query.qid) {
        return store.dispatch('search/startQID', route.query.qid)
          .then(() => {
            return {fromQid: true}
          })
      }
      return {fromQid: false}
    },
    mounted() {
      window.addEventListener("scroll", this.onScroll)
      this.onMapUpdate()

      if (this.fromQid && this.query) {
        this.$track.qid(`Search - ${this.locationType}`)
      }

      const g = this.$route.query.g;
      if (g) {
        this.$track.link('Search', g)

        let query = Object.assign({}, this.$route.query)
        delete query.g
        this.$router.replace({query})
      }
    },
    beforeRouteUpdate(to, from, next) {
      if (this.$store.state.search.qid !== to.query.qid) {
        this.$store.dispatch('search/startQID', to.query.qid)
      }
      next()
    },
    beforeDestroy() {
      window.removeEventListener("scroll", this.onScroll)
    },
    data() {
      return {
        map: {
          places: [],
          focusedPlaceId: null
        }
      }
    },
    computed: {
      ...mapGetters('search', ['query', 'cards', 'more', 'showsMap', 'searched', 'locationType']),
      ...mapGetters('filter', ['selected']),
    },
    watch: {
      searched() {
        this.$nextTick(() => {
          this.onMapUpdate()
        })
      }
    },
    methods: {
      visibleLoading(isVisible) {
        if (isVisible) {
          this.$store.dispatch('search/append')
          this.onMapUpdate()
        }
      },
      onScroll() {
        if (this.showsMap && this.$refs.map) {
          clearTimeout(this.$mapUpdate)
          this.$mapUpdate = setTimeout(() => {
            this.onMapUpdate()
          }, 500);
        }
      },
      onMapUpdate() {
        if (this.showsMap && this.$refs.map && this.$refs.cards) {
          const places = this.$refs.cards.map(delegator => {
            if (delegator.isVisible()) return delegator.card.place
          }).filter(p => !!p)

          this.map.places.splice(0)
          this.map.places.push(...places)
          this.$nextTick(() => {
            this.$refs.map.centerAnnotations()
          })
        }
      },
      onMouse(card, over) {
        if (over && card.place) {
          this.map.focusedPlaceId = card.place.placeId
        } else {
          this.map.focusedPlaceId = null
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Page {
    min-height: calc(100vh - 48px - 72px/*Header72px*/);
  }

  .SearchResult {
    margin-top: 12px;
    margin-bottom: 64px;
  }

  .Result {
    align-self: flex-start;

    margin-right: -12px;
    margin-left: -12px;
  }

  .MapView {
    width: 33vw;

    @media (max-width: 991.98px) {
      display: none;
    }
  }

  .Map {
    margin-top: 12px;
    height: calc(100vh - 48px - 72px/*Header72px*/ - 48px);
    width: 33vw;
  }
</style>
