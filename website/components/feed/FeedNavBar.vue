<template>
  <nav class="FeedNavBar hr-bottom container">
    <nuxt-link :to="`/feed/${link}`" class="hover-pointer secondary-500-hover" v-for="link in links" :key="link"
               :class="
    route === `feed-${link}` ? 'secondary-500' : 'black-a-75'">
      {{link}}
    </nuxt-link>
  </nav>
</template>

<script>
  export default {
    name: "FeedNavBar",
    data() {
      return {
        links: ['images', 'articles']
      }
    },
    computed: {
      route() {
        return this.$route.name
      },
    }
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
      font-size: 14px;
      font-weight: 700;

      margin-right: 32px;

      text-decoration: initial !important;
    }
  }
</style>
