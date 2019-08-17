<template>
  <div class="zero">
    <div class="container">
      <div class="ArticleFeed">
        <div class="ArticleFeedList">
          <div class="ArticleFeedItem" v-for="(item, index) in items" :key="item.itemId">
            <feed-article-card :item="item" @view="selected = index"/>
          </div>
        </div>
      </div>
    </div>

    <no-ssr>
      <div class="flex-center" style="padding: 48px" v-if="more"
           v-observe-visibility="{callback:visibilityChanged,throttle:1000}">
        <beat-loader color="#084E69" size="14px"/>
      </div>
    </no-ssr>

    <no-ssr v-if="selected !== -1">
      <feed-selected-article-dialog @close="selected = -1" v-if="items[selected].article" :item="items[selected]"/>
    </no-ssr>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedArticleCard from "../../components/feed/articles/FeedArticleCard";
  import FeedSelectedArticleDialog from "../../components/feed/articles/FeedSelectedArticleDialog";

  export default {
    components: {FeedSelectedArticleDialog, FeedArticleCard},
    head() {
      const meta = [
        {name: 'robots', content: `follow,noindex`}
      ]
      return {title: `Article Feed · Munch - Food Discovery`, meta}
    },
    data() {
      return {
        selected: -1
      }
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
    display: flex;
  }

  .ArticleFeedList {
    margin-top: 8px;
    margin-bottom: -24px;
    margin-left: auto;
    margin-right: auto;

    .ArticleFeedItem {
      padding-top: 16px;
      padding-bottom: 16px;
    }
  }
</style>
