<script>
  import search from './search/index'

  export default {
    extends: search,
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          url: `https://www.munch.app/`,
        }
      })
    },
    asyncData({store}) {
      return store.dispatch('search/feature', {feature: 'Home'})
    },
  }
</script>
