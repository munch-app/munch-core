import dateformat from 'dateformat'

const ISO_DATE_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss"

export default function ({$axios, store}) {
  if (process.client) {
    $axios.setHeader('User-Local-Time', dateformat(new Date(), ISO_DATE_FORMATTER))
    const latLng = store.state.filter.user.latLng
    if (latLng) $axios.setHeader('User-Lat-Lng', latLng)
  }
}
