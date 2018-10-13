<template>
  <div class="zero-spacing LandingPage">
    <section class="EatBetween">
      <landing-eat-between/>
    </section>

    <section>
    </section>
  </div>
</template>

<script>
  import LandingEatBetween from "../components/landing/LandingEatBetween";
  import AppleMap from "../components/core/AppleMap";

  export default {
    components: {AppleMap, LandingEatBetween},
  }
</script>

<style scoped lang="less">
  .LandingPage {
    margin-top: -56px;
    margin-bottom: -40px;
  }

  section {
    margin-bottom: 40px;
  }
</style>
