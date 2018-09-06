<template>
  <div>
    <nav class="Header NavBg Elevation1">
      <div class="container clearfix">
        <header-logo class="Logo float-left" @click="onClickLogo"/>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>

      <header-menu class="Menu" :show="isMenu" @onHidden="isMenu = false"/>
    </nav>
    <div class="HeaderSpace"/>

    <nuxt/>
    <nav class="Footer">
    </nav>
  </div>
</template>

<script>
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderProfile from "../components/layouts/HeaderProfile";
  import HeaderMenu from "../components/layouts/HeaderMenu";

  export default {
    components: {
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
  .HeaderSpace {
    height: 64px;
  }

  .Header {
    position: fixed;
    top: 0;
    height: 64px;
    width: 100%;

    .Logo {
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
