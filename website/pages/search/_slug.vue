<script>
  import index from './index'

  export default {
    extends: index,
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: this.title || 'Search · Munch - Food Discovery',
          description: this.description,
          url: `https://www.munch.app/search/${this.slug}`,
        },
        breadcrumbs: [
          {
            name: 'Search',
            item: 'https://www.munch.app/search'
          },
          {
            name: this.title,
            item: `https://www.munch.app/search/${this.slug}`
          },
        ]
      })
    },
    asyncData({store, $api, params: {slug}, error}) {
      return $api.get(`/search/named/${slug}`)
        .then(({data: {title, description, areas, tags}}) => {
          const searchQuery = {
            filter: {
              tags,
              location: {type: 'Where', areas, points: []}
            }
          }

          return store.dispatch('search/startNamed', searchQuery).then(() => {
            return {
              slug, title, description,
            }
          })
        }).catch((err) => {
          if (err.statusCode === 404) error({statusCode: 404, message: 'Search Not Found'})
          else if (err.statusCode) error(err)
        })
    },
  }
</script>
