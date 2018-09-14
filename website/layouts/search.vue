<template>
  <div>
    <nav class="IndexHeader Header NavBg" :class="{'Elevation1': !isFilter}">
      <div class="HeaderRow Container clearfix">
        <header-logo class="Logo" :class="{'IsSuggest': isSuggest}" @click="onClickLogo"/>
        <div class="Search">
          <search-bar class="SearchBar" @onText="onText" @onFocus="onFocus" @onBlur="onBlur"/>
        </div>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>
    </nav>

    <div style="height: 56px"/>
    <header-menu class="Menu"/>

    <div style="height: 48px" v-if="isFilter"/>
    <search-bar-filter class="Filter IndexHeader" v-if="isFilter"/>

    <div :class="{'ElevationOverlay IndexContentOverlay': isElevated}"></div>
    <nuxt :class="{ElevationBlur: isElevated}"/>
    <nav class="Footer IndexFooter">
    </nav>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderProfile from "../components/layouts/HeaderProfile";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import SearchBar from "../components/search/SearchBar";
  import SearchBarFilter from "../components/search/SearchBarFilter";

  export default {
    components: {
      SearchBarFilter, SearchBar, HeaderMenu, HeaderProfile, HeaderLogo
    },
    data() {
      return {
        isSuggest: false,
        text: ""
      }
    },
    computed: {
      ...mapGetters('layout', ['isElevated']),
      isFilter() {
        return this.$route.name === 'search'
      },
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
        this.text = text
      },
      onFocus() {
        this.$router.push({path: '/search', query: {q: this.text}})
        this.isSuggest = true
        this.$store.commit('layout/elevationOn', 'suggest')
      },
      onBlur() {
        if (this.isSuggest) {
          this.$store.commit('layout/elevationOff', 'suggest')
          this.isSuggest = false
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
          width: 440px;
        }
      }
    }

    .Profile {
      display: none;

      @media (min-width: 768px) {
        display: block;
      }
    }
  }

  .ElevationOverlay {
    background: rgba(255, 255, 255, 0.88);
    position: fixed;
    overflow: hidden;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }

  .ElevationBlur {
    filter: blur(1px);
  }
</style>
