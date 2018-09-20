import _ from "underscore";
import uuidv4 from "uuid/v4";

export const state = () => ({
  loading: false,
  notifications: [],
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
   * Internal use only, use dispatch to add instead
   * @param state
   * @param type of notification
   * @param error to add into notification stack
   * @param id to track when to remove
   */
  addNotification(state, {type, error, id}) {
    state.notifications.push({type, error, id})
  },

  /**
   * Remove from notification stack
   * @param state
   * @param errorId to remove
   */
  removeNotification(state, {id}) {
    const index = _.findIndex(state.notifications, (n) => n.id === id)
    if (index !== -1) {
      state.notifications.splice(index, 1)
    }
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

export const actions = {
  /**
   * @param commit
   * @param state
   * @param error to add to the stack for display in notification
   */
  addError({commit, state}, error) {
    console.log(error)
    if (state.notifications.length > 30) {
      console.log('Too many concurrent error. Not added')
      return
    }

    const id = uuidv4()
    commit('addNotification', {type: 'error', error, id})
    setTimeout(() => commit('removeNotification', {id}), 5000)
  }
}
