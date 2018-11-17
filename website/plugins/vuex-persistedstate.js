import createPersistedState from 'vuex-persistedstate'
// TODO Replace this with cookies

import * as Cookies from 'js-cookie'



export default ({store}) => {
  createPersistedState({
    key: 'vuex',
    paths: ['user.profile', 'user.setting']
  })(store)
}
