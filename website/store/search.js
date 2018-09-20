export const state = () => ({
  query: null,
  page: 0,
  result: {cards: []},

  more: false,
  loading: false,
})

export const getters = {
  query: (state) => state.query,
  cards: (state) => state.result.cards,
  more: (state) => state.more,
  loading: (state) => state.loading,
}

export const mutations = {
  clear(state) {
    state.more = false
    state.loading = false
    state.query = null
    state.result.cards.splice(0, state.result.cards.length)
  },
  start(state, query) {
    state.more = true
    state.loading = true

    // TODO: Search Preference to be enforced

    state.query = query
    state.page = 0
    state.result.cards.splice(0, state.result.cards.length)
  },

  append(state, cards) {
    state.more = cards.length > 0
    state.loading = false
    state.page += 1
    state.result.cards.push(...cards)
  }
}

export const actions = {
  start({commit, state}, query) {
    commit('start', query)

    this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  },

  append({commit, state}) {
    if (state.loading) return

    this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  }
}
