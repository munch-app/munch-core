const minWidth = 320

export const state = () => ({
  lanes: [{
    items: [],
    requested: false
  }],
  cursor: 0,
  data: [],
  next: {sort: null},
  loading: false,
})

export const getters = {
  lanes: (state) => state.lanes,
  more: (state) => !!state.next.sort,
}

export const mutations = {
  redraw(state, {count}) {
    state.lanes.splice(0, state.lanes.length)

    for (let i = 0; i < count; i++) {
      state.lanes.push({
        items: [],
        requested: true
      })
    }

    state.cursor = 0
  },

  loading(state) {
    state.loading = true
  },

  request(state, {index}) {
    state.loading = true
    if (state.lanes[index]) {
      state.lanes[index].requested = true
    }
  },

  append(state, {data, next}) {
    state.loading = false
    state.next = next
    state.data.push(...data)
  },

  fill(state) {
    state.lanes.forEach(lane => {
      const item = state.data[state.cursor]
      if (lane.requested && item) {
        lane.items.push(item)
        state.cursor = state.cursor + 1
      }
    })
  }
}

export const actions = {
  start({commit, state}, width) {
    const count = Math.floor(width / minWidth) || 1
    commit('redraw', {count})
    commit('loading')
    return this.$axios.$get(`/api/feed`)
      .then(({data, next}) => {
        commit('append', {data, next})

        for (let i = 0; i < 5; i++) {
          for (let i = 0; i < count; i++) {
            commit('request', {index: i})
            commit('fill')
          }
        }
      })
  },

  redraw({commit, state}, width) {
    const count = Math.floor(width / minWidth) || 1
    if (state.lanes.length === count) return

    commit('redraw', {count})
    commit('fill')
  },

  append({commit, state}, index) {
    commit('request', {index})
    commit('fill')

    if (state.cursor < state.data.length) return
    if (state.loading) return

    commit('loading')
    return this.$axios.$get(`/api/feed?next.sort=${state.next.sort}`)
      .then(({data, next}) => {
        commit('append', {data, next})
        commit('fill')
      })
  }
}
