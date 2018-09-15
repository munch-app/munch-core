<template>
  <div v-on-clickaway="onBlur">
    <div class="SearchTextBar NoSelect" :class="{'Extended': isExtended}">
      <input ref="input" class="TextBar" type="text"
             :placeholder="placeholder" v-model="text" @keyup="onKeyUp" @focus="onFocus">

      <div class="Clear" :style="clearStyle" @click="onClear">
        <simple-svg fill="black" filepath="/img/search/close.svg"/>
      </div>
    </div>

    <div class="SearchSuggest Elevation3 IndexTopElevation Border24Bottom NoSelect" v-if="isExtended">
      <div class="Results IndexTopElevation">
        <div class="Item" :class="{'OnPosition': position === item.position}" v-for="item in items"
             :key="item.position" @click="onClick(item)">
          <simple-svg class="Icon" fill="black" :filepath="`/img/search/${item.type}.svg`"/>
          <div class="Content">
            <search-suggest-place-item v-if="item.type === 'place'" :item="item"/>
            <search-suggest-assumption-item v-if="item.type === 'assumption'" :item="item"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import SearchSuggestPlaceItem from "./suggest/SearchSuggestPlaceItem";
  import SearchSuggestAssumptionItem from "./suggest/SearchSuggestAssumptionItem";

  export default {
    name: "SearchBar",
    components: {SearchSuggestAssumptionItem, SearchSuggestPlaceItem},
    props: {
      placeholder: {
        type: String,
        required: false,
        default: () => 'Search e.g. Italian in Marina Bay'
      }
    },
    data() {
      return {
        text: '',
        suggestions: [],
        position: 0,
        searching: false
      }
    },
    mounted() {
      this.text = this.$route.query.q || ''
      this.$emit('onText', this.text)
      window.addEventListener('keyup', this.keyUpListener);
    },
    computed: {
      clearStyle() {
        return {
          'display': this.text.length > 0 ? 'block' : 'none'
        }
      },
      items() {
        if (this.suggestions) {
          let counter = 0;
          let items = []

          if (this.suggestions.assumptions) {
            this.suggestions.assumptions.forEach((assumption) => {
              items.push({
                type: 'assumption',
                position: counter,
                assumption: assumption
              })
              counter += 1
            });
          }

          if (this.suggestions.places) {
            this.suggestions.places.forEach((place) => {
              items.push({
                type: 'place',
                position: counter,
                place: place,
              })
              counter += 1
            });
          }
          if (items.length > 0) {
            return items.slice(0, 10)
          }
        }
      },
      isExtended() {
        return this.items && this.searching
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
            if (this.position === this.items.length - 1) break
            this.position += 1
            break

          case 13: // Enter
            if (this.items.length < 0) {
              this.onBlur()
              break
            }
            this.onClick(this.items[this.position])
            break
        }
      },
      onClick(item) {
        switch (item.type) {
          case 'place':
            this.$router.push({path: '/places/' + item.place.placeId})
            this.onBlur()
            break

          case 'assumption':
            this.$store.dispatch('filter/start', item.assumption.searchQuery)
            this.$store.dispatch('search/start', item.assumption.searchQuery)

            if (this.$route.name !== 'search') this.$router.push({path: '/search'})
            this.onBlur()
            break
        }
      },
      onKeyUp() {
        if (this.text.length === 0) {
          this.suggestions = []
        }
        this.$emit('onText', this.text)
      },
      onFocus() {
        window.scrollTo(0, 0);
        this.searching = true
        this.position = 0
        this.$emit('onFocus', this.text)
      },
      onBlur() {
        this.searching = false
        this.$emit('onBlur', this.text)
      },
      onClear() {
        this.text = ''
        this.suggestions = []
        this.$emit('onText', this.text)
      },
    },
    subscriptions() {
      return {
        suggestions: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          debounceTime(300), // Change this
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
  .SearchTextBar {
    position: relative;
    width: 100%;
    height: 40px;
    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.20), 0 2px 3px 0 rgba(0, 0, 0, 0.13);
    border-radius: 3px;

    &.Extended {
      border-radius: 3px;

      @media (min-width: 768px) {
        border-radius: 3px 3px 0 0;
      }
    }

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
      width: 440px;
      margin-top: 0;
    }

    .Item {
      height: 42px;
      padding: 8px 14px;
      clear: both;

      .Icon {
        float: left;
        width: 24px;
        height: 24px;
        margin: 1px 14px 1px 0;
      }

      .Content {
        height: 26px;
        width: 100%;
      }

      &:hover {
        cursor: pointer;
        background: #BEC9D0;
      }

      &.OnPosition {
        background: #BEC9D0;
      }
    }
  }
</style>
