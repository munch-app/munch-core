const ANYWHERE = {
  areaId: 'singapore',
  type: 'City',
  name: 'Singapore',
  images: [],
  hour: [],
  location: {
    city: 'Singapore',
    country: 'SGP',
    latLng: '1.290270, 103.851959',
    polygon: {
      points: ["1.26675774823,103.603134155", "1.32442122318,103.617553711", "1.38963424766,103.653259277", "1.41434608581,103.666305542", "1.42944763543,103.671798706", "1.43905766081,103.682785034", "1.44386265833,103.695831299", "1.45896401284,103.720550537", "1.45827758983,103.737716675", "1.44935407163,103.754196167", "1.45004049736,103.760375977", "1.47887018872,103.803634644", "1.4754381021,103.826980591", "1.45827758983,103.86680603", "1.43219336108,103.892211914", "1.4287612035,103.897018433", "1.42670190649,103.915557861", "1.43219336108,103.934783936", "1.42189687297,103.960189819", "1.42464260763,103.985595703", "1.42121043879,104.000701904", "1.43974408965,104.02130127", "1.44592193988,104.043960571", "1.42464260763,104.087219238", "1.39718511473,104.094772339", "1.35737118164,104.081039429", "1.29009788407,104.127044678", "1.277741368,104.127044678", "1.25371463932,103.982162476", "1.17545464492,103.812561035", "1.13014521522,103.736343384", "1.19055762617,103.653945923", "1.1960495989,103.565368652", "1.26675774823,103.603134155"]
    }
  }
}

export const state = () => ({
  query: {
    filter: {
      price: {},
      tag: {
        positives: []
      },
      hour: {},
      area: null,
    },
    sort: {}
  },
  user: {
    latLng: null
  },
  count: null,
  tags: {},
  frequency: {},
  percentiles: []
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
  },

  isSelectedLocation: (state) => (location) => {
    if (location === 'Nearby') {
      return state.user.latLng && state.query.filter.area === null
    }

    if (location === 'Anywhere') {
      if (!state.user.latLng && state.query.filter.area === null) {
        return true
      }else {
        return state.query.filter.area && state.query.filter.area.areaId === ANYWHERE.areaId
      }
    }
    return state.query.filter.area && state.query.filter.area.areaId === location.areaId
  }
}

export const mutations = {
  update(state, query) {
    state.query = query
  },

  updateLatLng(state, latLng) {
    state.user.latLng = latLng
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
      state.query.filter.hour = {}
    } else {
      state.query.filter.hour = {name: timing}
    }
  },

  /**
   * @param state
   * @param location to replace or remove
   */
  toggleLocation(state, location) {
    if (location === 'Nearby') {
      // Might need to do a polygon due how inaccuracy of data
      state.query.filter.area = null
    } else if (location === 'Anywhere') {
      state.query.filter.area = ANYWHERE
    } else {
      state.query.filter.area = location
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
