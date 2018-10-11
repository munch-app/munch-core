<template>
  <div class="zero-spacing LandingPage">
    <section class="EatBetween">
      <landing-eat-between/>
    </section>
  </div>
</template>

<script>
  import LandingEatBetween from "../components/landing/LandingEatBetween";

  export default {
    components: {LandingEatBetween},
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
