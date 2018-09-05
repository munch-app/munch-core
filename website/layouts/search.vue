<template>
  <div>
    <nav class="Header NavBg Elevation1">
      <div class="HeaderRow container clearfix">
        <header-logo class="Logo" @click="onClickLogo"/>
        <div class="Search">
          <search-bar class="SearchBar"/>
        </div>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>

      <header-menu class="Menu" :show="isMenu" @onHidden="isMenu = false"/>
    </nav>
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

  export default {
    components: {
      SearchBar,
      HeaderMenu,
      HeaderProfile,
      HeaderLogo
    },
    data() {
      return {isMenu: false}
    },
    methods: {
      onClickLogo() {
        if (window.innerWidth < 768) {
          this.isMenu = !this.isMenu
        } else {
          this.$router.push({path: '/'})
        }
      },
      onClickProfile() {
        this.isMenu = !this.isMenu
      }
    }
  }
</script>

<style lang="less" scoped>
  .Header {
    height: 64px;

    .Logo {
    }

    .HeaderRow {
      display: flex;
    }

    .Search {
      margin: 16px 8px 16px 24px;
      flex-grow: 1;

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

    .Menu {

    }
  }

  .Footer {

  }
</style>
