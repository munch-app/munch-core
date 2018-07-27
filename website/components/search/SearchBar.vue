<template>
  <div class="SearchBar">
    <input class="SearchTextBar Elevation1"
           :class="{'Border24': !(this.isSuggest || this.isFilter), 'Border24Top': this.isSuggest || this.isFilter}"
           :style="style" type="text" :placeholder="placeholder" v-model="text" @keyup="onKeyUp" @focus="onFocus">
    <div class="SearchDropDown Elevation2 Border24Bottom">
      <search-suggest :results="results" v-if="isSuggest" @action="onSuggestAction"/>
      <search-filter v-if="isFilter" @action="onFilterAction"/>
    </div>
  </div>
</template>

<script>
  import MunchButton from "../core/MunchButton";
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import SearchSuggest from "./SearchSuggest";
  import SearchFilter from "./SearchFilter";

  export default {
    name: "SearchBar",
    components: {SearchFilter, SearchSuggest, MunchButton},
    props: {
      placeholder: {
        required: false,
        type: String,
        default: 'Search e.g. Italian in Marina Bay'
      },
      fontSize: {
        required: false,
        type: Number,
        default: 13
      },
      height: {
        required: false,
        type: Number,
        default: 32
      },
      padding: {
        required: false,
        type: String,
        default: '7px 12px'
      }
    },
    data() {
      return {
        text: "",
        isSuggest: false,
        isFilter: false
      }
    },
    computed: {
      style() {
        return {
          'font-size': this.fontSize + 'px',
          'height': this.height + 'px',
          'padding': this.padding
        }
      }
    },
    methods: {
      onKeyUp(e) {
        this.isSuggest = !!(this.text && this.text.length > 0)
        this.isFilter = this.text === ''
        if (e.keyCode === 13) {
          this.$router.push({path: '/search', query: {query: this.text}})
          this.onSuggestAction('')
        }
      },
      onFocus() {
        this.isSuggest = this.text !== ''
        this.isFilter = this.text === ''
      },
      onSuggestAction(text) {
        this.text = text || ''
        this.isSuggest = this.text !== ''
      },
      onFilterAction() {
        this.isFilter = false
      }
    },
    subscriptions() {
      return {
        results: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          filter(text => text.length > 2),
          debounceTime(250),
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
  div.SearchBar {

  }

  input.SearchTextBar {
    background-color: #FFFFFF;
    border: none transparent;
    width: 100%;
    z-index: 1000;

    color: rgba(0, 0, 0, 0.6);

    &:focus {
      outline: none;
      color: black;
    }
  }

  .SearchDropDown {
    position: relative;
    width: 100%;
    z-index: 1000;
    background: white;
  }
</style>
