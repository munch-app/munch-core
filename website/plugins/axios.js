import * as Cookies from 'js-cookie'
import authenticator from '~/services/authenticator'
import FormData from "form-data";

export default function (context, inject) {
  const {$axios, store, req, error, redirect} = context

  // This is old code, below version is copied from vyro
  // $axios.onResponse(({data}) => {
  //   const meta = data && data.meta
  //   if (meta && meta.code === 404) {
  //     throw({statusCode: 404, message: 'Not Found'})
  //   } else if (meta && meta.code >= 300) {
  //     throw({statusCode: meta.code, message: meta.error.message, meta})
  //   }
  // })

  $axios.onResponse((response) => {
    if (response.data && response.data.error) {
      const error = response.data.error
      throw({statusCode: error.code, message: error.message, response})
    }
  })

  $axios.onError((err) => {
    // TODO(fuxing): definitely need a better way to handle this, maybe can attempt at context.$api
    const status = err.response.status
    if (status === 404) {

    }
    // } else if (status) {
    //   // error(err)
    // }
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
      if (latLng) config.headers['Local-Lat-Lng'] = latLng
      return config
    });
  }

  context.$api = {
    get: (path, config) => $axios.$get('/api' + path, config),
    put: (path, data, config) => $axios.$put('/api' + path, data, config),
    post: (path, data, config) => $axios.$post('/api' + path, data, config),
    patch: (path, data, config) => $axios.$patch('/api' + path, data, config),
    delete: (path, data, config) => $axios.$delete('/api' + path, data, config),

    postImage: (file, source) => {
      const form = new FormData()
      form.append('file', file, file.name)
      form.append("source", source)
      console.log(process.env.apiUrl)
      return $axios.$post(process.env.apiUrl + '/me/images', form)
    },
  }
  inject('api', context.$api)
}
