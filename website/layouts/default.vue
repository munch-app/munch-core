<template>
  <div>
    <nav class="Header NavBg Elevation1 IndexHeader">
      <div class="HeaderRow Container clearfix">
        <header-logo class="Logo float-left" @click="onClickLogo"/>
        <header-right class="HeaderMenu float-right" @clickMenu="onClickMenu"/>
      </div>

      <header-menu class="Menu"/>
    </nav>
    <div style="height: 56px"/>

    <profile-on-boarding v-if="isFocused('Login')"/>

    <div :class="{'ElevationOverlay IndexContentOverlay': isElevated}"></div>
    <nuxt :class="{ElevationBlur: isElevated}"/>

    <nav class="Footer IndexFooter">
    </nav>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderRight from "../components/layouts/HeaderRight";
  import HeaderMenu from "../components/layouts/HeaderMenu";

  export default {
    components: {
      HeaderMenu,
      HeaderRight, HeaderLogo
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
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

    .HeaderRight {
      height: 56px;
    }
  }

  .Footer {

  }
</style>
