<template>
  <div>
    <nav class="Header NavBg Elevation1">
      <div class="HeaderRow container clearfix">
        <header-logo class="Logo" :class="{'IsSuggest': isSuggest}" @click="onClickLogo"/>
        <div class="Search">
          <search-bar class="SearchBar" :extended="isSuggest" @onText="onText" @onFocus="onFocus" @onBlur="onBlur" ref="searchBar"/>
          <search-bar-suggest v-if="isSuggest" class="SearchBarSuggest Elevation1 Border24Bottom"
                              @onItem="onItem"
                              :suggestions="suggestions"/>
        </div>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>
      <header-menu class="Menu"/>
      <search-bar-filter class="SearchBarFilter" v-if="isFilter"/>
    </nav>
    <div class="HeaderSpace"/>
    <div class="HeaderSpace" v-if="isFilter"/>
    <nuxt/>
    <nav class="Footer">
    </nav>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderProfile from "../components/layouts/HeaderProfile";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import SearchBar from "../components/search/SearchBar";
  import SearchBarSuggest from "../components/search/SearchBarSuggest";
  import SearchBarFilter from "../components/search/SearchBarFilter";

  export default {
    components: {
      SearchBarFilter,
      SearchBarSuggest,
      SearchBar,
      HeaderMenu,
      HeaderProfile,
      HeaderLogo
    },
    data() {
      return {
        isSuggest: false,
        text: "",
        suggestions: []
      }
    },
    methods: {
      onClickLogo() {
        if (window.innerWidth < 768) {
          this.$store.commit('layout/toggleMenu')
        } else {
          this.$router.push({path: '/'})
        }
      },
      onClickProfile() {
        this.$store.commit('layout/toggleMenu')
      },
      onText(text) {
        if (text.length === 0) {
          this.suggestions = []
        }
        this.text = text
      },
      onFocus() {
        this.$router.push({path: '/search', query: {q: this.text}})
        this.isSuggest = true
      },
      onBlur() {
        this.isSuggest = false
      },
      onItem() {
        this.$refs.searchBar.blur()
      }
    },
    computed: {
      isFilter() {
        return this.$route.name === 'search'
      }
    },
    subscriptions() {
      return {
        suggestions: this.$watchAsObservable('text').pipe(
          pluck('newValue'),
          debounceTime(500),
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

<style lang="less" scoped>
  .HeaderSpace {
    height: 64px;
  }

  .Header {
    position: fixed;
    top: 0;
    height: 64px;
    width: 100%;
    z-index: 100;

    .Logo {
      display: block;
      margin-right: 8px;

      &.IsSuggest {
        display: none;

        @media (min-width: 768px) {
          display: block;
        }
      }
    }

    .HeaderRow {
      display: flex;
    }

    .Search {
      margin: 12px 8px 12px 8px;
      flex-grow: 1;
      z-index: 200;

      .SearchBar {
        z-index: 200;

        @media (min-width: 768px) {
          width: 440px;
        }
      }

      .SearchBarSuggest {
        position: absolute;
        z-index: 200;
        background: white;

        margin-top: 12px;
        left: 0;
        right: 0;

        @media (min-width: 768px) {
          width: 440px;
          margin-top: 0;
          left: initial;
          right: initial;
        }
      }
    }

    .Profile {
      display: none;

      @media (min-width: 768px) {
        display: block;
      }
    }

    .Menu {

    }
  }

  .Footer {

  }
</style>
