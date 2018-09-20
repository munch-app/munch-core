import createPersistedState from 'vuex-persistedstate'

export default ({store}) => {
  createPersistedState({
    key: 'vuex',
    paths: ['user.profile', 'user.setting']
  })(store)
}
