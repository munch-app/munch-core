import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export function createRouter(ssrContext, createDefaultRouter, routerOptions) {
  const options = routerOptions ? routerOptions : createDefaultRouter(ssrContext).options
  const routes = options.routes

  return new Router({
    ...options,
    routes: routes.map(remap)
  })
}

// Special regex path remapping
const paths = {
  '@:username':
    '/@:username([a-z0-9]{3,32})',
  '@:username-articles':
    '/@:username([a-z0-9]{3,32})/articles',
  '@:username-medias':
    '/@:username([a-z0-9]{3,32})/medias',
  '@:username-media:id':
    '/@:username([a-z0-9]{3,32})/:id([0-9a-hjkmnp-tv-z]{15}s)',
  '@:username-mention:id':
    '/@:username([a-z0-9]{3,32})/:id([0-9a-hjkmnp-tv-z]{15}m)',
  '@:username-article:slug:id':
    '/@:username([a-z0-9]{3,32})/:slug([0-9a-z-]{0,1000}):id([0-9a-hjkmnp-tv-z]{12}1)',

  'article:slug:id':
    '/:slug([0-9a-z-]{0,1000}):id([0-9a-hjkmnp-tv-z]{12}1)',
  'place:slug:id':
    '/:slug([0-9a-z-]{0,1000}):id([0-9a-hjkmnp-tv-z]{12}0)'
}

function remap(route) {
  const path = paths[route.name]
  if (path) {
    return {...route, path}
  }
  return route
}
