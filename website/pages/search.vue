<template>
  <div class="ZeroSpacing">
    <div class="SearchResult Container" v-if="cards && query">
      <div class="Result">
        <card-delegator v-for="card in cards" :key="card['_uniqueId']" :card="card"/>
      </div>
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
    margin-top: 12px;
  }

  .Result {
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;

    margin-bottom: 64px;
  }
</style>
