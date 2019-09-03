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
    const accountId = state.account.id
    if (accountId) {
      return MUNCH_TEAM.test(accountId.slice(0, 14))
    }
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
    type: 'error',
    timeout: 10000,
    title: 'Already Exist',
    message: 'Place is already inside the collection.'
  }
}

const parseError = (error) => {
  // Deprecated, remove when ready
  if (error?.meta?.error) {
    const metaError = error.meta.error

    const mapped = TYPE_MAPPING[metaError.type]
    if (mapped) return mapped

    const parts = metaError.type.split('\.')
    return {
      title: parts[parts.length - 1],
      message: metaError.message
    }
  }

  // When error is created from $api service.
  const exception = error?.error?.response?.data?.error || error?.response?.data?.error
  if (exception) {
    const {type, message} = exception

    // Attempt to map to know types
    const mapped = TYPE_MAPPING[type]
    if (mapped) return mapped

    const parts = type.split(/[/.]/)
    return {title: parts[parts.length - 1], message: message}
  }

  // When error contains status code (might be deprecated, need to check.)
  if (error?.statusCode === 404) {
    return {title: 'Not Found', message: 'Requested object cannot be found.'}
  }

  // When error is placed into {error: ...} object
  if (error?.error) {
    return {title: 'Unknown Error', message: error.error}
  }

  return {title: 'Unknown Error', message: error}
}

export const actions = {
  /**
   * This is only called on store/index.js. The current file you are at.
   */
  nuxtServerInit({commit}, {req}) {
    if (req.cookies.MunchAccount) {
      const account = JSON.parse(req.cookies.MunchAccount)
      this.commit('account/setAccount', account)
    }
  },

  /**
   * @param commit
   * @param state
   * @param error to add to the stack for display in notification
   */
  addError({commit, state}, error) {
    console.error(error)

    const type = error?.type || 'error'
    const timeout = error?.timeout || 12000
    return this.dispatch('addMessage', {type, timeout, ...parseError(error)})
  },

  /**
   *
   * @param commit
   * @param state
   * @param object = {type, title, message} or String
   */
  addMessage({commit, state}, object) {
    if (state.notifications.length > 30) {
      console.log('Too many concurrent notification. Not added')
      return
    }

    if (typeof object === 'string') {
      object = {type: 'message', message: object, timeout: 5000}
    }

    const {type, title, message, timeout} = object
    const id = uuidv4()

    commit('addNotification', {type: type || 'message', message, title, id})
    setTimeout(() => commit('removeNotification', {id}), timeout || 5000)
  }
}
