<template>
  <nav class="FeedNavBar hr-bottom container">
    <nuxt-link :to="`/feed/${name}`" class="hover-pointer black-a-85 secondary-500-hover" v-for="name in ['images', 'articles']" :key="name">
      {{name}}
    </nuxt-link>
  </nav>
</template>

<script>
  export default {
    name: "FeedNavBar"
  }
</script>

<style scoped lang="less">
  @height: 48px;

  .FeedNavBar {
    display: flex;
    height: @height;

    > a {
      line-height: @height;
      height: @height;

      text-transform: uppercase;
      font-size: 15px;
      font-weight: 600;

      margin-right: 32px;

      text-decoration: initial !important;
    }
  }
</style>
