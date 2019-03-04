<template>
  <div class="Default flex-column" :class="{'gutter-24': isSearch && showsMap}" :lang="'en'">
    <nav class="Header fixed w-100 index-top-elevation hr-bot bg-white" :class="{Clear: isClear}">
      <div class="container h-100 flex-align-center">
        <header-logo class="mr-8" :clear="isClear" :class="{'IsSearching': searching}"/>
        <div class="mlr-8 mtb-8 flex-grow">
          <search-bar class="SearchBar" @onBlur="onBlur" @onFocus="onFocus"/>
        </div>
        <header-right class="HeaderRight" :clear="isClear"/>
        <header-profile/>
      </div>
    </nav>

    <div class="Header"/>
    <header-menu class="container"/>

    <div style="height: 48px" v-if="isSearch"/>
    <!-- Search Bar Filter -->
    <search-bar-filter v-if="isSearch"/>

    <layout-default/>

    <!-- Elevation overlay for content -->
    <div @click="() => {}" :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="flex-grow" :class="{'elevation-blur': isElevated}"/>

    <nav-footer/>
  </div>
</template>

<script>
  import HeaderProfile from "../components/layouts/HeaderProfile";

  if (process.browser) require('intersection-observer')
  import {mapGetters} from 'vuex'

  import LayoutDefault from "../components/layouts/LayoutDefault";
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import SearchBar from "../components/search/SearchBar";
  import SearchBarFilter from "../components/search/SearchBarFilter";
  import HeaderRight from "../components/layouts/HeaderRight";
  import NavFooter from "../components/layouts/NavFooter";

  export default {
    components: {
      HeaderProfile,
      LayoutDefault, NavFooter, HeaderRight, SearchBarFilter, SearchBar, HeaderMenu, HeaderLogo
    },
    data() {
      return {searching: false, scrolledHeight: false}
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
      ...mapGetters('search', ['showsMap', 'query']),
      route() {
        return this.$route.name
      },
      isSearch() {
        if (this.route && this.route.startsWith('search')) {
          if (this.route.startsWith('search-filter')) return false
          return true
        }
        return false
      },
      isClear() {
        if (this.isIndex) {
          if (this.scrolledHeight) return false
          return this.query && this.query.feature && this.query.feature === 'Home'
        }
        return false
      },
      isIndex() {
        return this.$route.name === 'index'
      }
    },
    mounted() {
      window.addEventListener('scroll', this.onScroll)
    },
    beforeDestroy() {
      window.removeEventListener('scroll', this.onScroll)
    },
    methods: {
      onFocus() {
        this.searching = true
      },
      onBlur() {
        this.searching = false
      },
      onScroll(event) {
        const scrolled = window.innerHeight - 240 < window.scrollY
        if (scrolled === this.scrolledHeight) return
        this.scrolledHeight = scrolled
      }
    },
  }
</script>

<style scoped lang="less">
  .Default {
    min-height: 100vh;
  }

  .Header {
    top: 0;
    height: 72px /*Header72px*/;
  }

  .Clear {
    background: rgba(0, 0, 0, 0);
    border-style: none none none none;

    .SearchBar {
      position: absolute;
      top: -1000000000px;
    }
  }

  .SearchBar {
    max-width: 500px;
  }

  @media (max-width: 767.98px) {
    .HeaderRight,
    .IsSearching {
      display: none;
    }

    .Search {
      margin-left: 0;
      margin-right: 0;
    }

    .SearchBar {
      width: auto;
    }
  }
</style>
