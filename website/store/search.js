export const state = () => ({
  query: null,
  from: 0,
  size: 30,
  result: {cards: []},

  more: false,
  loading: false,
})

export const getters = {
  query: (state) => state.query,
  cards: (state) => state.result.cards,
  more: (state) => state.more
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

    state.query = query
    state.from = 0
    state.size = 30
    state.result.cards.splice(0, state.result.cards.length)
  },

  append(state, cards) {
    state.more = cards.length > 0
    state.loading = false

    state.from += state.size
    state.result.cards.push(...cards)
  }
}

export const actions = {
  start({commit, state}, query) {
    commit('start', query)

    this.$axios.$post(`/api/search?from=${state.from}&size=${state.size}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  },

  append({commit, state}) {
    if (state.loading) return

    this.$axios.$post(`/api/search?from=${state.from}&size=${state.size}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  }
}
