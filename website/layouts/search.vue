<template>
  <div>
    <nav class="Header NavBg" :class="{'Elevation1': !isFilter}">
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
    <search-bar-filter class="Filter" v-if="isFilter"/>

    <nuxt/>
    <nav class="Footer">
    </nav>
  </div>
</template>

<script>
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderProfile from "../components/layouts/HeaderProfile";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import SearchBar from "../components/search/SearchBar";
  import SearchBarFilter from "../components/search/SearchBarFilter";

  export default {
    components: {
      SearchBarFilter,
      SearchBar,
      HeaderMenu,
      HeaderProfile,
      HeaderLogo
    },
    data() {
      return {
        isSuggest: false,
        text: ""
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
        this.isSuggest = false
      }
    },
    computed: {
      isFilter() {
        return this.$route.name === 'search'
      }
    }
  }
</script>

<style lang="less" scoped>
  .Header {
    position: fixed;
    top: 0;
    height: 56px;
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
      margin: 8px 0 8px 0;
      flex-grow: 1;
      z-index: 200;

      @media (min-width: 768px) {
        margin: 8px 8px 8px 8px;
      }

      .SearchBar {
        z-index: 200;

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
</style>
