import _ from "lodash";
import uuidv4 from "uuid/v4";

const MUNCH_TEAM = /oNOfWjsL49giM0|sGtVZuFJwYhf5O|7onsywnak2SaVt|GoNd1yY0uVcA8p|CM8wAOSdenMD8d|41qibhP0VjR3qQ/g

export const state = () => ({
  origin: process.env.origin,
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

  /**
   * @returns {boolean} whether it is currently in production
   */
  isProduction: (state) => {
    return state.origin === 'https://www.munch.app'
  },

  /**
   * @returns {boolean} whether it is currently in not in production
   */
  isStaging: (state) => {
    return state.origin !== 'https://www.munch.app'
  },

  /**
   * ONLY to be used to test feature, no security is implemented for this
   *
   * @returns {boolean} whether currently logged in user is part of the munch team
   */
  isMunchTeam: (state) => {
    // state.user
    const userId = state.user.profile && state.user.profile.userId
    if (userId) return MUNCH_TEAM.test(userId.slice(0, 14))
  }
}

import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

export const mutations = {
  /**
   * Internal use only, use dispatch to add instead
   * @param state
   * @param notification to add
   */
  addNotification(state, notification) {
    state.notifications.push(notification)
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
      clearAllBodyScrollLocks()
    } else {
      state.focus.name = name
      state.focus.is = true
      disableBodyScroll(document.querySelector('body'))
    }
  },
  /**
   * clear focus
   * @param state
   */
  clearFocus(state) {
    state.focus.name = null
    state.focus.is = false
    clearAllBodyScrollLocks()
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
    }

    // Issues with Search bar navigation, might cascade into other bugs if I change this now
    // Fix in the future
    clearAllBodyScrollLocks()
  }
}

const TYPE_MAPPING = {
  'munch.api.user.ItemAlreadyExistInPlaceCollection': {
    title: 'Already Exist',
    message: 'Place is already inside the collection.'
  }
}

const parseError = (error) => {
  if (error && error.meta && error.meta.error) {
    const metaError = error.meta.error

    const mapped = TYPE_MAPPING[metaError.type]
    if (mapped) return mapped

    const parts = metaError.type.split('\.')
    return {
      title: parts[parts.length - 1],
      message: metaError.message
    }
  }
  
  if (error && error.statusCode === 404) {
    return {title: 'Error', message: 'Object Not Found'}
  }

  return {title: 'Unknown Error', message: error}
}

export const actions = {
  /**
   * @param commit
   * @param state
   * @param error to add to the stack for display in notification
   */
  addError({commit, state}, error) {
    console.error(error)
    if (state.notifications.length > 30) {
      console.log('Too many concurrent notification. Not added')
      return
    }

    const id = uuidv4()
    commit('addNotification', {type: 'error', id, ...parseError(error)})
    setTimeout(() => commit('removeNotification', {id}), 15000)
  },

  /**
   *
   * @param commit
   * @param state
   * @param type of message, allows overriding
   * @param title of message
   * @param message itself
   */
  addMessage({commit, state}, {type, title, message}) {
    if (state.notifications.length > 30) {
      console.log('Too many concurrent notification. Not added')
      return
    }

    const id = uuidv4()
    commit('addNotification', {type: type || 'message', message, title, id})
    setTimeout(() => commit('removeNotification', {id}), 6000)
  }
}
