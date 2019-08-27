<template>
  <div class="fixed position-0 bg-overlay flex-center index-dialog">
    <div>
      <div class="flex-justify-end">
        <div class="CloseButton absolute hover-pointer" @click="onClose">
          <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>

      <div class="Dialog dialog-large flex-column wh-100" v-on-clickaway="onClose">
        <div class="hr-bot p-24">
          <input class="large" ref="input" v-model="query" placeholder="Restaurant name">
        </div>
        <div class="Result p-12 flex-grow overflow-y-auto">
          <div v-if="loading" class="flex-center p-24">
            <beat-loader color="#07F" size="16px"/>
          </div>

          <div v-for="place in places" :key="place.placeId" @click="onPlace(place)"
               class="Item p-12 hover-pointer flex-align-center p-12 border-3 hover-bg-a40">

            <div class="flex-no-shrink overflow-hidden border-3">
              <image-sizes v-if="place.images.length > 0" :sizes="place.images[0].sizes"
                           width="100" object-fit="cover" class="wh-64px"/>
              <div v-else class="bg-whisper100 p-12 text-center b-a75 flex-center tiny wh-64px">No Image Available</div>
            </div>

            <div class="ml-16">
              <h5 class="m-0">{{place.name}}</h5>
              <div class="small">{{place.location.address}}</div>
            </div>
          </div>

<!--          <div v-if="creatable && places.length === 0" class="p-12 border-3 hover-pointer hover-bg-a40"-->
<!--               @click="onNew">-->
<!--            <div class="flex-align-center">-->
<!--              <div class="wh-64px p-16 bg-steam border-3">-->
<!--                <simple-svg fill="#333" :filepath="require('~/assets/icon/icons8-plus-math.svg')"/>-->
<!--              </div>-->
<!--              <div class="ml-16">-->
<!--                <h4>Create new place: {{query}}</h4>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "ArticlePlaceDialog",
    components: {ImageSizes},
    data() {
      return {
        query: '',
        loading: false,
        creatable: false,
        places: [],
        cursor: {},
      }
    },
    computed: {
      next() {
        return this.cursor?.next
      },
    },
    mounted() {
      this.$nextTick(() => {
        this.$refs.input.focus()
      })
    },
    methods: {
      onLoadMore() {
        // TODO(fuxing): required after place block migration PlaceBlock
      },
      onClose() {
        this.$emit('on-close')
      },
      onPlace(place) {
        this.$api.get(`/migrations/places/${place.placeId}`)
          .then(({data: place}) => {
            this.$emit('on-place', place)
          })
          .catch(error => {
            console.log(error)
            this.$store.dispatch('addMessage', {
              type: 'error',
              title: 'Migration Error',
              message: 'We are in the mist of migrating our database, please email us at support@munch.app for help.'
            })
          })
      },
      onNew() {
        this.$emit('on-place', {
          name: this.query,
          price: {}, location: {},
          status: {type: 'OPEN'},
          tags: [], hours: [], synonyms: []
        })
      },
    },
    subscriptions() {
      const observable = this.$watchAsObservable('query').pipe(
        pluck('newValue'),
        tap(() => {
          this.places = []
          this.creatable = false
        }),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        tap(() => {
          this.loading = true
        }),
        distinctUntilChanged(),
        debounceTime(333),
      )

      const places = observable.pipe(
        switchMap((text) => {
          return this.$api.post('/suggest', {"text": text, "searchQuery": {}}, {progress: false})
        }),
        map(({data: {places}}) => places),
        tap(() => {
          this.loading = false
          this.creatable = true
        })
      )

      return {places}
    }
  }
</script>

<style scoped lang="less">
  input {
    outline: none;
    border: none;
    background: none;

    padding: 0;
  }

  .Dialog {
    height: 600px;
    padding: 0;
  }

  .CloseButton {
    margin-top: -48px;
  }

  .Result {

  }
</style>
