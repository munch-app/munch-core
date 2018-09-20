import _ from 'underscore'

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
    // Search Preference Injection
    const injections = this.getters['user/searchPreferenceTags']
    if (query && query.filters && query.filter.tag && query.filter.tag.positives) {
      query.filter.tag.positives = _.union(query.filter.tag.positives, injections)
    } else {
      if (!query.filter) query.filter = {}
      if (!query.filter.tag) query.filter.tag = {}
      query.filter.tag.positives = injections
    }

    state.more = true
    state.loading = true

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
