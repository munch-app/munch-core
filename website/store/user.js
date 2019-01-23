import authenticator from '~/services/authenticator'
import * as Cookies from 'js-cookie'

export const state = () => ({
  profile: null,
  searchPreference: null
})

export const getters = {
  profile: (state) => state.profile || {},
  userId: (state) => state.profile && state.profile.userId,
  isLoggedIn: (state) => state.profile && !!state.profile.name,
  displayName: (state) => state.profile && state.profile.name,
  searchPreference: (state) => state.searchPreference || {requirements: []}
}

export const mutations = {
  setUser(state, {profile}) {
    state.profile = profile
  },

  setSearchPreference(state, searchPreference) {
    state.searchPreference = searchPreference
  },

  clear(state) {
    state.profile = null
    state.searchPreference = null
  },
}

export const actions = {
  signInCustomToken({commit}, token) {
    return authenticator.signInCustomToken(token)
      .then(() => {
        return this.$api.post('/users/authenticate')
          .then(({data: {profile, searchPreference}}) => {
            Cookies.set('MunchUser', {profile})
            commit('setUser', {profile})
            commit('setSearchPreference', searchPreference)
          })
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  signInFacebook({commit}) {
    return authenticator.signInFacebook()
      .then(() => {
        return this.$api.post('/users/authenticate')
          .then(({data: {profile, searchPreference}}) => {
            Cookies.set('MunchUser', {profile})
            commit('setUser', {profile})
            commit('setSearchPreference', searchPreference)
          })
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  updateSearchPreference({commit}, searchPreference) {
    commit('setSearchPreference', searchPreference)

    return this.$api.put('/users/search/preference', searchPreference)
      .then(() => {
        this.dispatch('addMessage', {message: 'Updated Search Preference.'})
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  logout({commit}) {
    authenticator.signOut()
    Cookies.remove('MunchUser')
    commit('clear')
  },
}
