import * as Cookies from 'js-cookie'
import _ from "underscore";

export const state = () => ({
  query: {
    filter: {
      price: {},
      tag: {
        positives: []
      },
      hour: {},
      location: {
        areas: [],
        // Either: Nearby, Anywhere, Where, Between
        // Default to Anywhere unless user select otherwise
        type: 'Anywhere',
      },
    },
    sort: {}
  },
  user: {
    latLng: null
  },
  result: {
    count: null,
    tags: {},
    priceGraph: undefined,
  },
  startedLoading: null,
  loading: null, // true, false, null = not loaded before
  selected: null,
})

export const getters = {
  isSelectedTag: (state) => (tag) => {
    tag = tag.toLowerCase()
    const index = state.query.filter.tag.positives.indexOf(tag)
    return index > -1
  },

  isSelectedTiming: (state) => (timing) => {
    return state.query.filter.hour.name === timing
  },

  isSelectedLocationType: (state) => (location) => {
    // 4 possible types
    return state.query.filter.location.type === location
  },

  isSelectedLocationArea: (state) => (area) => {
    const index = _.findIndex(state.query.filter.location.areas, (a) => a.areaId === area.areaId)
    return index !== -1;
  },

  isSelectedPrice: (state) => (name) => {
    return state.query.filter.price && state.query.filter.price.name === name
  },
  loading: (state) => {
    return state.loading
  },
  count: (state) => {
    return state.result.count
  },
  selected: (state) => {
    return state.selected
  }
}

export const mutations = {
  selected(state, selected) {
    state.selected = selected
  },

  replace(state, query) {
    state.query.sort = query.sort

    state.query.filter.price = query.filter.price && query.filter.price.min && query.filter.price.max && query.filter.price || {}
    state.query.filter.hour = query.filter.hour || {}
    state.query.filter.location = query.filter.location || {areas: [], type: 'Anywhere'}

    state.query.filter.tag.positives = query.filter.tag.positives
  },

  /**
   * @param state
   * @param latLng update user current latLng
   */
  updateLatLng(state, latLng) {
    state.user.latLng = latLng
    // Expire in 30 minutes
    Cookies.set('UserLatLng', latLng, {expires: 1.0 / 48.0})
  },

  putTag(state, tag) {
    tag = tag.toLowerCase()
    const index = state.query.filter.tag.positives.indexOf(tag)
    if (index === -1) {
      state.query.filter.tag.positives.push(tag)
    }
  },

  /**
   * @param state
   * @param tag to toggle in search bar
   */
  toggleTag(state, tag) {
    tag = tag.toLowerCase()
    const index = state.query.filter.tag.positives.indexOf(tag)
    if (index === -1) {
      state.query.filter.tag.positives.push(tag)
    } else {
      state.query.filter.tag.positives.splice(index, 1)
    }
  },

  /**
   * @param state
   * @param timing tag to toggle in search bar
   */
  toggleTiming(state, timing) {
    if (state.query.filter.hour.name === timing) {
      state.query.filter.hour = {}
    } else if (timing === 'Open Now') {
      const date = new Date()
      const day = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'][date.getDay()]
      const hours = date.getHours() < 10 ? `0${date.getHours()}` : `${date.getHours()}`
      const minutes = date.getMinutes() < 10 ? `0${date.getMinutes()}` : `${date.getMinutes()}`
      const close = hours === '23'
        ? '23:59' : minutes < 30
          ? `${hours}:${minutes + 30}`
          : `${hours + 1}:${minutes - 30}`

      state.query.filter.hour = {
        name: 'Open Now',
        day: day,
        open: `${hours}:${minutes}`,
        close: close
      }
    } else {
      state.query.filter.hour = {name: timing}
    }
  },

  /**
   *
   * @param state
   * @param type to update to
   * @param areas areas to replace, for consistency, updater should concat and push to mutation
   */
  updateLocation(state, {type, areas}) {
    switch (type) {
      case 'Nearby':
        if (state.user.latLng) {
          state.query.filter.location.type = type
        }
        return

      case 'Anywhere':
        state.query.filter.location.type = 'Anywhere'
        return

      case 'Where':
        state.query.filter.location.areas.splice(0, state.query.filter.location.areas)
        state.query.filter.location.areas.push(...areas)
        state.query.filter.location.type = 'Where'
        return

      case 'Between':
        state.query.filter.location.areas.splice(0, state.query.filter.location.areas)
        state.query.filter.location.areas.push(...areas)
        state.query.filter.location.type = 'Between'
        return
    }
  },

  /**
   * @param state
   * @param tags to remove if any
   */
  clear(state, {tags}) {
    switch (state.selected) {
      case 'price':
        state.query.filter.price = {}
        break

      case 'cuisine':
      case 'amenities':
      case 'establishments':
        tags.forEach(function (tag) {
          tag = tag.toLowerCase()
          const index = state.query.filter.tag.positives.indexOf(tag)
          if (index !== -1) {
            state.query.filter.tag.positives.splice(index, 1)
          }
        })
        break

      case 'location':
        state.query.filter.location.type = 'Anywhere'
        break

      case 'timings':
        state.query.filter.hour = {}
        break
    }
  },

  reset(state) {
    state.query.filter.price = {}
    state.query.filter.location.type = 'Anywhere'
    state.query.filter.hour = {}
    state.query.filter.tag.positives.splice(0, state.query.filter.tag.positives.length)

    state.loading = false
  },

  updatePrice(state, {name, min, max}) {
    if (state.query.filter.price.name === name) {
      state.query.filter.price = {}
    } else {
      state.query.filter.price = {name, min, max}
    }
  },

  /**
   * Update result from result of /search/filter
   * @param state
   * @param result to update
   */
  result(state, result) {
    state.result.count = result && result.count
    state.result.tags = result && result.tags
    state.result.priceGraph = result && result.priceGraph
  },

  /**
   * Update whether search is loading
   * @param state
   * @param loading
   */
  loading(state, loading) {
    state.loading = loading
    state.startedLoading = new Date()
  },
}

function post(commit, state) {
  return this.$axios.$post('/api/search/filter', state.query, {progress: false})
    .then(({data}) => {
      commit('result', data)

      const timeout = (state.startedLoading.getTime() + 800) - new Date().getTime()
      setTimeout(() => commit('loading', false), timeout)
    })
    .catch(error => {
      commit('loading', false)
      return this.commit('addError', error)
    })
}

export const actions = {
  start({commit, state}, query) {
    if (state.loading !== null) return
    commit('loading', true)
    if (query) commit('replace', query)

    // Search Preference Injection
    const injections = this.getters['user/searchPreferenceTags']
    injections.forEach(tag => commit('putTag', tag))
    return post.call(this, commit, state)
  },

  clear({commit, state}, payload) {
    commit('loading', true)
    commit('clear', payload)
    return post.call(this, commit, state)
  },

  reset({commit, state}) {
    commit('loading', true)
    commit('reset')

    // Search Preference Injection
    const injections = this.getters['user/searchPreferenceTags']
    injections.forEach(tag => commit('putTag', tag))
    return post.call(this, commit, state)
  },

  location({commit, state}, location) {
    if (state.loading) return // Don't commit any changes if is already loading

    commit('loading', true)
    commit('updateLocation', location)
    return post.call(this, commit, state)
  },

  price({commit, state}, object) {
    if (state.loading) return // Don't commit any changes if is already loading

    commit('loading', true)
    commit('updatePrice', object)
    return post.call(this, commit, state)
  },

  timings({commit, state}, timing) {
    if (state.loading) return // Don't commit any changes if is already loading

    commit('loading', true)
    commit('toggleTiming', timing)
    return post.call(this, commit, state)
  },

  tag({commit, state}, tag) {
    if (state.loading) return // Don't commit any changes if is already loading

    commit('loading', true)
    commit('toggleTag', tag)
    return post.call(this, commit, state)
  },
}
