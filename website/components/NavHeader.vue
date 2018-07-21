<template>
  <div class="NavHeader NavBg Elevation1">
    <b-container class="Row">
      <a href="/"><img class="MunchLogo" src="/img/MunchLogo.svg"></a>
      <search-bar v-if="searchEnabled" class="SearchTextBar"/>
    </b-container>
  </div>
</template>

<script>
  import SearchBar from "./SearchBar";

  export default {
    components: {SearchBar},
    props: {
      searchEnabled: {
        required: false,
        type: Boolean,
        default() {
          return true
        }
      }
    }
  }
</script>


<style lang="less" scoped>
  .NavHeader {
    height: 64px;
    z-index: 1000;

    .Row {
      display: flex;
      flex-direction: row;
    }
  }

  .MunchLogo {
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
