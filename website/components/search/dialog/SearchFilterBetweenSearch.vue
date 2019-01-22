<template>
  <div :class="{OffScreen: !searching}">
    <div class="wh-100 flex-column mt-16">
      <div class="flex-align-center flex-justify-between">
        <div class="SearchTextBar border-3 hover-pointer">
          <input ref="input" class="TextBar" type="text" placeholder="Search Here" v-model="text">
        </div>
        <div>
          <simple-svg @click.native="onCancel" class="wh-24px hover-pointer ml-16" fill="black"
                      :filepath="require('~/assets/icon/close.svg')"/>
        </div>
      </div>

      <div class="mtb-24 flex-grow Result">
        <div class="text-ellipsis-1l SuggestCell hr-bot text" v-for="(location, index) in suggestions"
             :key="index" @click="onLocation(location)"
             :class="{'bg-whisper100': position === index}"
        >
          {{location.name}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchFilterBetweenSearch",
    data() {
      return {
        searching: false,
        text: '',
        position: 0,

        suggestions: [],
      }
    },
    mounted() {
      document.addEventListener('keyup', this.onKeyUp)
    },
    beforeDestroy() {
      document.removeEventListener('keyup', this.onKeyUp)
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
            return this.$axios.$post('/api/search/filter/between/search', {text}, {progress: false})
          }),
          map(({data}) => data)
        )
      }
    },
    methods: {
      onKeyUp(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.position !== 0) this.position -= 1
            break

          case 40: // Down
            if (this.position !== this.suggestions.length - 1) this.position += 1
            break

          case 13: // Enter
            if (this.suggestions && this.position < this.suggestions.length) {
              this.onLocation(this.suggestions[this.position])
            }
            break
        }
      },
      start() {
        this.searching = true
        this.text = ''
        this.position = 0

        this.$refs.input.focus()
      },
      onCancel() {
        this.searching = false
        this.suggestions = []
        this.text = ''
        this.position = 0
      },
      onLocation(point) {
        this.onCancel()

        this.$store.commit('filter/updateBetweenLocation', {point})
        this.$store.dispatch('filter/location', {type: 'Between'})
      },
    }
  }
</script>

<style scoped lang="less">
  .SearchTextBar {
    border: 1px solid rgba(0, 0, 0, 0.2);

    position: relative;
    width: 100%;
    height: 38px;
    overflow: visible;
  }

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

  .Result {
    overflow-y: auto;
    // 48 + 172
    height: calc(90vh - 260px);

    @media(min-width: 768px) {
      height: calc(80vh - 260px);
    }
  }

  .SuggestCell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;

    height: 40px;
    line-height: 40px;
    padding: 0 16px;
  }

  .OffScreen {
    top: -10000000px;
    position: absolute;
  }
</style>
