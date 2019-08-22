<template>
  <div class="Default flex-column" :lang="'en'">
    <nav class="Header fixed w-100 index-top-elevation hr-bot bg-white">
      <div class="container h-100 flex-align-center">
        <div class="flex mr-8">
          <header-logo class="mr-16"/>
          <h3>Admin System</h3>
        </div>
        <div class="mlr-8 mtb-8 flex-grow">
          <portal-target name="HeaderMiddle"/>
        </div>
        <header-right class="HeaderRight"/>
        <header-profile/>
      </div>
    </nav>

    <div class="Header"/>
    <header-menu class="container"/>

    <global-default/>

    <!-- Elevation overlay for content -->
    <div @click="() => {}" :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="Page flex-grow" :class="{'elevation-blur': isElevated}"/>
  </div>
</template>

<script>
  import HeaderProfile from "../components/layouts/HeaderProfile";

  if (process.browser) require('intersection-observer')
  import {mapGetters} from 'vuex'

  import GlobalDefault from "../components/global/GlobalDefault";
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import HeaderRight from "../components/layouts/HeaderRight";

  export default {
    components: {
      HeaderProfile, GlobalDefault, HeaderRight, HeaderMenu, HeaderLogo
    },
    head() {
      return {title: `Admin System`}
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused', 'isMunchTeam']),
      route() {
        return this.$route.name
      },
    },
    mounted() {
      if (!this.isMunchTeam) {
        return this.$router.push({path: '/'})
      }
    }
  }
</script>

<style scoped lang="less">
  .Page {
    min-height: calc(100vh - 72px);
  }

  .Header {
    top: 0;
    height: 72px /*Header72px*/;
  }

  @media (max-width: 767.98px) {
    .HeaderRight {
      display: none;
    }
  }
</style>

<style lang="less">
  table {
    border-collapse: collapse;
    margin-left: -8px;
    margin-right: -8px;
  }

  thead {
    border-bottom: 1px solid #F4F4F4;
  }

  th, td {
    border: none;
    padding: 8px;
    text-align: left;
  }

  td {
    padding-top: 16px;
    padding-bottom: 16px;

    a {
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
  tr {
    border-bottom: 1px solid #F4F4F4;
  }
</style>
