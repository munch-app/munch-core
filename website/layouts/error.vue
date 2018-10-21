<template>
  <div class="container Error">
    <h1 v-if="error.statusCode === 404">{{error.message || 'Page Not Found'}}</h1>
    <h1 v-else>{{error.message}}</h1>

    <nuxt-link class="HomePage hover-pointer" to="/">Home Page</nuxt-link>
  </div>
</template>

<script>
  export default {
    props: ['error']
  }
</script>

<style scoped lang="less">
  .Error {
    padding-top: 24px;
    padding-bottom: 64px;

    h1 {
      margin-bottom: 16px;
    }

    .HomePage {
      text-decoration: underline;
      font-size: 17px;
    }
  }
</style>
