<template>
  <div class="HeaderLogo relative">
    <div class="MobileCaret absolute wh-100" @click="$store.commit('toggleFocus', 'HeaderMenu')"/>
    <nuxt-link to="/" class="flex-align-center h-100 hover-pointer">
      <img class="ImageLogo" src="~/assets/img/MunchLogo.svg">
      <img class="Indicator" src="/img/layouts/caret-down.svg">
    </nuxt-link>
  </div>
</template>

<script>
  export default {
    name: "HeaderLogo"
  }
</script>

<style scoped lang="less">
  .ImageLogo {
    height: 36px;
    width: 36px;
  }

  .Indicator {
    height: 36px;
    width: 16px;
    padding: 10px 0 10px 4px;
  }

  @media (min-width: 768px) {
    .Indicator,
    .MobileCaret {
      display: none;
    }
  }
</style>
