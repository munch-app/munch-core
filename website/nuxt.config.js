module.exports = {
  /*
  ** Headers of the page
  */
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
      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=Open+Sans|Roboto'}
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
  /*
  ** Customize the progress bar color
  */
  loading: {color: '#3B8070'},
  /*
  ** Build configuration
  */
  build: {
    /*
    ** Run ESLint on save
    */
    extend(config, {isDev, isClient}) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
      if (!isClient) {
        // This instructs Webpack to include `vue2-google-maps`'s Vue files
        // for server-side rendering
        config.externals.splice(0, 0, function (context, request, callback) {
          if (/^vue2-google-maps($|\/)/.test(request)) {
            callback(null, false)
          } else {
            callback()
          }
        })
      }

      if (isDev) {
        config.devtool = 'eval-source-map'
      }
    }
  },
  serverMiddleware: [
    {
      path: '/_health', handler: (req, res) => res.end('ok')
    },
    '~/server/index.js'
  ],
  modules: [
    'bootstrap-vue/nuxt',
    ['@nuxtjs/google-analytics', {
      id: 'UA-117480436-1'
    }],
    '@nuxtjs/axios',
    '@nuxtjs/sitemap'
  ],
  plugins: [
    '~/plugins/axios',
    '~/plugins/global'
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
    ]
  }
};
