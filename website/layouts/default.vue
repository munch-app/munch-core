<template>
  <div class="Default">
    <nav class="index-top-elevation Header nav-bg" :class="{'elevation-1': !isFilter}">
      <div class="HeaderRow container clearfix">
        <header-logo class="Logo" :class="{'IsSuggest': isFocused('Suggest')}" @click="onClickLogo"/>
        <div class="Search">
          <search-bar class="SearchBar" @onText="onText" @onFocus="onFocus" @onBlur="onBlur"/>
        </div>
        <header-right class="HeaderMenu float-right" @clickMenu="onClickMenu"/>
      </div>
    </nav>

    <div style="height: 56px"/>
    <header-menu class="Menu"/>

    <div style="height: 48px" v-if="isFilter"/>
    <search-bar-filter class="Filter index-header" v-if="isFilter"/>

    <!-- Everything under here should be overlaying elements -->
    <profile-on-boarding v-if="isFocused('Login')"/>

    <dialog-portal/>

    <notification-list/>

    <!-- Elevation overlay for content -->
    <div :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="Content" :class="{'elevation-blur': isElevated}"/>

    <nav-footer class="Footer index-footer"/>
  </div>
</template>

<script>
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
      NavFooter,
      NotificationList,
      DialogPortal, ProfileOnBoarding, HeaderRight, SearchBarFilter, SearchBar, HeaderMenu, HeaderLogo
    },
    data() {
      return {
        text: ""
      }
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
      isFilter() {
        return this.$route.name === 'search'
      },
    },
    methods: {
      onClickLogo() {
        if (window.innerWidth < 768) {
          this.$store.commit('toggleFocus', 'HeaderMenu')
        } else {
          this.$router.push({path: '/'})
        }
      },
      onClickMenu() {
        this.$store.commit('toggleFocus', 'HeaderMenu')
      },
      onText(text) {
        this.text = text
      },
      onFocus() {
        this.$router.push({path: '/search', query: {q: this.text}})
        this.$store.commit('focus', 'Suggest')
      },
      onBlur() {
        if (this.isFocused('Suggest')) {
          this.$store.commit('unfocus', 'Suggest')
        }
      }
    },
  }
</script>

<style lang="less" scoped>
  .Default {
    display: flex;
    min-height: 100vh;
    flex-direction: column;
  }

  .Header {
    position: fixed;
    top: 0;
    height: 56px;
    width: 100%;

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
      margin: 8px 0 8px 0;
      flex-grow: 1;

      @media (min-width: 768px) {
        margin: 8px 8px 8px 8px;
      }

      .SearchBar {
        @media (min-width: 768px) {
          width: 500px;
        }
      }
    }

    @media (max-width: 767.98px) {
      .HeaderMenu {
        display: none;
      }
    }
  }

  .Content {
    flex: 1;
  }

  .Footer {
  }
</style>
