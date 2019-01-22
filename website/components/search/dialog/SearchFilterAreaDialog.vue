<template>
  <portal to="dialog-full">
    <div class="index-dialog">
      <div class="absolute-0 Search elevation-2 overflow-hidden">
        <div class="bg-white wh-100 flex-column">
          <div class="flex-no-shrink p-16-24 hr-bot flex-align-center flex-justify-between">
            <h3>Locations</h3>
            <simple-svg @click.native="$emit('cancel')" class="wh-24px hover-pointer" fill="black"
                        :filepath="require('~/assets/icon/close.svg')"/>
          </div>

          <div class="p-16-24">
            <div class="SearchTextBar border-3 hover-pointer">
              <input ref="input" class="TextBar" type="text" @keyup="onKeyUp" placeholder="Search Here" v-model="text">
            </div>
          </div>
          <div class="flex-center mtb-24" v-if="loading">
            <beat-loader color="#084E69" size="14px"/>
          </div>

          <div class="mlr-24 mb-24 flex-grow Result">
            <div class="SuggestCell hr-bot text" v-for="(area, index) in display"
                 :key="index" @click="onArea(area)"
                 :class="{'bg-whisper100': position === index}"
            >
              {{area.name}}
            </div>
          </div>
        </div>

        <!--<div class="AreaList flex-column">-->
          <!--<div class="Area hover-pointer" v-for="area in display" :key="area.areaId" @click="onArea(area)">-->
            <!--<h5 class="text-ellipsis-1l">{{area.name}}</h5>-->
          <!--</div>-->
        <!--</div>-->

      </div>
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
        searched: [],

        position: 0,
      }
    },
    mounted() {
      document.addEventListener('keyup', this.onKeyUp)
      this.$api.get('/search/filter/areas')
        .then(({data}) => {
          this.loading = false
          this.areas.push(..._.sortBy(data, (s) => s.name))
        })
        .catch(err => {
          this.$store.dispatch('addError', err)
        })
    },
    beforeDestroy() {
      document.removeEventListener('keyup', this.onKeyUp)
    },
    methods: {
      onKeyUp(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.position !== 0) this.position -= 1
            break

          case 40: // Down
            if (this.position !== this.display.length - 1) this.position += 1
            break

          case 13: // Enter
            if (this.display && this.position < this.display.length) {
              this.onArea(this.display[this.position])
            }
            break
        }
      },
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
  .Search {
    @media (min-width: 768px) {
      border-radius: 4px;
      width: 400px;

      top: 64px;
      bottom: 64px;

      margin-left: auto;
      margin-right: auto;
    }
  }

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


  .Result {
    overflow-y: auto;
  }

  .SuggestCell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;

    height: 40px;
    line-height: 40px;
    padding: 0 16px;
  }
</style>
