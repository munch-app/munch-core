export const state = () => ({
  loading: false,
  errors: [],
  focus: {
    name: null,
    is: false,
  }
})

export const getters = {

  /**
   * @param state
   * @returns {function(*): (*|boolean)} to check whether element is focused
   */
  isFocused: (state) => (name) => {
    return state.focus.is && state.focus.name === name
  },

  /**
   * @param state
   * @returns elevation is on
   */
  isElevated: (state) => {
    return state.focus.is
  },
}

import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

export const mutations = {
  /**
   * @param state
   * @param error to add for display
   */
  addError(state, error) {
    state.errors.push(error)
  },
  /**
   * @param state
   * @param name element to toggle focus between
   */
  toggleFocus(state, name) {
    if (state.focus.name === name && state.focus.is) {
      state.focus.is = false
      clearAllBodyScrollLocks(document.querySelector('body'))
    } else {
      state.focus.name = name
      state.focus.is = true
      disableBodyScroll(document.querySelector('body'))
    }
  },
  /**
   * @param state
   * @param name element to toggle focus on
   */
  focus(state, name) {
    state.focus.name = name
    state.focus.is = true
    disableBodyScroll(document.querySelector('body'))
  },
  /**
   * @param state
   * @param name element to toggle focus off
   */
  unfocus(state, name) {
    if (state.focus.name === name) {
      state.focus.is = false
      clearAllBodyScrollLocks(document.querySelector('body'))
    }
  }
}
