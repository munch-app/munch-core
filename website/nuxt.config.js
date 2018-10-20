const webpack = require('webpack')

module.exports = {
  head: {
    title: 'Discover Delicious · Munch',
    meta: [
      {charset: 'utf-8'},
      {name: 'viewport', content: 'width=device-width, initial-scale=1'},
      {
        hid: 'description', name: 'description',
        content: 'Munch helps users discover the perfect place to eat whether it’s the hottest new bar in town or a tasty hawker meal in the heartlands. Read the latest food articles and view mouth-watering images. With Munch always be able to discover delicious!'
      },
      {name: 'msapplication-TileColor', content: '#da532c'},
      {name: 'theme-color', content: '#ffffff'},
    ],
    link: [
      {rel: 'apple-touch-icon', sizes: '180x180', href: '/apple-touch-icon.png'},
      {rel: 'icon', type: 'image/png', sizes: '32x32', href: '/favicon-32x32.png'},
      {rel: 'icon', type: 'image/png', sizes: '16x16', href: '/favicon-16x16.png'},
      {rel: 'manifest', href: '/site.webmanifest'},
      {rel: 'mask-icon', href: '/safari-pinned-tab.svg', color: '#f05f3b'},

      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|Roboto'},
    ],
    script: [
      {src: 'https://cdn.apple-mapkit.com/mk/5.x.x/mapkit.js'}
    ]
  },
  css: [
    '~/assets/global.less'
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
    scrollBehavior: () => ({x: 0, y: 0})
  },
  build: {
    extend(config, {isDev, isClient, isServer}) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }

      if (isServer) {
        config.externals += [
          require('webpack-node-externals')({
            whitelist: ['vue-slick']
          })
        ]
      }

      if (isDev) {
        config.devtool = 'eval-source-map'
      }
    },
    plugins: [
      new webpack.ProvidePlugin({
        '$': 'jquery'
      })
    ]
  },
  vendor: [
    'jquery',

    'portal-vue',
    'prismic-vue',
    'vue-rx',
    'vue-clickaway',
    'vue-simple-svg',
    'vue-browser-geolocation',
    'vue-observe-visibility',
    'vue-slick',

    'vuex-persistedstate',
  ],
  plugins: [
    '~/plugins/global',
    '~/plugins/axios',
    '~/plugins/vue-router',

    '~/plugins/portal-vue',
    '~/plugins/prismic-vue',
    '~/plugins/vue-rx',
    '~/plugins/vue-clickaway',
    '~/plugins/vue-simple-svg',
    '~/plugins/vue-browser-geolocation',
    '~/plugins/vue-observe-visibility',
    '~/plugins/vue-slick',

    {src: '~/plugins/vue-touch', ssr: false},
    {src: '~/plugins/vue-loader', ssr: false},
    {src: '~/plugins/vuex-persistedstate', ssr: false},
  ],
  serverMiddleware: [
    {
      path: '/_health', handler: (req, res) => res.end('ok')
    },
    '~/server/index.js',
    '~/server/redirect.js'
  ],
  modules: [
    'bootstrap-vue/nuxt',
    '@nuxtjs/axios',
    '@nuxtjs/sitemap',

    '@nuxtjs/google-gtag',
    ['@nuxtjs/google-tag-manager', {
      id: 'GTM-KVBXWDJ',
      pageTracking: true
    }],
  ],
  'google-gtag': {
    id: 'UA-117480436-1',
    config: {
      send_page_view: false,
      'custom_map': {
        // Used to trace origin
        'dimension1': 'from_action',
        'dimension4': 'from_category',
        'dimension3': 'from_search_location_type',
      }
    },
    debug: false,
    disableAutoPageTrack: true,
  },
  axios: {},
  sitemap: {
    path: '/sitemap.xml',
    hostname: 'https://www.munch.app',
    cacheTime: 1000 * 60 * 15,
    exclude: [
      "/profile",
      "/authenticate",
    ],
    routes: [] // Custom routes to configure
  }
};
