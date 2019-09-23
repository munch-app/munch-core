import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

export const state = () => ({
  dialog: null
})

export const getters = {
  dialogName: (state) => {
    return state.dialog?.name
  },
  dialogProps: (state) => {
    return state.dialog?.props
  },
  anyDialog: (state) => {
    return state.dialog != null
  }
}

export const mutations = {
  setDialog: (state, object) => {
    if (typeof object === 'string') {
      state.dialog = {name: object}
    } else {
      state.dialog = {name: object.name, props: object.props}
    }

    disableBodyScroll(document.querySelector('.GlobalDialog > *'))
  },
  clearDialog: (state) => {
    state.dialog = null
    clearAllBodyScrollLocks()
  }
}
