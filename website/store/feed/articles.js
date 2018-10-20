const MAX_ITEMS = 500

export const state = () => ({
  items: [],
  places: {},
  next: {sort: null},
  loading: false,
})

export const getters = {
  items: (state) => state.items,
  more: (state) => !!state.next.sort && state.items.length < MAX_ITEMS,
  getPlace: (state) => (placeId) => state.places[placeId]
}

export const mutations = {
  loading(state) {
    state.loading = true
  },

  append(state, {places, data, next}) {
    state.items.push(...data)
    Object.keys(places).forEach(placeId => {
      state.places[placeId] = places[placeId]
    })
    state.next.sort = next.sort

    state.loading = false
  }
}

export const actions = {
  start({commit, state}) {
    if (state.items.length > 0) return
    commit('loading')

    const params = {
      'country': 'sgp',
      'latLng': '1.3521,103.8198',
      'size': 20
    }
    return this.$axios.$get(`/api/feed/articles`, {params})
      .then(({data, places, next}) => {
        commit('append', {data, places, next})
      })
  },

  append({commit, state}) {
    if (state.loading) return
    if (state.items.length >= MAX_ITEMS) return
    commit('loading')

    const params = {
      'country': 'sgp',
      'latLng': '1.3521,103.8198',
      'size': 20,
    }
    if (state.next && state.next.sort) {
      params['next.sort'] = state.next.sort
    }
    return this.$axios.$get(`/api/feed/articles`, {params})
      .then(({data, places, next}) => {
        commit('append', {data, places, next})
      })
  }
}
