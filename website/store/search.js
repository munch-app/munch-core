export const state = () => ({
  text: '',
  query: {
    filter: {},
    sort: {}
  }
})

export const mutations = {
  update(state, query) {
    state.query = query
  }
}
