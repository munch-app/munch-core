import webpack from 'webpack'

require('dotenv').config()

const title = 'Munch - Food Discovery'
const description = 'Munch is a social dining solution that makes your life easier when it comes to dining with friends & loved ones. Munch is packed with features to help you plan for and find places. Use Munch to find the perfect spot with friends, get inspired for your next meal or simply receive daily suggestions on what to chow down on.'
const image = 'https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJsb2dvXzMwMC5wbmcifQ=='

export default {
  head: {
    title: title,
    meta: [
      {charset: 'utf-8'},
      {name: 'viewport', content: 'width=device-width, initial-scale=1'},
      {hid: 'og:title', name: 'og:title', content: title},
      {hid: 'description', name: 'description', content: description},
      {hid: 'og:description', name: 'og:description', content: description},
      {hid: 'og:image', name: 'og:image', content: image},
      {hid: 'og:type', name: 'og:type', content: 'article'},
      {hid: 'apple-itunes-app', name: 'apple-itunes-app', content: 'app-id=1255436754'},
      {name: 'og:site_name', content: 'Munch - Food Discovery'},
      {name: 'og:locale', content: 'en_uk'},
      {name: 'msapplication-TileColor', content: '#da532c'},
      {name: 'theme-color', content: '#ffffff'},
    ],
    link: [
      {rel: 'apple-touch-icon', sizes: '180x180', href: '/meta/apple-touch-icon.2.png'},
      {rel: 'icon', type: 'image/png', sizes: '32x32', href: '/meta/favicon-32x32.2.png'},
      {rel: 'icon', type: 'image/png', sizes: '16x16', href: '/meta/favicon-16x16.2.png'},
      {rel: 'manifest', href: '/meta/site.2.webmanifest'},
      {rel: 'mask-icon', href: '/meta/safari-pinned-tab.2.svg', color: '#f05f3b'},
      {
        rel: 'stylesheet',
        href: 'https://fonts.googleapis.com/css?family=Roboto:300,300i,400,400i,500,500i,700,700i&display=swap'
      },
    ],
    script: [
      {src: 'https://cdn.apple-mapkit.com/mk/5.x.x/mapkit.js'},
      {src: 'https://www.googletagmanager.com/gtag/js?id=UA-117480436-1'},
    ]
  },
  css: [
    '~/assets/less/index.less'
  ],
  render: {
    bundleRenderer: {
      shouldPreload: (file, type) => {
        return ['script', 'style', 'font'].includes(type)
      }
    }
  },
  loading: {color: '#0A6284'},
  router: {
    // scrollBehavior: () => ({x: 0, y: 0})
  },
  env: {
    origin: process.env.ORIGIN || 'http://localhost:3000',
    apiUrl: process.env.API_MUNCH_APP,
  },
  build: {
    extend(config, {isDev}) {
      if (isDev && process.client) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }

      if (isDev) {
        config.devtool = 'eval-source-map'
      }
    },
    plugins: [
      new webpack.ProvidePlugin({
        '_': 'lodash'
      }),
    ],
    babel: {
      plugins: ["@babel/plugin-proposal-optional-chaining"]
    },
  },
  plugins: [
    // Plugins developed by Munch
    '~/plugins/head',
    '~/plugins/error',
    '~/plugins/router',
    {src: '~/plugins/tracker', ssr: false},

    // Core Plugin
    '~/plugins/axios',
    '~/plugins/vue-rx',
    '~/plugins/prismic-vue',

    // Required Plugin
    '~/plugins/vue-browser-geolocation',
    {src: '~/plugins/vuex-persistedstate', ssr: false},
    {src: '~/plugins/vue-google-adsense', ssr: false},

    // Useful Plugin
    '~/plugins/vue-clickaway',
    '~/plugins/vue-simple-svg',
    '~/plugins/vue-scrollto',
    '~/plugins/vue-observe-visibility',
    {src: '~/plugins/vue-clipboard2', ssr: false},
    {src: '~/plugins/vue-touch', ssr: false},
    {src: '~/plugins/vue-loader', ssr: false},
  ],
  serverMiddleware: [
    {path: '/_health', handler: (req, res) => res.end('ok')},
    '~/server/index.js',
    '~/server/redirect.js'
  ],
  modules: [
    '@nuxtjs/axios',
    '@nuxtjs/sitemap',
    'portal-vue/nuxt',
    'nuxt-device-detect',

    ['@nuxtjs/google-tag-manager', {
      id: 'GTM-KVBXWDJ',

      // For use by other Tag Subscriber only, GA & GTAG is done natively
      pageTracking: true
    }],
  ],
  axios: {},
  sitemap: {
    path: '/sitemap.xml',
    hostname: 'https://www.munch.app',
    cacheTime: 1000 * 60 * 15,
    exclude: [
      "/me",
      "/feed/articles",
    ],
    routes: [
      '/support/privacy',
      '/support/terms',
      {
        url: '/feed/images',
        changefreq: 'daily',
        priority: 1
      }
    ]
  }
}
