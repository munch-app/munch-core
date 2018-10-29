import dateformat from 'dateformat'
import authenticator from '~/services/authenticator'
import * as Cookies from 'js-cookie'

export default function ({$axios, store, req}) {
  // $axios.onError(error => {
  //   // TODO
  //   throw({statusCode: 404, message: 'Not Found'})
  //   // return $nuxt.error({statusCode: 404, message: 'Not Found'})
  // })

  if (process.client) {
    $axios.onRequest(config => {
      // User Data from state
      config.headers['User-Local-Time'] = dateformat(new Date(), "yyyy-mm-dd'T'HH:MM:ss")

      const latLng = store.state.filter.user.latLng || Cookies.get('UserLatLng')
      if (latLng) config.headers['User-Lat-Lng'] = latLng

      // Get user Id token
      return authenticator.getIdToken().then(({token}) => {
        if (token) config.headers['Authorization'] = `Bearer ${token}`
        return config
      }).catch(() => {
        // Error, Logout User,
        store.dispatch('user/logout')
        return config
      })
    });
  }

  if (process.server) {
    $axios.onRequest(config => {
      const token = req.cookies.IdToken && JSON.parse(req.cookies.IdToken).token
      if (token) config.headers['Authorization'] = `Bearer ${token}`

      const latLng = req.cookies.UserLatLng
      if (latLng) config.headers['User-Lat-Lng'] = latLng
      return config
    });
  }
}
