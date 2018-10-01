<template>
  <div class="Default">
    <nav class="Header index-top-elevation hr-bottom white-bg">
      <div class="HeaderRow container clearfix">
        <header-logo class="Logo" :class="{'IsSuggest': isFocused('Suggest')}"/>
        <div class="Search">
          <search-bar class="SearchBar" @onText="onText" @onBlur="onBlur" @onFocus="onFocus"/>
        </div>
        <header-right class="HeaderMenu"/>
      </div>
    </nav>

    <div style="height: 56px"/>
    <header-menu/>

    <div style="height: 48px" v-if="isSearch"/>
    <search-bar-filter v-if="isSearch"/>

    <!-- Dialog manager for system -->
    <dialog-portal/>

    <!-- Notification List on the right -->
    <notification-list/>

    <!-- Elevation overlay for content -->
    <div :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="Content" :class="{'elevation-blur': isElevated}"/>

    <nav-footer/>
  </div>
</template>

<script>
  import 'sanitize.css'
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

  export default {
    components: {
      NavFooter, NotificationList, DialogPortal, ProfileOnBoarding, HeaderRight, SearchBarFilter,
      SearchBar, HeaderMenu, HeaderLogo
    },
    data() {
      return {text: ""}
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
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
      onText(text) {
        this.text = text
      },
      onFocus() {
        this.$router.push({path: '/search'})
        this.$store.commit('focus', 'Suggest')
      },
      onBlur() {
        if (this.isFocused('Suggest')) {
          this.$store.commit('unfocus', 'Suggest')
        }
      }
    },
    watch: {
      route() {
        // this.$store.commit('clearFocus')
      }
    }
  }
</script>

<style lang="less" scoped>
  .Default {
    display: flex;
    min-height: 100vh;
    flex-direction: column;

    .Content {
      flex: 1;
    }
  }

  .Header {
    position: fixed;
    top: 0;
    height: 56px;
    width: 100%;

    .HeaderRow {
      display: flex;

      .Logo {
        margin-right: 8px;
      }

      .Search {
        margin: 8px 0 8px 0;
        flex-grow: 1;
      }

      @media (max-width: 767.98px) {
        .HeaderMenu, .IsSuggest {
          display: none;
        }
      }

      @media (min-width: 768px) {
        .Search {
          margin-left: 8px;
          margin-right: 8px;
        }

        .SearchBar {
          width: 500px;
        }
      }
    }
  }
</style>
