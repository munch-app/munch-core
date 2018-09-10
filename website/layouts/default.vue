<template>
  <div>
    <nav class="Header NavBg Elevation1">
      <div class="Container clearfix">
        <header-logo class="Logo float-left" @click="onClickLogo"/>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>

      <header-menu class="Menu"/>
    </nav>
    <div style="height: 56px"/>

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
      }
    }
  }
</script>

<style lang="less" scoped>
  .Header {
    z-index: 1000;
    position: fixed;
    top: 0;
    height: 56px;
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
