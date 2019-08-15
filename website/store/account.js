import authenticator from '~/services/authenticator'
import * as Cookies from 'js-cookie'

export const state = () => ({
  account: null,
})

export const getters = {
  account: (state) => state.account || {},
  accountId: (state) => state.account && state.account.id,
  name: (state) => state.account && state.account.name,
  profile: (state) => state.account && state.account.profile,
  username: (state) => state.account && state.account.profile && state.account.profile.username,
  isLoggedIn: (state) => state.account && !!state.account.id,
}

export const mutations = {
  setAccount(state, account) {
    state.account = account

    // Setup $track userId if available
    if (account.id && this.$track) {
      this.$track.setUserId(account.id)
    }
  },

  clear(state) {
    state.account = null

    Cookies.remove('MunchAccount')

    if (this.$track) {
      this.$track.clearUserData()
    }
  },
}

function authenticate(commit) {
  return this.$api.post('/accounts/tokens/authenticate')
    .then(({data: account}) => {
      Cookies.set('MunchAccount', account)
      commit('setAccount', account)
    })
}

export const actions = {
  signInCustomToken({commit}, token) {
    return authenticator.signInCustomToken(token)
      .then(() => {
        return authenticate.call(this, commit)
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  signInFacebook({commit}) {
    return authenticator.signInFacebook()
      .then(() => {
        return authenticate.call(this, commit)
      }).catch(error => {
        return this.dispatch('addError', error)
      })
  },

  logout({commit}) {
    authenticator.signOut()

    this.commit('account/clear')
  },
}
