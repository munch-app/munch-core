<template>
  <div v-on-clickaway="onBlur" class="SearchBar border-3 cubic-bezier no-select"
       :class="{'Extended': isExtended, 'elevation-2': searching}">
    <div class="SearchTextBar relative w-100">
      <input ref="input" class="TextBar bg-white absolute wh-100" type="text"
             @focus="onFocus" @keyup.up="onKeyUp" @keyup.down="onKeyDown" @keyup.enter="onKeyEnter"
             placeholder="Search e.g. Italian in Marina Bay" v-model="text">

      <div class="Clear absolute hover-pointer none" :class="{block: text.length}" @click="onClear">
        <simple-svg fill="black" :filepath="require('~/assets/icon/close.svg')"/>
      </div>
    </div>

    <div class="SearchSuggest bg-white absolute elevation-3 index-top-elevation" v-if="isExtended">
      <search-bar-navigation v-if="isNavigation" @on-blur="onBlur"/>

      <div class="Results index-top-elevation" v-if="suggestions">
        <div class="Items" v-if="items">
          <div class="Item hover-pointer" v-for="(item, index) in items" :key="item.key"
               @click="onItem(item)"
               :class="{'Position': index === position}">

            <search-bar-suggest-item
              v-if="item.type === 'suggest'"
              :suggest="item.object"/>

            <search-bar-assumption-item
              v-else-if="item.type === 'assumption'"
              :assumption="item.object" :highlight="index === position"/>

            <search-bar-place-item
              v-else-if="item.type === 'place'"
              :place="item.object"/>
          </div>
        </div>

        <div v-else>
          <div class="p-16 text">
            Sorry! We couldn't find results for <span class="weight-600 b-a85">{{text}}</span>.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {merge} from 'rxjs';
  import {partition, pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  import SearchBarNavigation from "./bar/SearchBarNavigation";
  import SearchBarAssumptionItem from "./items/SearchBarAssumptionItem";
  import SearchBarPlaceItem from "./items/SearchBarPlaceItem";
  import SearchBarSuggestItem from "./items/SearchBarSuggestItem";

  export default {
    name: "SearchBar",
    components: {SearchBarSuggestItem, SearchBarPlaceItem, SearchBarAssumptionItem, SearchBarNavigation},
    data() {
      return {
        text: '',
        suggestions: null,
        position: 0,
        searching: false
      }
    },
    watch: {
      loading() {
        this.text = ''
      }
    },
    computed: {
      ...mapGetters('search', ['loading']),
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
      items() {
        const items = [
          // Assumption Items
          ...(this.assumptions || []).map(assumption => {
            return {type: 'assumption', key: `a-${assumption.count}`, object: assumption}
          }),
          // Place Items
          ...(this.places || []).map(place => {
            return {type: 'place', key: `p-${place.placeId}`, object: place}
          })
        ]

        if (items.length > 0) {
          return items
        }

        if (items.length === 0 && this.suggests) {
          return [{type: 'suggest', key: 's-', object: this.suggests[0]}]
        }
      },
      /**
       * @returns {boolean} whether search bar is extended to drop down
       */
      isExtended() {
        if (!this.searching) return false

        return this.isNavigation || this.suggestions
      },
      /**
       * @returns {boolean} whether to show navigation bar
       */
      isNavigation() {
        return !this.text
      },
    },
    methods: {
      onKeyUp() {
        if (this.position > 0) {
          this.position -= 1
        }
      },
      onKeyDown() {
        if (this.position < this.items.length) {
          this.position += 1
        }
      },
      onKeyEnter() {
        if (!this.items) {
          return this.onBlur()
        }

        this.onItem(this.items && this.items[this.position])
      },
      onItem({type, object}) {
        switch (type) {
          case 'suggest':
            return this.text = object

          case 'assumption':
            this.$store.dispatch('search/start', object.searchQuery)
            this.$track.search(`Search - Assumption`, this.$store.getters['search/locationType'])
            return this.onBlur()

          case 'place':
            this.$router.push({path: '/places/' + object.placeId})
            this.$track.view(`RIP`, 'Suggest')
            return this.onBlur()
        }
      },
      onFocus() {
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
        this.$refs.input.blur()
        this.$emit('onBlur', this.text)
      },
      onClear() {
        this.text = ''
        this.suggestions = null
        this.position = 0
        this.$emit('onText', this.text)
      },
    },
    subscriptions() {
      const [server, local] = this.$watchAsObservable('text').pipe(
        pluck('newValue'),
        map((text) => text.trim()),
        debounceTime(333),
        distinctUntilChanged(),

        // Partition into 2 branch, server & local request
        partition(text => text !== '')
      )

      const suggestions = merge(
        server.pipe(
          switchMap((text) => {
            return this.$axios.$post('/api/search/suggest', {
              "text": text,
              "searchQuery": {
                "filter": {},
                "sort": {}
              }
            }, {progress: false})
          }),
          map(({data}) => data))
        ,
        local.pipe(map((text) => null))
      )

      return {suggestions}
    }
  }
</script>

<style scoped lang="less">
  .SearchBar {
    border: 1px solid rgba(0, 0, 0, 0.08);

    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1), 0 1px 2px rgba(0, 0, 0, 0.1);
    &:hover {
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    }
  }

  @media (min-width: 768px) {
    .Extended {
      border-radius: 3px 3px 0 0;
    }
  }

  .SearchTextBar {
    height: 38px;
  }

  .TextBar {
    overflow: visible;

    border: none transparent;
    font-size: 17px;
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

  .SearchSuggest {
    border: 1px solid rgba(0, 0, 0, 0.08);
    margin-left: -1px;

    @media (max-width: 767.98px) {
      position: fixed;
      margin-top: 8px;
      left: 0;
      right: 0;

      border-radius: 0;
    }

    @media (min-width: 768px) {
      width: 500px;
      margin-top: 0;
      border-radius: 0 0 3px 3px;
    }
  }

  @Whisper100: #F0F0F8;

  .Item {
    padding: 8px 16px;

    &:hover, &.Position {
      background: @Whisper100;
    }
  }
</style>
