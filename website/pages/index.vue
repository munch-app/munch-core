<template>
  <div class="zero LandingPage">
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
    head() {
      return this.$head({
        graph: {
          image: 'https://s3-ap-southeast-1.amazonaws.com/munch-static/Munch+Logo.png',
          url: `https://www.munch.app/`,
          keywords: 'food,discovery,food discovery,munch,singapore,munch app,delicious,discover delicious'
        }
      })
    }
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
