import * as Cookies from 'js-cookie'

export const state = () => ({
  profile: null,
})

export const getters = {
  creatorId: (state) => state.profile && state.profile.creatorId,
  creatorProfile: (state) => state.profile || {},
  creatorName: (state) => state.profile && state.profile.name,
  creatorBody: (state) => state.profile && state.profile.body,
  creatorImage: (state) => state.profile && state.profile.image,
}

export const mutations = {
  setCreator(state, {profile}) {
    state.profile = profile
    Cookies.set('MunchCreator', {profile})
  },
  clear(state) {
    state.profile = null
    Cookies.remove('MunchCreator')
  }
}

export const actions = {}
