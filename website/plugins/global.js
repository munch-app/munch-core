import Vue from 'vue'

import { lineClamp } from 'vue-line-clamp-extended'
Vue.directive('line-clamp', lineClamp)

import VueSimpleSVG from 'vue-simple-svg'
Vue.use(VueSimpleSVG)

import VueRx from 'vue-rx'
Vue.use(VueRx)

// import * as VueGoogleMaps from 'vue2-google-maps'
// Vue.use(VueGoogleMaps, {
//   load: {
//     key: 'AIzaSyDnaI9tCw54qWibZ7MMA_tYZF121Mw2h5E'
//   },
// })
