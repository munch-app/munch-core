export const state = () => ({
  type: null,
  query: null,
  page: 0,
  result: {cards: []},

  more: false,
  loading: false,
  showsMap: false
})

export const getters = {
  query: (state) => state.query,
  type: (state) => state.type,
  cards: (state) => state.result.cards,
  more: (state) => state.more,
  loading: (state) => state.loading,
  showsMap: (state) => state.showsMap,

  /**
   * Used for watching for search event, not properly implemented but a hack that works now
   */
  searched: (state) => state.result.cards,
}

export const mutations = {
  clear(state) {
    state.more = false
    state.loading = false
    state.query = null
    state.result.cards.splice(0)
  },

  start(state, {query, type}) {
    if (!query.filter) query.filter = {}
    if (!query.filter.tag) query.filter.tag = {}
    if (!query.filter.tag.positives) query.filter.tag.positives = []

    this.getters['user/searchPreferenceTags'].forEach(tag => {
      const index = query.filter.tag.positives.indexOf(tag.toLowerCase())
      if (index === -1) query.filter.tag.positives.push(tag)
    })

    state.type = type
    state.query = query

    state.more = true
    state.loading = true
    state.showsMap = false

    state.page = 0
    state.result.cards.splice(0)

    if (query.filter.location.type === 'Between') {
      state.showsMap = true
      // Between only loads once
      state.loading = false
      state.more = false
    }
  },

  append(state, cards) {
    state.more = cards.length > 0
    state.loading = false
    state.page += 1
    state.result.cards.push(...cards)
  }
}

function start({query, type}) {
  this.commit('search/start', {query, type})
  this.commit('filter/replace', query)
  this.dispatch('search/gtag')
}

/**
 * 3 ways to start searching
 *
 */
export const actions = {
  start({commit, state}, query) {
    if (window) window.scrollTo(0, 0)

    this.$router.replace({path: '/search'})

    start.call(this, {query, type: 'search'})
    return this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data, qid}) => {
        commit('append', data)

        this.$router.replace({path: '/search', query: {qid}})
      })
  },

  startNamed({commit, state}, query) {
    start.call(this, {query, type: 'named'})
    return this.$axios.$post(`/api/search?page=${state.page}`, state.query)
      .then(({data}) => {
        commit('append', data)
      })
  },

  startQid({commit, state}, qid) {
    return this.$axios.$get(`/api/search/qid/${qid}`)
      .then(({data}) => {
        if (!data) return Promise.reject(new Error('Not Found'))

        start.call(this, {query: data, type: 'qid'})
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
  },

  gtag({commit, state}) {
    const type = state.type
    const query = state.query

    if (type == null) return
    if (!this.$gtag) return

    const parameters = {}
    parameters['event_category'] = (type || 'search').toLowerCase()

    const locationType = query && query.filter && query.filter.location && query.filter.location.type || ''
    parameters['event_label'] = `search_${locationType.toLowerCase()}`

    this.$gtag('event', 'search', parameters)
  }
}
