<script>
  import search from '../search/index'

  export default {
    extends: search,
    head() {
      const {collectionId} = this.$route.params

      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          url: `https://www.munch.app/collections/${collectionId}`,
        }
      })
    },
    asyncData({store, params: {collectionId}}) {
      return store.dispatch('search/feature', {
        feature: 'Collection',
        collection: {collectionId}
      })
    },
  }
</script>
