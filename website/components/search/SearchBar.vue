<template>
  <div v-on-clickaway="onBlur" class="SearchBar border-3 cubic-bezier no-select"
       :class="{'Extended': isExtended, 'elevation-2': searching}">
    <div class="SearchTextBar relative w-100">
      <input id="globalSearchBar" ref="input" class="TextBar bg-white absolute wh-100" type="text"
             @focus="onFocus" @keyup.up="onKeyUp" @keyup.down="onKeyDown" @keyup.enter="onKeyEnter"
             placeholder="Search e.g. Italian in Orchard" v-model="text">

      <div class="Clear absolute hover-pointer none" :class="{block: text.length}" @click="onClear">
        <simple-svg fill="black" :filepath="require('~/assets/icon/close.svg')"/>
      </div>
    </div>

    <div class="SearchSuggest bg-white absolute elevation-3 index-top-elevation" v-if="isExtended">
      <div class="Results index-top-elevation">
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

            <search-bar-default-item
              v-else-if="item.type === 'default'"
              :name="item.object"/>
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
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  import SearchBarAssumptionItem from "./items/SearchBarAssumptionItem";
  import SearchBarPlaceItem from "./items/SearchBarPlaceItem";
  import SearchBarSuggestItem from "./items/SearchBarSuggestItem";
  import SearchBarDefaultItem from "./items/SearchBarDefaultItem";

  export default {
    name: "SearchBar",
    components: {SearchBarDefaultItem, SearchBarSuggestItem, SearchBarPlaceItem, SearchBarAssumptionItem},
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
        if (!this.text) {
          return [
            {type: 'default', object: 'Feed'},
            {type: 'default', object: 'Nearby'},
            {type: 'default', object: 'Anywhere'}
          ]
        }

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
        if (this.searching) {
          if (this.suggestions === null) return false;
          return true;
        }
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
        if (!this.items) return

        this.onItem(this.items[this.position])
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

          case 'default':
            return this.onDefault({name: object})
        }
      },
      onDefault({name}) {
        switch (name) {
          case 'Nearby':
            this.$store.commit('filter/loading', true)
            this.onBlur()

            return this.$getLocation({
              enableHighAccuracy: true,
              timeout: 8000,
              maximumAge: 15000
            }).then(coordinates => {
              return this.$store.commit('filter/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
            }).then(() => {
              this.$store.commit('filter/loading', false)
              return this.$store.dispatch('filter/location', {type: 'Nearby'})
            }).then(() => {
              return this.$store.dispatch('search/start')
            }).catch(error => {
              return this.$store.dispatch('addMessage', {
                type: 'error',
                title: 'Location Not Available',
                message: 'Is location service enabled?'
              })
            })

          case 'Anywhere':
            this.$store.dispatch('filter/location', {type: 'Anywhere'})
            this.$store.dispatch('search/start')
            return this.onBlur()

          case 'Feed':
            this.$router.push({path: '/feed/images'})
            return this.onBlur()
        }
      },
      onFocus() {
        this.searching = true
        this.position = 0
        this.$emit('onFocus')

        if (this.suggestions === null && this.text && this.text.length > 0) {
          this.$axios.$post('/api/suggest', {"text": text, "searchQuery": {}}, {progress: false})
            .then(suggestions => {
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
      const observable = this.$watchAsObservable('text').pipe(
        pluck('newValue'),
        tap(() => this.suggestions = null),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        distinctUntilChanged(),
        debounceTime(333),
      )

      const suggestions = observable.pipe(
        switchMap((text) => {
          return this.$axios.$post('/api/suggest', {"text": text, "searchQuery": {}}, {progress: false})
        }),
        map(({data}) => data)
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
