<template>
  <div class="Default flex-column" :class="{'gutter-24': isSearch && showsMap}" :lang="'en'">
    <nav class="Header fixed w-100 index-top-elevation hr-bot bg-white">
      <div class="container flex">
        <header-logo class="mr-8" :class="{'IsSearching': searching}"/>
        <div class="Search mlr-8 mtb-8 flex-grow">
          <search-bar class="SearchBar" @onBlur="onBlur" @onFocus="onFocus"/>
        </div>
        <header-right class="HeaderRight"/>
      </div>
    </nav>

    <div style="height: 56px"/>
    <header-menu/>

    <div style="height: 48px" v-if="isSearch"/>
    <!-- Search Bar Filter -->
    <search-bar-filter v-if="isSearch"/>

    <!-- Dialog manager for system -->
    <dialog-portal/>

    <!-- Notification List on the right -->
    <notification-list/>

    <!-- Notification Event on the bottom -->
    <notification-event/>

    <!-- Elevation overlay for content -->
    <div @click="() => {}" :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="flex-grow" :class="{'elevation-blur': isElevated}"/>

    <nav-footer/>
  </div>
</template>

<script>
  if (process.browser) require('intersection-observer')

  import {mapGetters} from 'vuex'
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import SearchBar from "../components/search/SearchBar";
  import SearchBarFilter from "../components/search/SearchBarFilter";
  import HeaderRight from "../components/layouts/HeaderRight";
  import ProfileOnBoarding from "../components/profile/ProfileOnBoarding";
  import DialogPortal from "../components/layouts/DialogPortal";
  import NotificationList from "../components/layouts/NotificationList";
  import NavFooter from "../components/layouts/NavFooter";
  import NotificationEvent from "../components/layouts/NotificationEvent";

  export default {
    components: {
      NotificationEvent,
      NavFooter, NotificationList, DialogPortal, ProfileOnBoarding, HeaderRight, SearchBarFilter,
      SearchBar, HeaderMenu, HeaderLogo
    },
    data() {
      return {searching: false}
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
      ...mapGetters('search', ['showsMap']),
      route() {
        return this.$route.name
      },
      isSearch() {
        return this.$route.name && this.$route.name.startsWith('search')
      },
      isIndex() {
        return this.$route.name === 'index'
      }
    },
    methods: {
      onFocus() {
        this.searching = true
      },
      onBlur() {
        this.searching = false
      }
    },
  }
</script>

<style lang="less" scoped>
  .Default {
    min-height: 100vh;
  }

  .Header {
    top: 0;
    height: 56px;
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
