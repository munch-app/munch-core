module.exports = {
  head: {
    title: 'Discover Delicious · Munch Singapore',
    meta: [
      {charset: 'utf-8'},
      {name: 'viewport', content: 'width=device-width, initial-scale=1'},
      {
        hid: 'description', name: 'description',
        content: 'Munch helps users discover the perfect place to eat whether it’s the hottest new bar in town or a tasty hawker meal in the heartlands. Read the latest food articles and view mouth-watering images. With Munch always be able to discover delicious!'
      },
      {
        hid: 'og:title', name: 'og:title',
        content: 'Discover Delicious · Munch Singapore'
      },
      {
        hid: 'og:description', name: 'og:description',
        content: 'Munch helps users discover the perfect place to eat whether it’s the hottest new bar in town or a tasty hawker meal in the heartlands. Read the latest food articles and view mouth-watering images. With Munch always be able to discover delicious!'
      },
      {hid: 'apple-itunes-app', name: 'apple-itunes-app', content: 'app-id=1255436754'},
      {hid: 'og:type', name: 'og:type', content: 'article'},
      {name: 'og:site_name', content: 'Munch Singapore'},
      {name: 'og:locale', content: 'en_uk'},
      {name: 'msapplication-TileColor', content: '#da532c'},
      {name: 'theme-color', content: '#ffffff'},
    ],
    link: [
      {rel: 'apple-touch-icon', sizes: '180x180', href: '/meta/apple-touch-icon.1.png'},
      {rel: 'icon', type: 'image/png', sizes: '32x32', href: '/meta/favicon-32x32.1.png'},
      {rel: 'icon', type: 'image/png', sizes: '16x16', href: '/meta/favicon-16x16.1.png'},
      {rel: 'manifest', href: '/meta/site.1.webmanifest'},
      {rel: 'mask-icon', href: '/meta/safari-pinned-tab.1.svg', color: '#f05f3b'},

      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700|Roboto'},
    ],
    script: [
      {src: 'https://cdn.apple-mapkit.com/mk/5.x.x/mapkit.js'},
      {src: 'https://www.googletagmanager.com/gtag/js?id=UA-117480436-1'},
      {src: 'https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js', defer: true},

      // Only in production
      ...(process.env.NODE_ENV === 'production' ? [{
        innerHTML: '(adsbygoogle=window.adsbygoogle || []).push({google_ad_client: "ca-pub-7144155418390858", enable_page_level_ads:true});'
      }] : [])
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
  },
  build: {
    extend(config, {isDev, isClient}) {
      if (isDev && isClient) {
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
    }
  },
  vendor: [
    'portal-vue',
    'prismic-vue',
    'vue-rx',
    'vue-clickaway',
    'vue-simple-svg',
    'vue-browser-geolocation',
    'vue-observe-visibility',
    'vuex-persistedstate',
    'vue-scrollto',
    'vue-clipboard2',
    'vue-script2',
  ],
  plugins: [
    // Plugins by Munch Team
    '~/plugins/head',
    '~/plugins/router',
    {src: '~/plugins/tracker', ssr: false},

    // Core Plugin
    '~/plugins/axios',
    '~/plugins/vue-rx',
    '~/plugins/portal-vue',
    '~/plugins/prismic-vue',

    // Required Plugin
    '~/plugins/vue-browser-geolocation',
    {src: '~/plugins/vue-google-adsense', ssr: false},
    {src: '~/plugins/vuex-persistedstate', ssr: false},

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
    {
      path: '/_health', handler: (req, res) => res.end('ok')
    },
    '~/server/index.js',
    '~/server/redirect.js'
  ],
  modules: [
    '@nuxtjs/axios',
    '@nuxtjs/sitemap',
    'nuxt-device-detect',

    ['@nuxtjs/google-tag-manager', {
      id: 'GTM-KVBXWDJ',

      // For use by other Tag Subscriber only, GA & GTAG is done natively
      pageTracking: true
    }],
    'nuxt-google-optimize',
  ],
  axios: {},
  sitemap: {
    path: '/sitemap.xml',
    hostname: 'https://www.munch.app',
    cacheTime: 1000 * 60 * 15,
    exclude: [
      "/profile",
      "/authenticate",
      "/logout",
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
};
