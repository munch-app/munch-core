import createPersistedState from 'vuex-persistedstate'
// TODO Redo this with new authentication caching on server site

import * as Cookies from 'js-cookie'

export default ({store}) => {
  createPersistedState({
    key: 'vuex',
    paths: ['user.profile', 'user.setting']
  })(store)
}
