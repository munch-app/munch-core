<template>
  <div>
    <div class="SearchResult Container" v-if="cards.length > 0">
      <h3 class="TopResult">Top Results</h3>
      <b-row>
        <b-col class="Card" cols="12" md="4" lg="3" v-for="card in cards" :key="card['_uniqueId']"
               v-if="card['_cardId'] === 'basic_Place_20171211'">
          <search-place-card :place="card.place"/>
        </b-col>
      </b-row>
    </div>
  </div>
</template>

<script>
  import SearchBarFilter from "../components/search/SearchBarFilter";
  import SearchPlaceCard from "../components/search/SearchPlaceCard";

  export default {
    layout: 'search',
    components: {SearchPlaceCard, SearchBarFilter},
    loading: false,
    head() {
      return {
        title: 'Search | Munch',
      }
    },
    data() {
      return {cards: []}
    },
    computed: {
      searchQuery() {
        return this.$store.state.search.query
      },
    },
    watch: {
      searchQuery(newQuery) {
        this.search(newQuery)
      }
    },
    methods: {
      search(newQuery) {
        this.$axios.$post('/api/search?from=0&size=20', newQuery)
          .then(({data}) => {
            this.cards = data
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchResult {
    .TopResult {
      margin-top: 24px;
    }
  }

  .Card {
    margin-top: 12px;
    margin-bottom: 12px;
  }
</style>
