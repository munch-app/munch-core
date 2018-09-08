<template>
  <div class="Container Error">
    <h1 v-if="error.statusCode === 404">Page Not Found</h1>
    <h1 v-else>An error occurred</h1>

    <nuxt-link to="/">Home Page</nuxt-link>
  </div>
</template>

<script>
  export default {
    props: ['error']
  }
</script>

<style scoped lang="less">
  .Error {
    margin-top: 24px;
  }
</style>
