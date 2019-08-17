<template>
  <div class="Default flex-column" :lang="'en'">

    <global-default/>
  </div>
</template>

<script>
  if (process.browser) require('intersection-observer')
  import {mapGetters} from 'vuex'

  import GlobalDefault from "../components/global/GlobalDefault";
  import HeaderLogo from "../components/layouts/HeaderLogo";
  import HeaderMenu from "../components/layouts/HeaderMenu";
  import HeaderProfile from "../components/layouts/HeaderProfile";

  export default {
    components: {
      GlobalDefault, HeaderProfile, HeaderMenu, HeaderLogo
    },
    computed: {
      ...mapGetters(['isElevated', 'isFocused']),
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
    height: 72px /*Header72px*/;
  }

  .CreatorName {
    margin: 8px;
    height: 28px;
  }
</style>
