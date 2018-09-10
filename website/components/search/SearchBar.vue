<template>
  <div v-on-clickaway="onBlur">
    <div class="SearchBar NoSelect">
      <input ref="input" class="TextBar" :class="{'Extended': isExtended}" type="text"
             :placeholder="placeholder" v-model="text" @keyup="onKeyUp" @focus="onFocus">

      <div class="Clear" :style="clearStyle" @click="onClear">
        <simple-svg fill="black" filepath="/img/search/close.svg"/>
      </div>
    </div>

    <div class="SearchSuggest Elevation1 Border24Bottom NoSelect" v-if="isExtended">
      <div class="Results">
        <div class="Item" :class="{'OnPosition': position === item.position}" v-for="item in items"
             :key="item.position" @click="onClick(item)">
          <simple-svg class="Icon" fill="black" :filepath="`/img/search/${item.type}.svg`"/>
          <div class="Content">
            <div class="Place Text" v-if="item.type === 'place'">
              <span class="Name">{{item.place.name}}</span>
              <span class="Location">, {{item.place.location.neighbourhood}}</span>
            </div>

            <div class="Assumption Text" v-if="item.type === 'assumption'">
              <div class="Token Border24 TagBg" v-if="item.type === 'assumption'"
                   v-for="token in item.assumption.tokens" :key="token.type + token.text">
                <span class="TokenText">{{token.text}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchBar",
    components: {},
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
            this.$store.commit('search/update', item.assumption.searchQuery)
            this.$router.push({path: '/search'})
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
          debounceTime(300),
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
    position: relative;
    width: 100%;
    height: 40px;

    .TextBar {
      border-radius: 3px;
      box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.20), 0 2px 3px 0 rgba(0, 0, 0, 0.13);

      position: absolute;
      background-color: #FFFFFF;
      border: none transparent;
      width: 100%;
      font-size: 17px;
      height: 40px;
      padding: 0 16px;
      line-height: 1;

      color: rgba(0, 0, 0, 0.6);

      &:focus {
        outline: none;
        color: black;
      }

      &.Extended {
        border-radius: 3px;

        @media (min-width: 768px) {
          border-radius: 3px 3px 0 0;
        }
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
    z-index: 200;
    background: white;
    position: absolute;

    @media (max-width: 767.98px) {
      position: fixed;
      margin-top: 12px;
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

  .Place {
    height: 26px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    span.Name {
      font-weight: 600;
    }

    span.Location {

    }
  }

  .Assumption {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .Token {
      height: 26px;
      margin-right: 8px;

      .TokenText {
        font-size: 14px;
        font-weight: 500;
        margin: auto 12px;
      }
    }
  }
</style>
