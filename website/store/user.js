import authenticator from '~/services/authenticator'

export const state = () => ({
  profile: null,
  setting: null,
})

// Future: You can fetch from JWT or Cookie and fill store before rendering
// So that user info will be loaded without using vuex-local-storage

export const getters = {
  profile: (state) => state.profile || {},
  userId: (state) => state.profile && state.profile.userId,
  isLoggedIn: (state) => !!state.profile,
  displayName: (state) => state.profile && state.profile.name,
  isSearchPreference: (state) => (tag) => {
    return state.setting.search.tags.includes(tag.toLowerCase())
  },
  searchPreferenceTags: (state) => {
    return state.setting && state.setting.search && state.setting.search.tags
  }
}

export const mutations = {
  setUser(state, {profile, setting}) {
    state.profile = profile
    state.setting = setting
  },
  clear(state) {
    state.profile = null
    state.setting = null
  },
  toggleSettingSearchTag(state, tag) {
    tag = tag.toLowerCase()
    const index = state.setting.search.tags.indexOf(tag)
    if (index === -1) {
      state.setting.search.tags.push(tag)
    } else {
      state.setting.search.tags.splice(index, 1)
    }
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

  toggleSettingSearchTag({commit, state}, tag) {
    commit('toggleSettingSearchTag', tag)

    return this.$axios.$patch('/api/users/setting/search', state.setting.search)
      .then(({data}) => {
        return data
      })
  }
}
