import dateformat from 'dateformat'
import authenticator from '~/services/authenticator'

export default function ({$axios, store, req}) {
  if (process.client) {
    $axios.onRequest(config => {
      // User Data from state
      config.headers['User-Local-Time'] = dateformat(new Date(), "yyyy-mm-dd'T'HH:MM:ss")
      const latLng = store.state.filter.user.latLng
      if (latLng) config.headers['User-Lat-Lng'] = latLng

      // Get user Id token
      return authenticator.getIdToken().then(({token}) => {
        if (token) config.headers['Authorization'] = `Bearer ${token}`
        return config
      }).catch(() => {
        return config
      })
    });
  }

  if (process.server) {
    $axios.onRequest(config => {
      const token = req.cookies.IdToken && req.cookies.IdToken.token
      if (token) config.headers['Authorization'] = `Bearer ${token}`
      return config
    });
  }
}
