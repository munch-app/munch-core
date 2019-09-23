<template>
  <div class="container pt-48 pb-128">
    <div class="flex-between">
      <h2>Place Data Management</h2>
      <div>
        <nuxt-link to="/system/places/_">
          <button class="small pink-outline">New Place</button>
        </nuxt-link>
      </div>
    </div>

    <search-place class="mt-24 border border-3" @on-select="onSelect" input-hint="Search places" :size="30">
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
              </div>
            </div>
          </div>
        </div>
      </template>
    </search-place>
  </div>
</template>

<script>
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import CdnImg from "../../../components/utils/image/CdnImg";
  import SearchPlace from "../../../components/places/SearchPlace";
  import PlaceEditorDialog from "../../../components/dialog/PlaceEditorDialog";

  export default {
    components: {PlaceEditorDialog, SearchPlace, CdnImg},
    layout: 'system',
    data() {
      return {
        state: null
      }
    },
    methods: {
      onSelect(document) {
        this.$store.commit('global/setDialog', 'LoadingDialog')
        this.$api.get(`/places/${document.id}`)
          .then(({data: place}) => {
            this.onEditing(place)
          })
      },
      onEditing(place) {
        this.$store.commit('global/setDialog', {
          name: 'PlaceEditorDialog', props: {
            place: place,
            onSubmit: (place) => {
              this.$api.post(`/places/${place.id}/revisions`, place)
                .then(() => {
                  this.$store.dispatch('addMessage', {title: 'Added Revision', message: 'Thanks for contributing!'})
                })
                .catch(err => {
                  this.$store.dispatch('addError', err)
                })
                .finally(() => {
                  this.$store.commit('global/clearDialog')
                })
            }
          }
        })
      },
    }
  }
</script>
