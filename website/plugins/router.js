export default function ({app, store}) {
  if (process.client) {
    app.router.afterEach((to, from) => {
      // Route going to 'search' with focused: 'Suggest'
      if (to.name === 'search' && store.getters['isFocused']('Suggest')) return

      // Exist always clear focus
      store.commit('clearFocus')
    })
  }
}
