<template>
  <div>
    <nav class="Header NavBg Elevation1 IndexHeader">
      <div class="Container clearfix">
        <header-logo class="Logo float-left" @click="onClickLogo"/>
        <header-profile class="Profile float-right" @click="onClickProfile"/>
      </div>

      <header-menu class="Menu"/>
    </nav>
    <div style="height: 56px"/>

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

  export default {
    components: {
      HeaderMenu, HeaderProfile, HeaderLogo
    },
    computed: {
      ...mapGetters('layout', ['isElevated']),
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
