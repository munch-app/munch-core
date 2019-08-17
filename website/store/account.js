import authenticator from '~/services/authenticator'
import * as Cookies from 'js-cookie'

export const state = () => ({
  id: null,
  email: null,
  profile: null,
})

export const getters = {
  id: (state) => state.id,
  name: (state) => state.profile?.name,
  profile: (state) => state.profile,
  username: (state) => state.profile?.username,
  isLoggedIn: (state) => !!state.id,
}

export const mutations = {
  setAccount(state, account) {
    Cookies.set('MunchAccount', account)

    state.id = account.id
    state.email = account.email
    state.profile = account.profile

    // Setup $track userId if available
    if (account.id && this.$track) {
      this.$track.setUserId(account.id)
    }
  },
  clear(state) {
    state.id = null
    state.email = null
    state.profile = null

    Cookies.remove('MunchAccount')

    if (this.$track) {
      this.$track.clearUserData()
    }
  },
}

function authenticate(commit) {
  return this.$api.post('/accounts/tokens/authenticate')
    .then(({data: account}) => {
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

  signOut({commit}) {
    authenticator.signOut()

    this.commit('account/clear')
  },
}
