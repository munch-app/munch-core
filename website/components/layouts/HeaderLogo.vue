<template>
  <div class="HeaderLogo">
    <div class="MobileAnchor" @click="$store.commit('toggleFocus', 'HeaderMenu')"/>
    <nuxt-link to="/">
      <img class="Image" src="/img/MunchLogo.svg">
      <img class="HeaderIndicator" src="/img/layouts/caret-down.svg">
    </nuxt-link>
  </div>
</template>

<script>
  export default {
    name: "HeaderLogo"
  }
</script>

<style scoped lang="less">
  .HeaderLogo {
    position: relative;
  }

  .HeaderLogo a {
    display: flex;
    height: 100%;

    margin-top: 8px;
    margin-bottom: 8px;

    &:hover {
      cursor: pointer;
    }

    .Image {
      float: left;
      height: 40px;
      width: 40px;
    }

    .HeaderIndicator {
      display: block;
      height: 40px;
      width: 16px;
      padding: 10px 0 10px 4px;

      @media (min-width: 768px) {
        display: none;
      }
    }
  }

  .MobileAnchor {
    z-index: 1;
    position: absolute;
    width: 100%;
    height: 100%;

    @media (min-width: 768px) {
      display: none;
    }
  }
</style>
