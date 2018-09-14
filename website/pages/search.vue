<template>
  <div>
    <div class="SearchResult Container" v-if="cards && query">
      <h2 class="TopResult">Top Results</h2>
      <b-row>
        <card-delegator v-for="card in cards" :key="card['_uniqueId']" :card="card"/>
      </b-row>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import CardDelegator from "../components/search/cards/CardDelegator";

  export default {
    layout: 'search',
    components: {CardDelegator},
    head() {
      return {
        title: 'Search | Munch',
      }
    },
    computed: {
      ...mapGetters('search', ['query', 'cards', 'more']),
      ...mapGetters('filter', ['selected']),
    }
  }
</script>

<style scoped lang="less">
  .SearchResult {
    .TopResult {
      margin-top: 24px;
    }
  }
</style>
