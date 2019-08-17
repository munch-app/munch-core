import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

export const state = () => ({
  dialog: null
})

export const getters = {
  dialog: (state) => {
    return state.dialog
  },
  anyDialog: (state) => {
    return state.dialog != null
  }
}

export const mutations = {
  setDialog: (state, name) => {
    state.dialog = name
    disableBodyScroll(document.querySelector('.global-dialog'))
  },
  clearDialog: (state, name) => {
    state.dialog = null
    clearAllBodyScrollLocks()
  }
}
