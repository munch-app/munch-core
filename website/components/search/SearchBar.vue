<template>
  <div class="SearchBar">
    <input class="SearchTextBar Elevation1 Border3" type="text" :placeholder="placeholder" v-model="text"
           @keyup="onKeyUp" @focus="onFocus">
  </div>
</template>

<script>
  import MunchButton from "../core/MunchButton";
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import SearchSuggest from "../layouts/SearchSuggest";
  import SearchFilter from "../layouts/SearchFilter";

  export default {
    name: "SearchBar",
    components: {SearchFilter, SearchSuggest, MunchButton},
    props: {
      placeholder: {
        type: String,
        required: false,
        default: () => 'Search e.g. Italian in Marina Bay'
      }
    },
    data() {
      return {
        text: "",
        isSuggest: false,
        isFilter: false
      }
    },
    computed: {},
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
  .SearchBar {

  }

  .SearchTextBar {
    background-color: #FFFFFF;
    border: none transparent;
    width: 100%;
    z-index: 1000;
    font-size: 13px;
    padding: 7px 12px;
    height: 32px;

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
