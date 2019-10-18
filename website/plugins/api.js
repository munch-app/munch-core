import * as Cookies from 'js-cookie'
import authenticator from '~/services/authenticator'
import FormData from "form-data"

const apiVersion = 'v0.26.0'

export default function (context, inject) {
  const {$axios, store, req} = context

  $axios.onResponse((response) => {
    if (response.data && response.data.error) {
      const error = response.data.error
      throw({statusCode: error.code, message: error.message, response})
    }
  })

  if (process.client) {
    $axios.onRequest(config => {
      const latLng = store.state.filter.user.latLng || Cookies.get('UserLatLng')
      if (latLng) config.headers['Local-Lat-Lng'] = latLng

      // Get user Id token
      return authenticator.getIdToken().then(({token}) => {
        if (token) config.headers['Authorization'] = `Bearer ${token}`
        return config
      }).catch(() => {
        // Error, Logout User,
        store.dispatch('account/signOut')
        return config
      })
    });
  }

  if (process.server) {
    $axios.onRequest(config => {
      const token = req.cookies.IdToken && JSON.parse(req.cookies.IdToken).token
      if (token) config.headers['Authorization'] = `Bearer ${token}`

      const latLng = req.cookies.UserLatLng
      if (latLng) config.headers['Local-Lat-Lng'] = latLng
      return config
    });
  }

  context.$api = {
    get: (path, config) => $axios.$get(`/api/${apiVersion}` + path, config),
    put: (path, data, config) => $axios.$put(`/api/${apiVersion}` + path, data, config),
    post: (path, data, config) => $axios.$post(`/api/${apiVersion}` + path, data, config),
    patch: (path, data, config) => $axios.$patch(`/api/${apiVersion}` + path, data, config),
    delete: (path, data, config) => $axios.$delete(`/api/${apiVersion}` + path, data, config),

    postImage: (file, source) => {
      const form = new FormData()
      form.append('file', file, file.name)
      form.append("source", source)
      console.log(process.env.apiUrl)
      return $axios.$post(`${process.env.apiUrl}${apiVersion}/me/images`, form)
    },
  }
  inject('api', context.$api)
}
