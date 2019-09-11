<template>
  <div class="container pt-48 pb-128">
    <div class="flex-between">
      <h1>Place Data Management</h1>
      <div>
        <nuxt-link to="/system/places/_">
          <button class="pink-outline">New Place</button>
        </nuxt-link>
      </div>
    </div>

    <div class="ptb-24 hr-bot">
      <div class="p-24 bg-steam border-3 overflow-hidden">
        <input class="h3 clear w-100" ref="input" v-model="query" placeholder="Search: name">
      </div>
    </div>

    <div class="mt-24">
      <nuxt-link :to="`/system/places/${document.id}`" class="block ptb-12 text-decoration-none" v-for="document in documents" :key="document.key">
        <div class="bg-steam border-4 overflow-hidden hover-pointer">
          <div class="p-24 flex hover-bg-a10">
            <div class="flex-no-shrink wh-80px mr-24" v-if="document.image">
              <cdn-img class="border-3 overflow-hidden" type="320x320" :image="document.image"/>
            </div>
            <div class="flex-grow">
              <h3>{{document.name}}</h3>
              <div class="regular">{{document.location.address}}</div>
              <div class="mt-8 flex-align-center">
                <div class="mr-8 small">Status:</div>
                <div class="p-4-12 border-3 white small-bold" :class="{
              'bg-success': document.status.type === 'OPEN',
              'bg-error': document.status.type !== 'OPEN',
              }">
                  {{document.status.type}}
                </div>
              </div>
            </div>
          </div>
        </div>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import CdnImg from "../../../components/utils/image/CdnImg";

  export default {
    components: {CdnImg},
    layout: 'system',
    data() {
      return {
        query: '',
        loading: false,

        documents: [],
      }
    },
    subscriptions() {
      const observable = this.$watchAsObservable('query').pipe(
        pluck('newValue'),
        tap(() => {
          this.documents = []
        }),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        tap(() => {
          this.loading = true
        }),
        distinctUntilChanged(),
        debounceTime(333),
      )

      return {
        documents: observable.pipe(
          switchMap((text) => {
            return this.$api.get('/places/search', {
              params: {
                text, fields: 'key,id,name,image,location,tags,status'
              }
            }, {progress: false})
          }),
          map(({data: documents}) => documents),
          tap((d) => {
            this.loading = false
          })
        )
      }
    }
  }
</script>

<style scoped lang="less">

</style>
