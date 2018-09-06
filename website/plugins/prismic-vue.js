import Prismic from 'prismic-javascript'
import PrismicDom from 'prismic-dom'

const endpoint = 'https://munch.cdn.prismic.io/api/v2'
export default (context, inject) => {
  return Prismic.getApi(endpoint, {
    req: context.req
  }).then(Api => {
    context.Prismic = Api
    context.PrismicDOM = PrismicDom
  })
}
