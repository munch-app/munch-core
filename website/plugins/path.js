import {Base64} from 'js-base64';

/**
 * Path & views manipulation plugin.
 */
export default function (context, inject) {
  inject('path', {
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
  })

  function views() {
    const vs = context?.query?.vs
    if (!vs) return {}
    const decoded = Base64.decode(vs)
    if (!decoded) return {}

    return decoded.split(',').reduce((dict, object) => {
      dict[object] = true
      return dict
    }, {})
  }

  /**
   * 'vs' short of 'views'
   */
  inject('vs', {
    has: (view) => {
      return views()[view]
    },
    none: (view) => {
      return !views()[view]
    },
    encode: (...views) => {
      return Base64.encodeURI(views.join(','))
    }
  })
}
