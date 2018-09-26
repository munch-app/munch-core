<template>
  <div v-on-clickaway="onBlur" class="elevation-1 border-3" :class="{'Extended': isExtended, 'elevation-2': searching}">
    <div class="SearchTextBar no-select">
      <input ref="input" class="TextBar" type="text"
             placeholder="Search e.g. Italian in Marina Bay" v-model="text" @focus="onFocus">

      <div class="Clear" :style="clearStyle" @click="onClear">
        <simple-svg fill="black" filepath="/img/search/close.svg"/>
      </div>
    </div>

    <div class="SearchSuggest elevation-3 index-top-elevation border-3-bottom no-select" v-if="isExtended">
      <div class="Results index-top-elevation">
        <div class="NoResult text" v-if="!hasResult && suggestions">
          Sorry! We couldn’t find results for '{{text}}'.
        </div>
        <div class="Suggest" v-if="suggests">
          <div class="SuggestCell whisper-100-bg text WhiteA85" v-for="suggest in suggests" :key="suggest"
               @click="onItemSuggest(suggest)">
            {{suggest}}
          </div>
        </div>
        <div class="Assumption" v-if="assumptions">
          <div class="Item" v-for="assumption in assumptions" :key="assumption.count"
               :class="{'Highlight': isPosition(assumption)}"
               @click="onItemAssumption(assumption)">
            <search-suggest-assumption-item :assumption="assumption" :highlight="isPosition(assumption)"/>
          </div>
        </div>

        <div class="Place" v-if="places">
          <div class="Item" v-for="place in places" :key="place.placeId" :class="{'Highlight': isPosition(place)}"
               @click="onItemPlace(place)">
            <search-suggest-place-item :place="place"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import SearchSuggestPlaceItem from "./suggest/SearchSuggestPlaceItem";
  import SearchSuggestAssumptionItem from "./suggest/SearchSuggestAssumptionItem";

  export default {
    name: "SearchBar",
    components: {SearchSuggestAssumptionItem, SearchSuggestPlaceItem},
    data() {
      return {
        text: '',
        suggestions: null,
        position: 0,
        searching: false
      }
    },
    mounted() {
      window.addEventListener('keyup', this.keyUpListener)

      // Auto focus refs.input if 'Suggest' is focused
      if (this.isFocused('Suggest')) {
        this.$refs.input.focus()
      }
    },
    watch: {
      loading() {
        this.text = ''
      }
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
      ...mapGetters('search', ['loading']),
      clearStyle() {
        return {
          'display': this.text.length > 0 ? 'block' : 'none'
        }
      },
      suggests() {
        if (this.suggestions && this.suggestions.suggests && this.suggestions.suggests.length > 0) {
          return this.suggestions.suggests
        }
      },
      assumptions() {
        if (this.suggestions && this.suggestions.assumptions && this.suggestions.assumptions.length > 0) {
          return this.suggestions.assumptions
        }
      },
      places() {
        if (this.suggestions && this.suggestions.places && this.suggestions.places.length > 0) {
          return this.suggestions.places.slice(0, 10)
        }
      },
      positions() {
        let position = 0
        return [
          ...(this.assumptions || []).map(assumption => {
            return {type: 'assumption', item: assumption, position: position++}
          }),
          ...(this.places || []).map(place => {
            return {type: 'place', item: place, position: position++}
          })
        ]
      },
      isExtended() {
        return this.searching && this.text !== ''
      },
      hasResult() {
        return this.suggests || this.assumptions || this.places
      }
    },
    methods: {
      keyUpListener(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.position === 0) break
            this.position -= 1
            break

          case 40: // Down
            if (this.position === this.positions.length - 1) break
            this.position += 1
            break

          case 13: // Enter
            if (this.positions.length < 0) {
              return this.onBlur()
            }

            const object = this.positions && this.positions[this.position]
            if (object) {
              switch (object.type) {
                case 'suggest':
                  return this.onItemSuggest(object.item)
                case 'assumption':
                  return this.onItemAssumption(object.item)
                case 'place':
                  return this.onItemPlace(object.item)
              }
            }
            break
          default:
            this.suggestions = null
            this.$emit('onText', this.text)
        }
      },
      onItemSuggest(suggest) {
        this.text = suggest
      },
      onItemAssumption(assumption) {
        this.$store.dispatch('search/start', assumption.searchQuery)

        if (this.$route.name !== 'search') this.$router.push({path: '/search'})
        this.onBlur()
      },
      onItemPlace(place) {
        this.$router.push({path: '/places/' + place.placeId})
        this.onBlur()
      },
      onFocus() {
        window.scrollTo(0, 0);
        this.searching = true
        this.position = 0
        this.$emit('onFocus')

        if (this.suggestions === null && this.text && this.text.length > 0) {
          this.$axios.$post('/api/search/suggest', {
            "text": text,
            "searchQuery": {
              "filter": {},
              "sort": {}
            }
          }, {progress: false}).then(suggestions => {
            this.suggestions = suggestions
          })
        }
      },
      onBlur() {
        this.searching = false
        this.$emit('onBlur', this.text)
      },
      onClear() {
        this.text = ''
        this.suggestions = null
        this.position = 0
        this.$emit('onText', this.text)
      },
      isPosition(object) {
        const item = this.positions && this.positions[this.position] && this.positions[this.position].item
        if (item && item.placeId && object.placeId) return item.placeId === object.placeId
        if (item && item.tokens && object.tokens) return item.count === object.count
        return false
      }
    },
    subscriptions() {
      return {
        suggestions: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          filter((text) => text !== ''),
          debounceTime(200),
          distinctUntilChanged(),
          switchMap((text) => {
            return this.$axios.$post('/api/search/suggest', {
              "text": text,
              "searchQuery": {
                "filter": {},
                "sort": {}
              }
            }, {progress: false})
          }),
          map(({data}) => data)
        )
      }
    }
  }
</script>

<style scoped lang="less">
  .Extended {
    @media (min-width: 768px) {
      border-radius: 3px 3px 0 0;
    }
  }

  .SearchTextBar {
    position: relative;
    width: 100%;
    height: 40px;

    .TextBar {
      border-radius: 3px;
      overflow: visible;

      position: absolute;
      background-color: #FFFFFF;
      border: none transparent;
      width: 100%;
      font-size: 17px;
      height: 40px;
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
      width: 40px;
      height: 40px;
      padding: 10px;

      &:hover {
        cursor: pointer;
      }
    }
  }

  @Nav2: #EFF2F7;

  .SearchSuggest {
    background: white;
    position: absolute;

    @media (max-width: 767.98px) {
      position: fixed;
      margin-top: 8px;
      left: 0;
      right: 0;
    }

    @media (min-width: 768px) {
      width: 500px;
      margin-top: 0;
    }

    .Item {
      padding: 8px 15px;
      clear: both;

      &:hover, &.Highlight {
        cursor: pointer;
        background: @Nav2;
      }
    }
  }

  .Suggest {
    padding: 8px 15px;
    clear: both;
    display: flex;
    align-items: center;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    .SuggestCell {
      font-size: 14px;
      padding: 3px 12px;
      margin-right: 10px;
      border-radius: 12px;

      white-space: nowrap;
      overflow: visible;

      &:hover {
        cursor: pointer;
      }
    }
  }

  .NoResult {
    padding: 8px 15px;
  }
</style>
