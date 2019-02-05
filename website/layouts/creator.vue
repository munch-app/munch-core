<template>
  <div class="Default flex-column" :lang="'en'">
    <nav class="Header fixed w-100 index-top-elevation hr-bot bg-white">
      <div class="container-1200 flex-between h-100">
        <header-logo class="mr-8"/>
        <div class="flex-grow">
          <h3 class="CreatorName text-ellipsis-1l">{{creatorName}}</h3>
        </div>
        <header-profile/>
      </div>
    </nav>

    <div style="height: 56px"/>
    <header-menu class="container-1200"/>

    <layout-default/>

    <!-- Elevation overlay for content -->
    <div @click="() => {}" :class="{'elevation-overlay index-content-overlay': isElevated}"></div>
    <nuxt class="flex-grow" :class="{'elevation-blur': isElevated}"/>
  </div>
</template>

<script>
  if (process.browser) require('intersection-observer')
  import {mapGetters} from 'vuex'

  import LayoutDefault from "../components/layouts/LayoutDefault";
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import HeaderProfile from "../components/layouts/HeaderProfile";

  export default {
    components: {
      LayoutDefault, HeaderProfile, HeaderMenu, HeaderLogo
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    methods: {},
  }
</script>

<style scoped lang="less">
  .Default {
    min-height: 100vh;
  }

  .Header {
    top: 0;
    height: 56px;
  }

  .CreatorName {
    margin: 8px;
    height: 28px;
  }
</style>
