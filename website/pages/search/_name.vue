<script>
  import index from './index'

  export default {
    extends: index,
    fetch({$axios, route, params, store}) {
      const name = params.name

      switch (name) {
        case 'sg-halal':
          const query = {
            filter: {
              tag: {
                positives: ['halal']
              }
            }
          }
          store.dispatch('filter/start', query)
          return store.dispatch('search/start', query)
      }
    }
  }
</script>
