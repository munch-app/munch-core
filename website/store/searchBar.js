export const state = () => ({
  query: {
    filter: {
      price: {},
      tag: {
        positives: []
      },
      hour: {},
      area: {},
    },
    sort: {}
  },
  count: null,
  tags: {},
  frequency: {},
  percentiles: [] // {percent, price}
})

export const getters = {
  isSelectedTag: (state) => (tag) => {
    tag = tag.toLowerCase()
    const index = state.query.filter.tag.positives.indexOf(tag)
    return index > -1
  },

  isSelectedTiming: (state) => (timing) => {
    timing = timing.toLowerCase()
    return state.query.filter.hour.name === timing
  }
}

export const mutations = {
  update(state, query) {
    state.query = query
  },

  /**
   * Toggle tag in search bar search query
   * @param state
   * @param tag to toggle
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
   * Toggle hour tag in search bar search query
   * @param state
   * @param timing tag to toggle
   */
  toggleTiming(state, timing) {
    timing = timing.toLowerCase()
    if (state.query.filter.hour.name === timing) {
      state.query.filter.hour.name = null
    } else {
      state.query.filter.hour.name = timing
    }
  },

  /**
   * Update count data from result of /search/filter/count
   * @param state
   * @param data to update
   */
  updateCount(state, data) {
    state.count = data.count
    state.tags = data.tags
  },

  /**
   * Update price data from result of /search/filter/price
   * @param state
   * @param data to update
   */
  updatePrice(state, data) {
    state.frequency = data.frequency
    state.percentiles = data.percentiles
  }
}
