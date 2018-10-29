/**
 * Because some of the gtag event is triggered on ssr
 * This plugin will ensure that the event is triggered on client side via ssr: false
 *
 * You should trigger all gtag event, the store will reject them if invalid
 */
export default async ({store}) => {
  await store.dispatch('search/gtag')
}
