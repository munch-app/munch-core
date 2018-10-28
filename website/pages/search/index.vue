<template>
  <div class="zero-spacing">
    <div class="SearchResult container" v-if="cards && query">
      <div class="Result">
        <card-delegator v-for="card in cards" :key="card._uniqueId" :card="card"
                        v-observe-visibility="{callback: (v) => visibleCard(v, card),throttle:200}"
                        @mouseover.native="onMouse(card, true)" @mouseleave.native="onMouse(card, false)"
        />

        <no-ssr>
          <div class="LoadingIndicator flex-center" v-if="more"
               v-observe-visibility="{callback: (v) => visibleLoading(v),throttle:300}">
            <beat-loader color="#084E69" size="14px"/>
          </div>
        </no-ssr>
      </div>

      <div class="MapView" v-if="showsMap">
        <apple-map ref="map" class="Map" :places="map.places" :highlight="map.highlight"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import Vue from 'vue'

  import CardDelegator from "../../components/search/cards/CardDelegator";
  import AppleMap from "../../components/core/AppleMap";
  import AppleMapPlaceAnnotation from "../../components/core/AppleMapPlaceAnnotation";

  export default {
    components: {AppleMapPlaceAnnotation, AppleMap, CardDelegator},
    head() {
      const {name, description, keywords} = this.$store.state.search.seo

      const meta = []
      meta.push({name: 'robots', content: `follow,${name ? 'index' : 'noindex'}`})
      if (description) meta.push({hid: 'description', name: 'description', content: description})
      if (keywords) meta.push({name: 'keywords', content: keywords})
      return {title: `${name || 'Search'} · Munch`, meta}
    },
    async fetch({store, route, params}) {
      const named = params.named
      if (named) {
        return store.dispatch('search/startNamed', named)
      }

      const qid = route.query.qid
      if (qid) {
        return store.dispatch('search/startQid', qid)
      }
    },
    data() {
      return {
        visible: {},
        map: {
          places: [],
          highlight: null,
        }
      }
    },
    computed: {
      ...mapGetters('search', ['query', 'cards', 'more', 'showsMap']),
      ...mapGetters('filter', ['selected']),
      visiblePlaces() {
        return Object.keys(this.visible).map(key => {
          const card = this.visible[key]
          if (card._cardId === 'basic_Place_20171211') {
            return card.place
          }
        }).filter(a => !!a)
      },
    },
    mounted() {
      const search = this.$store.state.search
      this.onSearch(search.type, search.query)
    },
    methods: {
      visibleLoading(isVisible) {
        if (isVisible) {
          this.$store.dispatch('search/append')
        }
      },
      visibleCard(isVisible, card) {
        if (isVisible) {
          if (this.visible[card._uniqueId]) return
          Vue.set(this.visible, card._uniqueId, card)
        } else {
          Vue.delete(this.visible, card._uniqueId)
        }

        if (this.showsMap) {
          clearTimeout(this.$mapUpdate)
          this.$mapUpdate = setTimeout(() => {
            this.map.places = this.visiblePlaces
            this.$refs.map.centerAnnotations()
          }, 300);
        }
      },
      onMouse(card, on) {
        if (on && card._cardId === 'basic_Place_20171211') {
          this.map.highlight = card.place
        } else {
          this.map.highlight = null
        }
      },
      onSearch(type, query) {
        this.visible = {}

        // const type = this.$store.state.search.type
        const parameters = {}
        parameters['event_category'] = (type || 'search').toLowerCase()

        const locationType = query && query.filter && query.filter.location && query.filter.location.type || ''
        parameters['event_label'] = `search_${locationType.toLowerCase()}`

        this.$gtag('event', 'search', parameters)
      }
    },
    watch: {
      query(query) {
        if (query) {
          const search = this.$store.state.search
          this.onSearch(search.type, search.query)
        }
      }
    }
  }
</script>

<style scoped lang="less">
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
    margin-right: -12px;
    margin-left: -12px;
  }

  .MapView {
    flex-shrink: 0;
    position: relative;

    width: 33vw;
    min-height: calc(100vh - 48px - 56px - 48px);
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
