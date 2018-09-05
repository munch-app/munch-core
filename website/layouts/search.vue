<template>
  <div>
    <div class="NavHeader NavBg Elevation1">
      <b-container class="Row">
        <a href="/"><img class="Logo" src="/img/MunchLogo.svg"></a>
        <search-bar class="SearchTextBar"/>
      </b-container>
    </div>
    <nuxt/>
  </div>
</template>

<script>
  import SearchBar from "../components/layouts/SearchBar";

  export default {
    components: {SearchBar}
  }
</script>

<style scoped lang="less">
  .NavHeader {
    height: 64px;
    z-index: 1000;

    .Row {
      display: flex;
      flex-direction: row;
    }
  }

  .Logo {
    margin-top: 16px;
    margin-bottom: 16px;
    height: 32px;
    width: 32px;
  }

  .SearchTextBar {
    margin-top: 16px;
    margin-bottom: 16px;
    margin-left: 24px;
  }

  @media only screen and (max-width: 600px) {
    .SearchTextBar {
      flex-grow: 2;
      margin-right: 24px;
    }
  }

  @media only screen and (min-width: 600px) {
    .SearchTextBar {
      width: 480px;
    }
  }
</style>
