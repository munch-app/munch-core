<template>
  <div>
    <nav class="index-header Header NavBg" :class="{'elevation-1': !isFilter}">
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

    <profile-on-boarding v-if="isFocused('Login')"/>

    <dialog-portal/>

    <div :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt :class="{'elevation-blur': isElevated}"/>

    <nav class="Footer index-footer">
    </nav>
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

  export default {
    components: {
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
</style>
