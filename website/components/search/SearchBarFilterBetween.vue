<template>
  <div class="FilterBetween Listing">
    <div v-show="!search.searching">
      <div class="text">Simply enter everyone's location & we will find the most ideal spot for a meal together.</div>

      <div class="Locations">
        <div>
          <div class="LocationTextBar border-3 hover-pointer" v-for="(location, index) in locationPoints" :key="index">
            <div class="TextBar" @click.capture="onSearch(index)">
              <div>
                {{location.name}}
              </div>
            </div>

            <div class="Clear hover-pointer" @click="onRemove(index)">
              <simple-svg fill="black" filepath="/img/search/close.svg"/>
            </div>
          </div>

          <div class="LocationTextBar border-3 hover-pointer" v-for="(key, index) in emptyPoints" :key="key">
            <div class="TextBar" @click.capture="onSearch(index + locationPoints.length)">
              <div class="primary-500">
                {{`Enter Location ${index + 1 + locationPoints.length}`}}
              </div>
            </div>
          </div>

          <div>
            <h6>(Up to 10 locations)</h6>
          </div>
        </div>
      </div>
    </div>

    <div class="FilterBetween Search" :class="{OffScreen: !search.searching}">
      <div class="flex">
        <div class="Back hover-pointer" @click="search.searching = false">
          <simple-svg fill="black" filepath="/img/search/back.svg"/>
        </div>
        <div class="SearchTextBar border-3 hover-pointer">
          <input ref="input" class="TextBar" type="text" @keyup="onKeyUp"
                 placeholder="Search Location Name" v-model="search.text">

          <div class="Clear hover-pointer" @click="onSuggestCancel">
            <simple-svg fill="black" filepath="/img/search/close.svg"/>
          </div>
        </div>
      </div>

      <div class="Suggest">
        <div class="SuggestCell whisper-100-bg text" v-for="(location, index) in suggestions"
             :key="index" @click="onLocation(location)"
             :class="{'whisper-200-bg': search.position === index}"
        >
          {{location.name}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchBarFilterBetween",
    data() {
      return {
        focusIndex: 0,

        search: {
          text: '',
          searching: false,
          editing: 0,
          position: 0
        },
        suggestions: [],
      }
    },
    computed: {
      ...mapGetters('filter', ['locationPoints']),
      emptyPoints() {
        if (this.locationPoints.length >= 10) return []
        if (this.locationPoints.length === 0) return ['first', 'second']
        return ['last']
      }
    },
    methods: {
      onKeyUp(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.search.position === 0) break
            this.search.position -= 1
            break

          case 40: // Down
            if (this.search.position === this.suggestions.length - 1) break
            this.search.position += 1
            break

          case 13: // Enter
            if (this.suggestions && this.search.position < this.suggestions.length) {
              this.onLocation(this.suggestions[this.search.position])
            }
            break
        }
      },
      onSearch(index) {
        this.search.searching = true
        this.search.text = ''
        this.search.editing = index
        this.search.position = 0

        this.$nextTick(() => {
          // this.$refs.input.focus()
        })

        this.$refs.input.focus()
      },
      onRemove(index) {
        this.$store.commit('filter/updateBetweenLocation', {index})
      },
      onSuggestCancel() {
        if (this.search.text === '') {
          this.search.searching = false
          return
        }
        this.search.text = ''
        this.search.position = 0
        this.suggestions = []
      },
      onLocation(point) {
        const index = this.search.editing
        this.$store.commit('filter/updateBetweenLocation', {point, index})

        this.search.searching = false
        this.suggestions = []
        this.search.text = ''
        this.search.position = 0
      }
    },
    subscriptions() {
      return {
        suggestions: this.$watchAsObservable('search.text').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          filter((text) => text !== ''),
          debounceTime(200),
          distinctUntilChanged(),
          switchMap((text) => {
            return this.$axios.$post('/api/search/filter/between/search', {text}, {progress: false})
          }),
          map(({data}) => data)
        )
      }
    }
  }
</script>

<style scoped lang="less">
  .Locations {
    padding-top: 16px;
    padding-bottom: 16px;

    margin-top: -12px;
    margin-bottom: -12px;

    max-height: 440px;
    overflow-y: auto;
  }

  .LocationTextBar {
    border: 1px solid rgba(0, 0, 0, 0.2);
    margin-top: 12px;
    margin-bottom: 12px;

    position: relative;
    width: 100%;
    height: 38px;
    overflow: visible;

    .TextBar {
      border-radius: 3px;

      position: absolute;
      background-color: #FFFFFF;
      border: none transparent;
      width: 100%;
      font-size: 17px;
      height: 36px;
      padding: 0 16px;
      line-height: 2;

      color: rgba(0, 0, 0, 0.85);
      font-weight: 600;

      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;

      &:focus {
        outline: none;
        color: black;
      }
    }

    .Clear {
      position: absolute;
      background: white;
      right: 0;
      width: 36px;
      height: 36px;
      padding: 10px;
    }
  }

  .FilterBetween.Search {
    margin-top: 12px;
    margin-bottom: 12px;

    .Back {
      width: 38px;
      height: 38px;
      padding: 4px 8px 4px 0;
    }

    .SearchTextBar {
      border: 1px solid rgba(0, 0, 0, 0.2);

      position: relative;
      width: 100%;
      height: 38px;
      overflow: visible;

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

      .Clear {
        position: absolute;
        right: 0;
        width: 38px;
        height: 38px;
        padding: 10px;
      }
    }

    .Suggest {
      margin-left: 36px;
      height: 240px;
      overflow-y: auto;

      .SuggestCell {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        height: 40px;
        line-height: 40px;
        padding: 0 16px;
        margin-top: 1px;
      }
    }
  }

  .FilterBetween.OffScreen {
    position: absolute;
    top: -100000px;
  }
</style>
