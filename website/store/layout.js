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

import {disableBodyScroll, enableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

export const mutations = {
  showMenu(state, menu) {
    state.menu = menu

    if (state.menu) {
      this.commit('layout/elevationOn', 'headerMenu')
    } else {
      this.commit('layout/elevationOff', 'headerMenu')
    }
  },

  toggleMenu(state) {
    this.commit('layout/showMenu', !state.menu)
  },

  /**
   * @param state
   * @param name that triggered overlay on
   */
  elevationOn(state, name) {
    state.elevation.name = name
    state.elevation.elevated = true
    disableBodyScroll(document.querySelector('body'))
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
      enableBodyScroll(document.querySelector('body'));
    }
  }
}
