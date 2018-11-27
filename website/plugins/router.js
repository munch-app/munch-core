export default function ({app, store}) {
  if (process.client) {
    app.router.afterEach((to, from) => {

      // Exist always clear focus
      store.commit('clearFocus')
    })
  }
}
