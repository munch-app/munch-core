import _ from 'underscore'
import Vue from 'vue'

export const state = () => ({
  list: [],
  next: null,
  loading: false,
})

export const getters = {
  list: (state) => state.list,
  more: (state) => state.next === null || !!state.next,
  loading: (state) => state.loading,
}

export const mutations = {
  put(state, collection) {
    const index = _.findIndex(state.list, (c) => c.collectionId === collection.collectionId)
    if (index !== -1) {
      Vue.set(state.list, index, collection)
    } else {
      state.list.push(collection)
    }
  },

  delete(state, collectionId) {
    const index = _.findIndex(state.list, (c) => c.collectionId === collectionId)
    if (index !== -1) {
      state.list.splice(index, 1)
    }
  },

  append(state, {collections, next}) {
    state.list.push(...collections)
    state.next = next
    state.loading = false
  },

  removeAll(state) {
    state.list.splice(0, state.list.length)
  },

  restart(state) {
    state.list.splice(0, state.list.length)
    state.next = null
    state.loading = false
  }
}

export const actions = {
  putItem({commit, state}, {collectionId, placeId}) {
    return this.$axios.$put(`/api/users/places/collections/${collectionId}/items/${placeId}`)
      .then(() => {
        commit('restart')
      })
  },

  deleteItem({commit, state}, {collectionId, placeId}) {
    return this.$axios.$delete(`/api/users/places/collections/${collectionId}/items/${placeId}`)
      .then(() => {
        commit('restart')
      })
  },

  post({commit, state}, collection) {
    return this.$axios.$post(`/api/users/places/collections`, collection)
      .then(({data}) => {
        commit('put', data)
      })
  },

  patch({commit, state}, {collectionId, name, description}) {
    return this.$axios.$patch(`/api/users/places/collections/${collectionId}`, {
      name, description
    }).then(({data}) => {
      commit('put', data)
    })
  },

  delete({commit, state}, collectionId) {
    return this.$axios.$delete(`/api/users/places/collections/${collectionId}`)
      .then(({data}) => {
        commit('delete', data.collectionId)
      })
  },

  load({commit, state}, reload) {
    if (state.loading) return
    if (reload) commit('removeAll')

    const params = state.next && state.next.sort && {'next.sort': state.next.sort} || {}
    return this.$axios.$get(`/api/users/places/collections`, {params})
      .then(({data, next}) => {
        commit('append', {collections: data, next})
      })
  }
}
