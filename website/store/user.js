export const state = () => ({
  user: null
})

export const getters = {
  isLoggedIn: (state) => !!state.user,
}

export const mutations = {}

export const actions = {}
