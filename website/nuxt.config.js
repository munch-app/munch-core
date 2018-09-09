const webpack = require('webpack')

module.exports = {
  head: {
    title: 'Discover Delicious - Munch',
    meta: [
      {charset: 'utf-8'},
      {name: 'viewport', content: 'width=device-width, initial-scale=1'},
      {
        hid: 'description', name: 'description',
        content: 'Munch helps users discover the perfect place to eat whether it’s the hottest new bar in town or a tasty hawker meal in the heartlands. Read the latest food articles and view mouth-watering images. With Munch always be able to discover delicious!'
      }
    ],
    link: [
      {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'},
      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|Roboto'}
    ]
  },
  css: [
    '~/assets/global.less',
    '~/assets/colors.less',
  ],
  render: {
    bundleRenderer: {
      shouldPreload: (file, type) => {
        return ['script', 'style', 'font'].includes(type)
      }
    }
  },
  loading: {color: '#3B8070'},
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
            whitelist: [/^vue-slick/]
          })
        ]
      }

      if (isDev) {
        config.devtool = 'eval-source-map'
      }
    },
    vendor: [
      'jquery',
      'vue-line-clamp-extended',
      'vue-clickaway',
      'vue-simple-svg',
      'vue-rx',
      'prismic-vue',
      'portal-vue',
    ],
    plugins: [
      new webpack.ProvidePlugin({
        '$': 'jquery'
      })
    ]
  },
  plugins: [
    '~/plugins/axios',
    '~/plugins/global',
    '~/plugins/vue-line-clamp-extended',
    '~/plugins/vue-clickaway',
    '~/plugins/vue-simple-svg',
    '~/plugins/vue-rx',
    '~/plugins/prismic-vue',
    '~/plugins/portal-vue',
  ],
  serverMiddleware: [
    {
      path: '/_health', handler: (req, res) => res.end('ok')
    },
    '~/server/index.js'
  ],
  modules: [
    'bootstrap-vue/nuxt',
    '@nuxtjs/axios',
    '@nuxtjs/sitemap',
    ['@nuxtjs/google-analytics', {
      id: 'UA-117480436-1'
    }],
  ],
  axios: {
    // proxyHeaders: false
  },
  sitemap: {
    path: '/sitemap.xml',
    hostname: 'https://www.munch.app',
    cacheTime: 1000 * 60 * 15,
    exclude: [],
    routes: [
      '/',
      '/downloads',
      '/support',
      '/search',
      '/about',
    ]
  }
};
