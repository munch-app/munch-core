<template>
  <div class="container-1400 mt-24 mb-64">
    <h1 v-if="error.statusCode === 404">{{error.message || 'Page Not Found'}}</h1>
    <h1 v-else>{{error.message}}</h1>

    <nuxt-link class="mt-16 hover-pointer" to="/">Home Page</nuxt-link>
  </div>
</template>

<script>
  export default {
    props: ['error']
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: underline;
    font-size: 17px;
  }
</style>
