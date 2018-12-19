const MAX_ITEMS = 500
import _ from 'lodash'

export const state = () => ({
  items: [],
  places: {},
  next: {from: null},
  loading: false,

  // Persistence of feed structure
  persistence: {lanes: [], cursor: 0}
})

export const getters = {
  items: (state) => state.items,
  more: (state) => !!state.next.from && state.items.length < MAX_ITEMS,
  getItem: (state) => (itemId) => _.find(state.items, item => item.itemId === itemId),
  getPlace: (state) => (placeId) => state.places[placeId],
  persistence: (state) => state.persistence
}

export const mutations = {
  loading(state) {
    state.loading = true
  },

  append(state, {items, places, next}) {
    state.items.push(...items)
    // Currently add one ad every 20 images because size = 20
    state.items.push({isAdSense: true})

    Object.keys(places).forEach(placeId => {
      state.places[placeId] = places[placeId]
    })
    state.next.from = next && next.from || null

    if (state.items.length > 25
      && !state.items[25].injected
      && !this.getters['user/isLoggedIn']) {
      state.items.splice(25, 0, {injected: true, type: 'activation:login'})
    }

    state.loading = false
  },

  persistence(state, persistence) {
    state.persistence = persistence
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
    return this.$axios.$get(`/api/feed/images`, {params})
      .then(({data, next}) => {
        commit('append', {items: data.items, places: data.places, next})
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
    if (state.next && state.next.from) {
      params['next.from'] = state.next.from
    }
    return this.$axios.$get(`/api/feed/images`, {params})
      .then(({data, next}) => {
        commit('append', {items: data.items, places: data.places, next})
      })
  }
}
