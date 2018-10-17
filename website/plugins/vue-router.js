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

      let label = undefined

      switch (from.name) {
        case 'search':
          const search = store.state.search
          const type = search.type || ''
          const locationType = search.query && search.query.filter && search.query.filter.location && search.query.filter.location.type || ''
          label = `${type.toLowerCase()}-${locationType.toLowerCase()}`

      }

      app.$gtag('event', 'view', {
        'event_category': 'rip',
        'event_label': label
      })
    })
  }
}
