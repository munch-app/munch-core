import dateformat from 'dateformat'

const ISO_DATE_FORMATTER = "yyyy-mm-dd'T'HH:MM:ss"

export default function ({$axios, store}) {
  if (process.client) {
    $axios.onRequest(
      config => {
        config.headers['User-Local-Time'] = dateformat(new Date(), ISO_DATE_FORMATTER)
        const latLng = store.state.filter.user.latLng
        if (latLng) config.headers['User-Lat-Lng'] = latLng
        return config
      }
    );
  }
}
