<template>
  <div :class="{OffScreen: !searching}">
    <div class="wh-100 flex-column mt-16">
      <div class="flex-align-center flex-justify-between">
        <div class="SearchTextBar border-3 hover-pointer">
          <input ref="input" class="TextBar" type="text" placeholder="Search here" v-model="text">
        </div>
        <div>
          <simple-svg @click.native="onCancel" class="wh-24px hover-pointer ml-16" fill="black"
                      :filepath="require('~/assets/icon/close.svg')"/>
        </div>
      </div>

      <div class="mtb-16 flex-grow Result relative">
        <div v-if="text !== ''">
          <div class="text-ellipsis-1l hover-pointer SuggestCell text border-3" v-for="(location, index) in suggestions"
               :key="index" @click="onLocation(location, {input: 'searched', type: 'recent'})" :class="{'bg-whisper050 weight-600': position === index}"
          >
            {{location.name}}
          </div>
        </div>

        <div class="absolute-0 p-16 flex-justify-center" v-if="loading">
          <beat-loader color="#084E69" size="14px"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map, tap} from 'rxjs/operators'
  import {mapGetters} from "vuex";
  import Vue from 'vue';


  export default {
    name: "SearchFilterBetweenSearch",
    data() {
      return {
        searching: false,
        text: '',
        position: 0,

        suggestions: [],
        loading: false,

        saved: [],
        recent: [],

        deleting: null,
        saving: null,
        savingSelected: false,
        savingType: [
          {icon: require('~/assets/icon/location/Location_Home.svg'), name: 'Home', type: 'home'},
          {icon: require('~/assets/icon/location/Location_Work.svg'), name: 'Work', type: 'work'},
          {icon: require('~/assets/icon/location/Location_Bookmark_Filled.svg'), name: 'Other', type: 'saved'},
        ]
      }
    },
    mounted() {
      document.addEventListener('keyup', this.onKeyUp)
    },
    beforeDestroy() {
      document.removeEventListener('keyup', this.onKeyUp)
    },
    subscriptions() {
      return {
        suggestions: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          filter((text) => text !== ''),
          debounceTime(200),
          tap(() => {
            this.suggestions = []
            this.loading = true
          }),
          distinctUntilChanged(),
          switchMap((text) => {
            return this.$api.post('/locations/search', {text}, {progress: false})
          }),
          tap(() => this.loading = false),
          map(({data}) => data)
        )
      }
    },
    methods: {
      onKeyUp(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.position !== 0) this.position -= 1
            break

          case 40: // Down
            if (this.position !== this.suggestions.length - 1) this.position += 1
            break

          case 13: // Enter
            if (this.suggestions && this.position < this.suggestions.length) {
              this.onLocation(this.suggestions[this.position], {input: 'searched', type: 'recent'})
            }
            break
        }
      },
      start() {
        this.searching = true
        this.text = ''
        this.position = 0

        this.$refs.input.focus()
      },
      onCancel() {
        this.searching = false
        this.suggestions = []
        this.text = ''
        this.position = 0

        this.$emit('close')
      },
      onLocation(location, {type, input}) {
        this.onCancel()

        this.$store.commit('filter/updateBetweenLocation', {point: location})
        this.$store.dispatch('filter/location', {type: 'Between'})
      },
      refresh() {
        this.saved.splice(0)
        this.recent.splice(0)
        this.loading = true
      },
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
  }

  .TextBar {
    border-radius: 3px;
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

  .SaveAsList {
    margin-top: -16px;
    margin-bottom: -16px;
  }

  .Result {
    overflow-y: auto;
    // 48 + 172
    height: calc(90vh - 260px);

    @media (min-width: 768px) {
      height: calc(80vh - 260px);
    }
  }

  .SuggestCell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;

    height: 40px;
    line-height: 40px;
    padding: 0 16px;
  }

  .OffScreen {
    top: -10000000px;
    position: absolute;
  }
</style>
