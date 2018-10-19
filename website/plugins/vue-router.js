export default function ({app, store, $gtag}) {
  if (process.client) {
    app.router.afterEach((to, from) => {
      // Route going to 'search' with focused: 'Suggest'
      if (to.name === 'search' && store.getters['isFocused']('Suggest')) return

      // Exist always clear focus
      store.commit('clearFocus')
    })

    app.router.afterEach((to, from) => {
      if (to.name !== 'places-placeId') return

      const additional = {}

      switch (from.name) {
        case 'search':
          additional['from_action'] = 'search'
          const search = store.state.search
          additional['from_category'] = (search.type || 'search').toLowerCase()
          const locationType = search.query && search.query.filter && search.query.filter.location && search.query.filter.location.type || ''
          additional['from_search_location_type'] = locationType.toLowerCase()
      }

      app.$gtag('event', 'view', {
        'event_category': 'rip',
        ...additional
      })
    })
  }
}
