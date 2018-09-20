import authenticator from '~/services/authenticator'

export const state = () => ({
  profile: null,
  setting: null,
})

// Future: You can fetch from JWT or Cookie and fill store before rendering
// So that user info will be loaded without using vuex-local-storage

export const getters = {
  isLoggedIn: (state) => !!state.profile,
  displayName: (state) => state.profile && state.profile.name,
}

export const mutations = {
  setUser(state, {profile, setting}) {
    state.profile = profile
    state.setting = setting
  },
  clear(state) {
    state.profile = null
    state.setting = null
  }
}

export const actions = {
  signInFacebook({commit}) {
    return authenticator.signInFacebook()
      .then(() => {
        return this.$axios.$post('/api/users/authenticate')
          .then(({data}) => {
            return data
          })
      })
      .then(({profile, setting}) => {
        return commit('setUser', {profile, setting})
      }).catch(error => {
        return this.store.dispatch('addError', error)
      })
  },

  logout({commit}) {
    authenticator.signOut()
    commit('clear')
  },

  patchSettingSearch({commit, state}) {
    // TODO future implementation
  }
}
