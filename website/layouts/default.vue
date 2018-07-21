<template>
  <div>
    <nav-header class="Header"></nav-header>
    <nuxt/>
    <nav-bottom class="Bottom"></nav-bottom>
  </div>
</template>

<script>
  import NavHeader from '~/components/NavHeader.vue'
  import NavBottom from '~/components/NavBottom.vue'

  export default {
    components: {
      NavHeader,
      NavBottom
    }
  }
</script>

<style lang="less" scoped>
  .Header {
    z-index: 1000;
  }

  .Bottom {
    z-index: 1000;
  }
</style>
