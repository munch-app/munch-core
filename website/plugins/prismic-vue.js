import Prismic from 'prismic-javascript'
import PrismicDOM from 'prismic-dom'

const endpoint = 'https://munch.cdn.prismic.io/api/v2'

function linkResolver(doc) {
  switch (doc.type) {
    case 'support':
      return '/support'
    case 'privacy_policy':
      return '/support/privacy-policy'
    case 'terms_of_use':
      return '/support/terms-of-use'
    default:
      return `/page/${doc.id}`
  }
}

export default (context, inject) => {
  return Prismic.getApi(endpoint, {
    req: context.req
  }).then(Api => {
    context.Prismic = {
      api: Api,
      dom: PrismicDOM,
      getSingle: (type) => Api.getSingle(type),
      asHtml: (content) => PrismicDOM.RichText.asHtml(content, linkResolver)
    }
  })
}
