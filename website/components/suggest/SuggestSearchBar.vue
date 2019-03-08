<template>
  <div v-on-clickaway="onBlur" class="SearchBar border-3 cubic-bezier no-select"
       :class="{'Extended': isExtended}">
    <div class="SearchTextBar relative">
      <input id="suggestSearchBar" ref="input" class="TextBar bg-white absolute wh-100" type="text"
             @focus="onFocus" @keyup.up="onKeyUp" @keyup.down="onKeyDown" @keyup.enter="onKeyEnter"
             placeholder="Search..." v-model="text" autocomplete="off">

      <div class="Clear absolute hover-pointer none" :class="{block: text.length}" @click="onClear">
        <simple-svg fill="black" :filepath="require('~/assets/icon/close.svg')"/>
      </div>
    </div>

    <div class="SearchSuggest bg-white absolute" v-if="isExtended">
      <div class="Results">
        <div class="Items" v-if="items">
          <div class="Item hover-pointer" v-for="(item, index) in items" :key="item.key"
               @click="onItem(item)"
               :class="{'Position': index === position}">

            <search-bar-place-item
              v-if="item.type === 'place'"
              :place="item.object"/>

            <search-bar-default-item
              v-else-if="item.type === 'default'"
              :name="item.object"/>
          </div>
        </div>

        <div v-else-if="searching && text.length > 1">
          <div class="p-16 text">
            Searching for <span class="weight-600 b-a85">{{text}}</span>...
          </div>
        </div>

        <div v-else-if="!searching && text.length > 1">
          <div class="p-16 text">
            Sorry! We couldn't find results for <span class="weight-600 b-a85">{{text}}</span>.
          </div>
        </div>

        <div v-else>
          <div class="p-16 text">
            Enter at least 2 characters to start searching!
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue'
  import {debounceTime, distinctUntilChanged, filter, map, pluck, switchMap, tap} from 'rxjs/operators'
  import SearchBarPlaceItem from "../search/items/SearchBarPlaceItem";
  import SearchBarDefaultItem from "../search/items/SearchBarDefaultItem";

  export default {
    name: "SuggestSearchBar",
    components: {SearchBarDefaultItem, SearchBarPlaceItem},
    data() {
      return {
        text: '',
        suggestions: null,
        position: 0,
        searching: false
      }
    },
    props: {
      status: {
        type: Object,
        required: true
      },
      dialog: {
        type: Object,
        required: true
      },
    },
    watch: {
      loading() {
        this.text = ''
      }
    },
    computed: {
      places() {
        if (this.suggestions && this.suggestions && this.suggestions.length > 0) {
          return this.suggestions.slice(0, 10)
        }
      },
      items() {
        const items = [
          ...(this.places || []).map(place => {
            return {type: 'place', key: `p-${place.placeId}`, object: place}
          })
        ]

        if (items.length > 0) {
          return items
        }
      },

      isExtended() {
        return this.searching || this.items || this.text;
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
      },
      onItem({type, object}) {
        switch (type) {
          case 'place':
            Vue.set(this.dialog, "duplicate", false)
            Vue.set(this.dialog, "status", false)
            Vue.set(this.status, "placeId", object.placeId)
            Vue.set(this.status, "placeName", object.name)
            Vue.set(this.status, "type", 'duplicated')
        }
      },
      onFocus() {
        this.position = 0
        this.$emit('onFocus')

        if (this.suggestions === null && this.text && this.text.length > 0) {
          this.$axios.$post('/api/suggest', {"text": this.text, "searchQuery": {}}, {progress: false})
            .then(data => {
              this.suggestions = data.places
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
        tap(() => this.searching = true),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        distinctUntilChanged(),
        debounceTime(333),
      )

      const suggestions = observable.pipe(
        switchMap((text) => {
          let promise = this.$axios.$post('/api/suggest', {"text": text, "searchQuery": {}}, {progress: false})
          promise.then(() => {
            this.searching = false
          })
          return promise
        }),
        map(({data}) => data.places)
      )
      return {suggestions}
    }
  }
</script>

<style scoped lang="less">
  .SearchBar {
    border: 1px solid rgba(0, 0, 0, 0.08);
    max-width: 280px;
    margin-left: 0 !important;
  }

  @media (min-width: 768px) {
    .Extended {
      border-radius: 3px 3px 0 0;
    }
  }

  .SearchTextBar {
    height: 24px;
  }

  .TextBar {
    overflow: visible;

    border: none transparent;
    font-size: 14px;
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
      width: 80%;
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
