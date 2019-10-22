import {Base64} from 'js-base64';

export default function (context, inject) {
  const {query: {views}} = context
  context.$path = {
    views: () => {
      if (!views) return {}
      const decoded = Base64.decode(views)
      if (!decoded) return {}

      return decoded.split(',').reduce((dict, object) => {
        dict[object] = true
        return dict
      }, {})
    },
    replace: ({path, query}) => {
      const getPath = () => {
        return path ? path : window.location.pathname;
      }

      const getQuery = () => {
        if (query) {
          if (Object.keys(query).length > 0) {
            return '?' + Object.keys(query)
              .map(k => `${encodeURIComponent(k)}=${encodeURIComponent(query[k])}`)
              .join('&');
          }

          return ''
        }
        return window.location.search || ''
      }

      window.history.replaceState({}, document.title, `${getPath()}${getQuery()}`)
    },
    encodeViews: (...views) => {
      return Base64.encodeURI(views.join(','))
    },
  }
  inject('path', context.$path)
}
