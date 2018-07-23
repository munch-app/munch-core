<template>
  <div class="Downloads">
    <div class="OverlayBanner">
      <section class="Banner">
        <nav-header class="NavBar" :search-enabled="false"></nav-header>
        <search-bar class="SearchTextBar" :font-size="17" :height="42" padding="8px 12px"/>
      </section>
    </div>
  </div>
</template>

<script>
  import SearchBar from "../components/search/SearchBar";
  import NavHeader from "../components/NavHeader";

  export default {
    components: {
      NavHeader,
      SearchBar
    },
    layout: 'empty'
  }
</script>

<style scoped lang="less">
  .OverlayBanner {
    background: url("/img/downloads/banner.jpg");
    background-position: center center;
    background-size: cover;

    height: 100vh;
    width: 100vw;
  }

  .Banner {
    background-color: #00000060;
    display: flex;
    justify-content: center;
    height: 100%;
    width: 100%;
  }

  .NavBar {
    position: fixed;
    width: 100vw;
  }

  .SearchTextBar {
    padding: auto;
    align-self: center;
  }

  @media only screen and (max-width: 500px) {
    .SearchTextBar {
      width: 80%;
    }
  }

  @media only screen and (min-width: 500px) {
    .SearchTextBar {
      width: 40%;
    }
  }
</style>
