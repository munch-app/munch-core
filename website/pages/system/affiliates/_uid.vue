<template>
  <div class="container pt-24 pb-256">
    <div class="flex-align-center flex-between">
      <div>
        <h4>Affiliate Linking</h4>
      </div>
      <div class="flex-align-center">
        <button class="border" @click="state = 'delete'" v-if="!state">Delete</button>
        <button class="danger" @click="onDelete" v-if="state === 'delete'">Confirm Delete?</button>
      </div>
    </div>

    <div class="mt-24">
      <div class="flex-1-2 m--12">
        <div class="p-12">
          <div class="border border-3 p-8-12 flex-align-center">
            <h5>Type:</h5>
            <div class="ml-8 p-4-8 small-bold bg-steam border-2">{{affiliate.type}}</div>
          </div>
          <div class="border border-3 p-8-12 flex-align-center mt-16">
            <h5>Status:</h5>
            <div class="ml-8 p-4-8 small-bold bg-steam border-2">{{affiliate.status}}</div>
          </div>
          <div class="border border-3 p-8-12 mt-16 overflow-hidden">
            <h5>Details:</h5>
            <pre>{{affiliate.placeStruct}}</pre>
          </div>
          <div class="border border-3 p-8-12 mt-16">
            <h5>Brand: {{affiliate.brand.name}}</h5>
            <div class="BrandImage flex">
              <cdn-img :image="affiliate.brand.image" object-fit="contain"/>
            </div>
          </div>
          <div class="border border-3 p-8-12 mt-16">
            <h5>Affiliate Link:</h5>
            <a rel="noreferrer nofollow noopener" :href="affiliate.url" target="_blank">{{affiliate.url}}</a>
          </div>
        </div>
        <div class="p-12">
          <search-place v-if="state === null" :pre-fill="preFill" class="border border-3" create
                        @on-select="onSelect"
                        @on-create="onCreate">
            <template v-slot:default="{document}">
              <div class="p-16 hover-bg-a10">
                <div class="flex">
                  <div class="wh-80px flex-no-shrink mr-16" v-if="document.image">
                    <cdn-img class="border-2 overflow-hidden" type="320x320" :image="document.image"/>
                  </div>
                  <div>
                    <h5 class="text-ellipsis-1l">{{document.name}}</h5>
                    <p class="text-ellipsis-1l m-0">{{document.location.address}}</p>
                    <div class="flex-align-center">
                      <div v-if="document.status.type !== 'OPEN'"
                           class="white tiny-bold bg-error p-6 lh-1 border-2 mr-8">
                        {{document.status.type}}
                      </div>
                      <h6 class="b-a60 text-ellipsis-1l">{{distanceBetween(affiliate, document)}} km from Affiliate</h6>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </search-place>
          <div v-if="state === 'place'">
            <place-editor ref="editor" :place="place"/>
            <div class="flex-end mt-24">
              <button class="blue" @click="onUpdateLink">Update & Link</button>
            </div>
          </div>
          <div v-if="state === 'fetching'" class="flex-center p-16">
            <beat-loader color="#07F" size="12px"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../../components/utils/image/CdnImg";
  import SearchPlace from "../../../components/places/SearchPlace";
  import Spatial from '../../../utils/spatial'
  import PlaceEditor from "../../../components/places/PlaceEditor";

  export default {
    components: {PlaceEditor, SearchPlace, CdnImg},
    layout: 'system',
    asyncData({$api, params: {uid}}) {
      return $api.get(`/admin/affiliates/${uid}`)
        .then(({data: affiliate}) => {
          return {affiliate}
        })
    },
    data() {
      return {
        state: null,
        place: null
      }
    },
    computed: {
      preFill() {
        return this.affiliate?.placeStruct?.name
      }
    },
    methods: {
      distanceBetween: (affiliate, document) => {
        const aLatLng = affiliate?.placeStruct?.location?.latLng
        const dLatLng = document?.location?.latLng
        if (aLatLng && dLatLng) {
          return Spatial.distance(aLatLng, dLatLng).toFixed(3)
        }
      },
      onSelect(document) {
        this.state = 'fetching'
        this.$api.get(`/places/${document.id}`)
          .then(({data}) => {
            this.state = 'place'
            this.place = data
          })
      },
      onCreate() {
        this.state = 'place'
        this.place = this.affiliate?.placeStruct
        window.scrollTo(0, 0)
      },
      onDelete() {
        this.state = ''

        this.$api.patch(`/admin/affiliates/${this.uid}`, {status: 'DELETED_MUNCH'})
          .then(() => {
            this.$router.push({path: `/system/affiliates`})
          })
          .catch(err => {
            this.$store.dispatch('addError', err)
          })
      },
      onUpdateLink() {
        const $api = this.$api

        function post(place) {
          if (place.id) {
            return $api.post(`/places/${place.id}/revisions`, place)
          } else {
            return $api.post(`/places`, place)
          }
        }

        return this.$refs.editor.confirm(({place}) => {
          post(place)
            .then(({data}) => {
              return $api.patch(`/admin/affiliates/${this.affiliate.uid}`, {status: 'LINKED', linked: {place: data}})
                .then(() => {
                  this.$router.push({path: `/system/affiliates`})
                });
            })
            .catch(err => {
              this.$store.dispatch('addError', err)
            })
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .BrandImage {
    height: 80px;
  }
</style>
