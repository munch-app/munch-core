<template>
  <div class="SearchBar">
    <input class="SearchTextBar Elevation1" :class="{'Border24': !isDropDown, 'Border24Top': isDropDown}" :style="style"
           type="text" :placeholder="placeholder" v-model="text" @keyup="onKeyUp">
    <div class="SearchDropDown Elevation2 Border24Bottom">
      <div class="Suggest" v-if="isSuggest">
        <div class="Places">
          <div class="Item" v-for="place in places" :key="place.placeId" v-on:click="onClickPlace(place)">
            <div class="Name">{{place.name}}</div>
          </div>
        </div>
      </div>
      <div class="Filter" v-if="isFilter">
        <munch-button class="Button" text="RESET" variant="clear" size="small"></munch-button>
        <munch-button class="Button" text="SEE 0 RESTAURANTS" variant="primary" size="small"></munch-button>
      </div>
    </div>
  </div>
</template>

<script>
  import MunchButton from "./core/MunchButton";
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchBar",
    components: {MunchButton},
    props: {
      placeholder: {
        required: false,
        type: String,
        default() {
          return 'Search e.g. Italian in Marina Bay'
        }
      },
      fontSize: {
        required: false,
        type: Number,
        default() {
          return 13
        }
      },
      height: {
        required: false,
        type: Number,
        default() {
          return 32
        }
      },
      padding: {
        required: false,
        type: String,
        default() {
          return '7px 12px'
        }
      }
    },
    data() {
      return {
        text: "",
        isSuggest: false,
        isFilter: false,
        isEntered: false
      }
    },
    computed: {
      style() {
        return {
          'font-size': this.fontSize + 'px',
          'height': this.height + 'px',
          'padding': this.padding
        }
      },

      isDropDown() {
        return this.isSuggest || this.isFilter
      },

      places() {
        if (this.results) {
          return this.results.places.slice(0, 10)
        }
        return []
      }
    },
    methods: {
      onTextChange(text) {
        return this.$axios.$post('/api/search/suggest', {
          "text": text,
          "searchQuery": {
            "filter": {},
            "sort": {}
          }
        }, { progress: false })
      },
      onKeyUp(e) {
        this.isSuggest = !!(this.text && this.text.length > 0)
        if (e.keyCode === 13) {
          this.onClickPlace(this.results.places[0])
        }
      },
      onClickPlace(place) {
        this.$router.push({path: '/places/' + place.placeId})
        this.text = ""
        this.isSuggest = false
      }
    },
    subscriptions() {
      return {
        results: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          filter(text => text.length > 2),
          debounceTime(500),
          distinctUntilChanged(),
          switchMap(this.onTextChange),
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

  div.SearchDropDown {
    position: relative;
    width: 100%;
    z-index: 1000;
    background: white;

    div.Suggest {
      background: white;

      .Places {
        padding: 8px 0;

        .Item {
          padding: 8px 12px;
          &:hover {
            cursor: pointer;
          }
        }
      }
    }

    div.Filter {
      background: white;
      display: flex;
      justify-content: flex-end;

      .Button {
        margin-right: 16px;
        margin-bottom: 16px;
      }
    }
  }
</style>
