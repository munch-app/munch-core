<template>
  <div class="zero-spacing">
    <feed-nav-bar/>

    <div class="ArticleFeed container">
      <div class="ArticleFeedItem" v-for="item in items" :key="item.itemId">
        <feed-article-card :item="item"/>
        <hr>
      </div>
    </div>

    <no-ssr>
      <div class="LoadingIndicator flex-center" v-if="more"
           v-observe-visibility="{callback:visibilityChanged,throttle:1000}">
        <beat-loader color="#084E69" size="14px"/>
      </div>
    </no-ssr>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedArticleCard from "../../components/feed/FeedArticleCard";
  import FeedNavBar from "../../components/feed/FeedNavBar";

  export default {
    components: {FeedNavBar, FeedArticleCard},
    head() {
      const meta = [
        {name: 'robots', content: `follow,index`}
      ]
      return {title: `Article · Munch Feed`, meta}
    },
    async fetch({store}) {
      return store.dispatch('feed/articles/start')
    },
    computed: {
      ...mapGetters('feed/articles', ['items', 'more']),
    },
    methods: {
      visibilityChanged(isVisible) {
        if (isVisible) {
          this.$store.dispatch('feed/articles/append')
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .ArticleFeed {
    .ArticleFeedItem {
      margin-top: 24px;
      margin-bottom: 24px;
    }
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
