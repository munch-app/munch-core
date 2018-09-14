export const state = () => ({
  menu: false,
  elevation: {
    name: null,
    elevated: false
  }
})


export const getters = {
  isElevated: (state) => state.elevation.elevated,
}

export const mutations = {
  showMenu(state, menu) {
    state.menu = menu
  },

  toggleMenu(state) {
    state.menu = !state.menu
  },

  /**
   * @param state
   * @param name that triggered overlay on
   */
  elevationOn(state, name) {
    state.elevation.name = name
    state.elevation.elevated = true
  },

  /**
   * You can't turn it off unless you turn it on
   * @param state
   * @param name that triggered overlay off
   */
  elevationOff(state, name) {
    if (state.elevation.elevated && state.elevation.name === name) {
      state.elevation.name = name
      state.elevation.elevated = false
    }
  }
}
