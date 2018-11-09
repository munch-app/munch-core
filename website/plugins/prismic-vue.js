import Prismic from 'prismic-javascript'
import PrismicDOM from 'prismic-dom'

function linkResolver(doc) {
  switch (doc.type) {
    case 'support':
      return '/support'
    case 'support_article':
      return `/support/${doc.uid}`

    default:
      return `/page/${doc.uid}`
  }
}

export default (context, inject) => {
  const {req} = context
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

  context.$prismic = prismic
  inject('prismic', prismic)
}
