import Prismic from 'prismic-javascript'
import PrismicDOM from 'prismic-dom'

const endpoint = 'https://munch.cdn.prismic.io/api/v2'

function linkResolver(doc) {
  switch (doc.type) {
    case 'support':
      return '/support'
    case 'support_article':
      return `/support/${doc.data.uid}`

    // Deprecated
    case 'privacy_policy':
      return '/support/privacy'
    case 'terms_of_use':
      return '/support/terms'

    default:
      return `/page/${doc.id}`
  }
}

export default (context, inject) => {
  context.Prismic = {
    dom: PrismicDOM,
    getSingle: (type) => {
      return Prismic.getApi(endpoint, {req: context.req})
        .then(Api => {
          return Api.getSingle(type)
        })
    },
    get: (type, uid) => {
      return Prismic.getApi(endpoint, {req: context.req})
        .then(Api => {
          return Api.getByUID(type, uid)
        })
    },
    asHtml: (content) => content && PrismicDOM.RichText.asHtml(content, linkResolver),
    asText: (content) => content && PrismicDOM.RichText.asText(content),
  }
}
