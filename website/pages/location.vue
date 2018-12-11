<script>
  import search from './search/index'

  export default {
    extends: search,
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          image: 'https://s3-ap-southeast-1.amazonaws.com/munch-static/Munch+Logo.png',
          url: `https://www.munch.app/location`,
        }
      })
    },
    asyncData({store}) {
      return store.dispatch('search/feature', {
        feature: 'Location',

        // Technically filter, sort ignored, but present to respect SearchQuery constraints
        filter: {
          location: {type: 'Anywhere', points: [], areas: []},
          tags: []
        },
        sort: {}
      })
    },
  }
</script>
