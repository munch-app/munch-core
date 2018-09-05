<template>
  <div class="Results">
    <b-container>
      <b-row style="margin: 24px 0">
        <b-col v-if="searchQuery.filter.tag && searchQuery.filter.tag.positives">
          Tags: {{searchQuery.filter.tag.positives}}
        </b-col>
        <b-col v-if="searchQuery.filter.area && searchQuery.filter.area.name">
          Area: {{searchQuery.filter.area.name}}
        </b-col>
      </b-row>
      <div class="SuggestPlaces">

      </div>
      <b-row class="SearchCards">
        <b-col class="Card" md="3" v-for="card in cards" :key="card['_uniqueId']"
               v-if="card['_cardId'] === 'basic_Place_20171211'">
          <search-place-card :place="card.place"/>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
  import SearchPlaceCard from "../components/search/SearchPlaceCard";

  export default {
    layout: 'search',
    components: {SearchPlaceCard},
    head() {
      return {
        title: 'Search | Munch',
      }
    },
    computed: {
      searchQuery() {
        return this.$store.state.search.query
      },
      query() {
        return this.$route.query.query
      }
    },
    asyncData({$axios, store, query}) {
      const searchQuery = store.state.search.query
      let text = query.query
      if (text) {
        return $axios.$post('/api/search/suggest', {
          "text": text,
          "searchQuery": searchQuery
        }).then(({data}) => {
          let places = data.places

          if (data.assumptions && data.assumptions[0] && data.assumptions[0].searchQuery) {
            store.commit('search/update', data.assumptions[0].searchQuery)
            return $axios.$post('/api/search?from=0&size=20', data.assumptions[0].searchQuery)
              .then(({data}) => {
                return {
                  cards: data,
                  places: places
                };
              })
          }

          return {
            cards: [],
            places: places
          };
        })
      }

      return $axios.$post('/api/search?from=0&size=20', searchQuery)
        .then(({data}) => {
          return {cards: data};
        })
    },
    watch: {
      searchQuery(newQuery) {
        this.$axios.$post('/api/search?from=0&size=20', newQuery)
          .then(({data}) => {
            this.cards = data
          })
      },
      query(newQuery) {
        this.$axios.$post('/api/search/suggest', {
          "text": newQuery,
          "searchQuery": this.searchQuery
        }).then(({data}) => {
          this.places = data.places

          if (data.assumptions && data.assumptions[0] && data.assumptions[0].searchQuery) {
            this.$store.commit('search/update', data.assumptions[0].searchQuery)
            return this.$axios.$post('/api/search?from=0&size=20', data.assumptions[0].searchQuery)
              .then(({data}) => {
                this.cards = data
              })
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .Results {
    margin-top: 24px;
    margin-bottom: 24px;
  }

  .Card {
    margin-bottom: 24px;
  }
</style>
