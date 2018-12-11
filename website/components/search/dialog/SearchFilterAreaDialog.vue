<template>
  <portal to="dialog-w400">
    <h2 class="mb-8">Locations</h2>

    <div class="SearchTextBar border-3 hover-pointer mb-8">
      <input ref="input" class="TextBar border-3" type="text" placeholder="Search Location" v-model="text">

      <div class="Clear absolute hover-pointer" @click="text = ''">
        <simple-svg fill="black" :filepath="require('~/assets/icon/close.svg')"/>
      </div>
    </div>

    <div class="AreaList flex-column">
      <div class="Area" v-for="area in display" :key="area.areaId" @click="onArea(area)">
        <h5 class="text-ellipsis-1l">{{area.name}}</h5>
      </div>
    </div>

    <div class="flex-center mtb-24" v-if="loading">
      <beat-loader color="#084E69" size="14px"/>
    </div>

    <div class="mt-24 flex-end">
      <button class="border" @click="$emit('cancel')">Cancel</button>
    </div>
  </portal>
</template>

<script>
  import _ from 'lodash'
  import {pluck, filter, debounceTime, partition, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchFilterAreaDialog",
    data() {
      return {
        text: '',
        loading: true,
        areas: [],
        searched: []
      }
    },
    mounted() {
      this.$api.get('/search/filter/areas')
        .then(({data}) => {
          this.loading = false
          this.areas.push(..._.sortBy(data, (s) => s.name))
        })
        .catch(err => {
          this.$store.dispatch('addError', err)
        })
    },
    methods: {
      onArea(area) {
        this.$store.dispatch('filter/reset')
        this.$store.dispatch('filter/location', {areas: [area], type: 'Where'})
        this.$store.dispatch('search/start')
        this.$track.search(`Search - Location: Enter Location`, this.$store.getters['search/locationType'])
      },
    },
    computed: {
      display() {
        return this.searched ? this.searched : this.areas
      }
    },
    subscriptions() {
      return {
        searched: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          debounceTime(200),
          distinctUntilChanged(),
          map((text) => {
            return _.filter(this.areas, (a) => a.name.toLowerCase().startsWith(text.toLowerCase()))
          })
        )
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchTextBar {
    border: 1px solid rgba(0, 0, 0, 0.2);

    position: relative;
    width: 100%;
    height: 38px;
    overflow: visible;

    .TextBar {
      overflow: visible;

      position: absolute;
      background-color: #FFFFFF;
      border: none transparent;
      width: 100%;
      font-size: 17px;
      height: 36px;
      padding: 0 16px;
      line-height: 2;

      color: rgba(0, 0, 0, 0.6);

      &:focus {
        outline: none;
        color: black;
      }
    }

    .Clear {
      right: 0;
      width: 38px;
      height: 38px;
      padding: 10px;
    }
  }

  .AreaList {
    overflow-y: scroll;
    overflow-x: hidden;
    height: 60vh;
  }

  .Area {
    padding-top: 6px;
    padding-bottom: 6px;
    min-width: 375px;
  }
</style>
