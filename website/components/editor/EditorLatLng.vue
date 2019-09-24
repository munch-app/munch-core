<template>
  <div class="border-2 overflow-hidden" :class="{Searching: state === 'searching'}">
    <div class="relative">
      <div class="absolute position-r-0 mr-8 h-100 flex-center" v-if="!state">
        <div @click="copyMapCenter" v-if="editing !== mapCenter"
             class="bg-blue border-2 white small-bold p-4-8 hover-pointer">
          Copy Map Center
        </div>
      </div>

      <input v-model.trim="editing" @keyup="update">
    </div>
    <div class="Map flex">
      <div class="relative wh-100">
        <apple-map ref="map" class="wh-100" @on-change="onMapChange" :options="map.options">
          <apple-map-pin-annotation v-if="isLatLng" :lat-lng="editing"/>
        </apple-map>

        <div class="absolute position-r-0 position-tb-0 mr-16 mtb-16">
          <button class="blue-outline tiny" v-if="state !== 'searching'" @click="state = 'searching'">
            Search Address
          </button>
          <button class="blue-outline tiny" v-if="state === 'searching'" @click="state = null">
            Close Search
          </button>
        </div>
      </div>

      <div v-if="state === 'searching'" class="SearchBox hr-right hr-bot">
        <div class="h-100 flex-column">
          <input v-model="query" placeholder="Search Name/Address">
          <div class="p-24 flex-center" v-if="loading">
            <beat-loader color="#07F" size="12px"/>
          </div>
          <div class="overflow-y-auto flex-grow">
            <div v-for="location in locations" :key="location.miud">
              <div class="p-12 hr-bot hover-pointer hover-bg-a10" @click="onLocation(location)">
                <h5>{{location.name}}</h5>
                <div><small>{{location.formattedAddress}}</small></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {debounceTime, distinctUntilChanged, filter, map, pluck, switchMap, tap} from 'rxjs/operators'

  import AppleMap from "../utils/map/AppleMap";
  import AppleMapPinAnnotation from "../utils/map/AppleMapPinAnnotation";

  export default {
    name: "EditorLatLng",
    components: {AppleMapPinAnnotation, AppleMap},
    props: {
      value: String,
      preState: {
        type: String,
        default: null
      }
    },
    data() {
      return {
        editing: this.value,
        mapCenter: this.value,
        map: {
          options: {
            showsZoomControl: true,
          }
        },
        state: this.preState,
        query: null,
        locations: null,
        loading: false
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.$refs.map.centerAnnotations({
          minimumSpan: new mapkit.CoordinateSpan(0.00005, 0.00005)
        })
      })
    },
    computed: {
      isLatLng() {
        if (!this.editing) return false

        const ll = this.editing.split(',')
        const lat = parseFloat(ll[0])
        const lng = parseFloat(ll[1])
        return lat && lng
      }
    },
    methods: {
      onMapChange(map) {
        const coordinate = map.center
        this.mapCenter = `${coordinate.latitude.toFixed(5)},${coordinate.longitude.toFixed(5)}`
      },
      copyMapCenter() {
        this.editing = this.mapCenter
        this.update()
      },
      update() {
        if (this.editing) {
          this.$emit('input', this.editing)
        } else {
          this.$emit('input', null)
        }
      },
      onLocation(location) {
        this.state = null
        const coordinate = location.coordinate
        this.editing = `${coordinate.latitude.toFixed(5)},${coordinate.longitude.toFixed(5)}`

        this.$nextTick(() => {
          this.$refs.map.centerAnnotations({
            minimumSpan: new mapkit.CoordinateSpan(0.00005, 0.00005)
          })
        })
        this.update()
      }
    },
    subscriptions() {
      return {
        locations: this.$watchAsObservable('query').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          filter((text) => text !== ''),
          debounceTime(500),
          tap(() => {
            this.locations = null
            this.loading = true
          }),
          distinctUntilChanged(),
          switchMap((text) => {
            return new Promise((resolve, reject) => {
              const search = new mapkit.Search({region: this.$refs.map.$map.region})
              search.search(text, function (error, data) {
                if (error) {
                  reject(error)
                } else {
                  resolve(data.places)
                }
              })
            })
          }),
          tap(() => this.loading = false),
        )
      }
    },
  }
</script>

<style scoped lang="less">
  input, .TextAuto {
    outline: none;
    border: none;

    background: #FAFAFA;
    color: black;

    width: 100%;
    font-size: 17px;
    padding: 12px;

    &:focus {
      background: #F0F0F0;
    }
  }

  .Map {
    height: 160px;
  }

  .Searching {
    .Map {
      height: 360px;
    }
  }

  .SearchBox {
    flex: 0 0 50%;
    max-width: 50%;
  }
</style>
