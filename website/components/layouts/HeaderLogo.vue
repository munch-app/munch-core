<template>
  <div class="HeaderLogo relative">
    <div class="MobileCaret absolute wh-100" @click="$store.commit('toggleFocus', 'HeaderMenu')"/>
    <a href="/" class="flex-align-center h-100 hover-pointer">
      <img v-if="clear" class="ImageLogo" src="~/assets/img/MunchLogoWhite.svg">
      <img v-else class="ImageLogo" src="~/assets/img/MunchLogo.svg">
      <simple-svg class="Indicator" :fill="clear ? '#fff': '#000'" :filepath="require('~/assets/icon/caret-down.svg')"/>
    </a>
  </div>
</template>

<script>
  export default {
    name: "HeaderLogo",
    props: {
      clear: {
        type: Boolean,
        default: false
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageLogo {
    height: 36px;
    width: 36px;
  }

  .Indicator {
    height: 16px;
    width: 16px;
    margin-top: -4px;
  }

  @media (min-width: 768px) {
    .Indicator,
    .MobileCaret {
      display: none;
    }
  }
</style>
