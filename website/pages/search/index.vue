<template>
  <div class="zero-spacing">
    <div class="SearchResult container" v-if="cards && query">
      <div class="Result">
        <card-delegator v-for="card in cards" :key="card['_uniqueId']" :card="card"/>
      </div>

      <no-ssr>
        <div class="LoadingIndicator" v-if="more" v-observe-visibility="{
          callback: visibilityChanged,
          throttle: 300,
        }">
          <beat-loader color="#084E69" size="14px"/>
        </div>
      </no-ssr>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import CardDelegator from "../../components/search/cards/CardDelegator";

  export default {
    components: {CardDelegator},
    head() {
      const {name, description, keywords} = this.$store.state.search.seo

      const meta = []
      meta.push({name: 'robots', content: `follow,${name ? 'index' : 'noindex'}`})
      if (description) meta.push({hid: 'description', name: 'description', content: description})
      if (keywords) meta.push({name: 'keywords', content: keywords})
      return {title: `${name || 'Search'} | Munch`, meta}
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
    computed: {
      ...mapGetters('search', ['query', 'cards', 'more']),
      ...mapGetters('filter', ['selected']),
    },
    methods: {
      visibilityChanged(isVisible, entry) {
        if (isVisible) {
          this.$store.dispatch('search/append')
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchResult {
    margin-top: 12px;
    margin-bottom: 64px;
  }

  .Result {
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
