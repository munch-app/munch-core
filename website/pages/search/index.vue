<template>
  <div class="zero-spacing Page">
    <div class="SearchResult container" v-if="cards && query">
      <div class="Result">
        <card-delegator v-for="card in cards" :key="card._uniqueId" :card="card" ref="cards"
                        @mouseover.native="onMouse(card, true)" @mouseleave.native="onMouse(card, false)"
        />

        <no-ssr>
          <div class="LoadingIndicator flex-center" v-if="more"
               v-observe-visibility="{callback: (v) => visibleLoading(v),throttle:300}">
            <beat-loader color="#084E69" size="14px"/>
          </div>
        </no-ssr>
      </div>

      <div class="MapView index-content-overlay" v-if="showsMap">
        <apple-map ref="map" class="Map">
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
    async fetch({store, route}) {
      const qid = route.query.qid
      if (qid) {
        return store.dispatch('search/startQid', qid)
      }
    },
    mounted() {
      window.addEventListener("scroll", this.onScroll)
      this.onMapUpdate()
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
      ...mapGetters('search', ['query', 'cards', 'more', 'showsMap', 'searched']),
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
    min-height: calc(100vh - 48px - 56px);
  }

  .SearchResult {
    margin-top: 12px;
    margin-bottom: 64px;

    display: flex;
  }

  .Result {
    width: 100%;

    display: flex;
    flex-wrap: wrap;
    flex-grow: 1;
    align-self: flex-start;

    margin-right: -12px;
    margin-left: -12px;
  }

  .MapView {
    flex-shrink: 0;
    position: relative;

    width: 33vw;
    margin-left: 24px;

    @media (max-width: 991.98px) {
      display: none;
    }

    .Map {
      position: fixed;
      margin-top: 12px;
      height: calc(100vh - 48px - 56px - 48px);
      width: 33vw;

      border-radius: 3px;
      overflow: hidden;
    }
  }

  .LoadingIndicator {
    width: 100%;
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
