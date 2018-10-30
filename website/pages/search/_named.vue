<script>
  import index from './index'

  export default {
    extends: index,
    head() {
      const meta = []
      meta.push({name: 'robots', content: 'follow,index'})

      if (this.description) {
        meta.push({hid: 'description', name: 'description', content: this.description})
      }
      if (this.keywords) {
        meta.push({name: 'keywords', content: this.keywords})
      }
      return {title: this.title || 'Search · Munch', meta}
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
