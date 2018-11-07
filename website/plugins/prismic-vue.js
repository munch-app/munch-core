import Prismic from 'prismic-javascript'
import PrismicDOM from 'prismic-dom'

function linkResolver(doc) {
  switch (doc.type) {
    case 'support':
      return '/support'
    case 'support_article':
      return `/support/${doc.data.uid}`

    default:
      return `/page/${doc.id}`
  }
}

export default ({context: {req}}, inject) => {
  const getApi = Prismic.getApi('https://munch.cdn.prismic.io/api/v2', {req})

  const prismic = {
    single: (type) => getApi.then(api => {
      return api.getSingle(type)
    }),
    get: (type, uid) => getApi.then(api => {
      return api.getByUID(type, uid)
    }),
    asHtml: (content) => {
      return content && PrismicDOM.RichText.asHtml(content, linkResolver)
    },
    asText: (content) => {
      return content && PrismicDOM.RichText.asText(content)
    },
  }

  inject('prismic', prismic)
}
