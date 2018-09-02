import Vue from 'vue'

import { lineClamp } from 'vue-line-clamp-extended'
Vue.directive('line-clamp', lineClamp)

import VueSimpleSVG from 'vue-simple-svg'
Vue.use(VueSimpleSVG)

import VueRx from 'vue-rx'
Vue.use(VueRx)

import * as VueGoogleMaps from 'vue2-google-maps'
Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyCpV-NwmrL6SfeWacF5xHEhCLniiTzZS3c',
    libraries: 'places',
  },
})
