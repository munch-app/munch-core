<template>
  <div class="zero-spacing">
    <div class="SearchResult container" v-if="cards && query">
      <div class="Result">
        <card-delegator v-for="card in cards" :key="card['_uniqueId']" :card="card"/>
      </div>

      <div class="LoadingIndicator" v-if="more" v-observe-visibility="{
          callback: visibilityChanged,
          throttle: 300,
        }">
        <beat-loader color="#084E69" size="14px"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import CardDelegator from "../components/search/cards/CardDelegator";
  import BeatLoader from 'vue-spinner/src/BeatLoader.vue'

  if (process.browser) require('intersection-observer')

  export default {
    components: {CardDelegator, BeatLoader},
    head() {
      return {
        title: 'Search | Munch',
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
