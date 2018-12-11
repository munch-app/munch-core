import _ from 'lodash'
import Vue from 'vue'

export const state = () => ({
  items: [],
  // null = NotStarted, undefined = no more
  next: null,
  loading: false,
})

export const getters = {
  items: (state) => state.items,
  more: (state) => state.next !== undefined,
  loading: (state) => state.loading,
  isSaved: (state) => (placeId) => {
    return _.some(state.items, s => s.placeId === placeId)
  }
}

export const mutations = {
  append(state, {items, next}) {
    state.items.push(...items)
    state.next = next ? next : undefined
    state.loading = false
  },
  restart(state) {
    state.items.splice(0)
    state.next = null
    state.loading = false
  },

  loading(state) {
    state.loading = true
  },
}

export const actions = {
  putPlace({commit, state}, {place: {placeId, name}}) {
    return this.$api.put(`/users/saved/places/${placeId}`)
      .then(() => {
        commit('restart')
        this.dispatch('addMessage', {
          title: 'Saved Places',
          message: `Added '${name}' to your places.`
        })
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  deletePlace({commit, state}, {place: {placeId, name}}) {
    return this.$api.delete(`/users/saved/places/${placeId}`)
      .then(() => {
        commit('restart')
        this.dispatch('addMessage', {
          title: 'Removed Places',
          message: `Removed '${name}' to your places.`
        })
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  append({commit, state}) {
    if (state.loading) return
    if (state.next === undefined) return
    commit('loading')

    const createdMillis = state.next && state.next.createdMillis
    return this.$api.get(`/users/saved/places`, {params: {createdMillis}})
      .then(({data, next}) => {
        commit('append', {items: data, next})
      })
  }
}
