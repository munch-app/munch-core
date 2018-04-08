<template>
  <div>
    <nav-header></nav-header>
    <nuxt/>
    <nav-bottom></nav-bottom>
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

<style>
  body {
    font-family: 'Open Sans', sans-serif;
  }
</style>
