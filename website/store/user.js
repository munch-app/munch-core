export const state = () => ({
  user: null
})

export const getters = {
  isLoggedIn: (state) => !!state.user,
  displayName: (state) => state.user && status.user.name,
}

export const mutations = {}

export const actions = {}
