import _ from 'underscore'

export const state = () => ({
  seo: {}, // For seo purpose
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

  setSeo(state, {name, description, keywords}) {
    state.seo = {name, description, keywords}
  },

  start(state, query) {
    // Search Preference Injection
    if (!query.filter) query.filter = {}
    if (!query.filter.tag) query.filter.tag = {}
    if (!query.filter.tag.positives) query.filter.tag.positives = []

    this.getters['user/searchPreferenceTags'].forEach(tag => {
      const index = query.filter.tag.positives.indexOf(tag.toLowerCase())
      if (index === -1) query.filter.tag.positives.push(tag)
    })

    state.more = true
    state.loading = true

    state.seo = {}
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

/**
 * 3 ways to start searching
 *
 */
export const actions = {
  start({commit, state}, query) {
    commit('start', query)
    this.commit('filter/replace', query)

    return this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data, qid}) => {
        commit('append', data)

        this.$router.replace({path: '/search', query: {qid}})
      })
  },

  startNamed({commit, state}, named) {
    return this.$axios.$get(`/api/search/named/${named}`)
      .then(({data}) => {
        if (!data) return Promise.reject(new Error('Not Found'))

        const query = data.searchQuery
        commit('start', query)
        commit('setSeo', {name: data.name, description: data.description, keywords: data.keywords})
        this.commit('filter/replace', query)

        return this.$axios.$post(`/api/search?page=${state.page}`, query)
          .then(({data}) => {
            commit('append', data)
          })
      }).catch(error => {
        return this.$router.replace({path: '/search', query: {}})
      })
  },

  startQid({commit, state}, qid) {
    return this.$axios.$get(`/api/search/qid/${qid}`)
      .then(({data}) => {
        if (!data) return Promise.reject(new Error('Not Found'))

        commit('start', data)
        this.commit('filter/replace', data)

        return this.$axios.$post(`/api/search?page=${state.page}`, data)
          .then(({data}) => {
            commit('append', data)
          })
      }).catch(error => {
        return this.$router.replace({path: '/search', query: {}})
      })
  },

  append({commit, state}) {
    if (state.loading) return

    return this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  }
}
