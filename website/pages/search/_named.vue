<script>
  import index from './index'

  export default {
    extends: index,
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: this.title || 'Search · Munch Singapore',
          description: this.description,
          url: `https://www.munch.app/search/${this.name}`,
          keywords: this.keywords,
        },
        breadcrumbs: [
          {
            name: 'Search',
            item: 'https://www.munch.app/search'
          },
          {
            name: this.title,
            item: `https://www.munch.app/search/${this.name}`
          },
        ]
      })
    },
    asyncData({store, $axios, params, error}) {
      const named = params.named
      return $axios.$get(`/api/search/named/${named}`).then(({data}) => {
        return store.dispatch('search/startNamed', data.searchQuery).then(() => {
          return {
            name: data.name,
            title: data.title,
            description: data.description,
            keywords: data.keywords
          }
        })
      }).catch(({message, response}) => {
        if (response && response.status === 404) {
          error({statusCode: 404, message: 'Search Not Found'})
        } else {
          error({statusCode: 500, message: message})
        }
      })
    },
  }
</script>
