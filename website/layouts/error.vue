<template>
  <div class="container pt-24 pb-64">
    <h1 v-if="error.statusCode === 404">{{error.message || 'Page Not Found'}}</h1>
    <h1 v-else>{{error.message}}</h1>

    <div class="mt-16">
      <nuxt-link to="/">Home Page</nuxt-link>
    </div>
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
