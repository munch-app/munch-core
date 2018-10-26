<template>
  <div class="zero-spacing">
    <!--<feed-nav-bar/>-->

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
      <div class="LoadingIndicator flex-center" v-if="more"
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
  import FeedArticleCard from "../../components/feed/FeedArticleCard";
  import FeedNavBar from "../../components/feed/FeedNavBar";
  import FeedSelectedArticleDialog from "../../components/feed/FeedSelectedArticleDialog";

  export default {
    components: {FeedSelectedArticleDialog, FeedNavBar, FeedArticleCard},
    head() {
      const meta = [
        {name: 'robots', content: `follow,index`}
      ]
      return {title: `Article · Munch Feed`, meta}
    },
    data() {
      return {
        tags: [
          'Recent Articles',
          'Tag: Bar',
          'Tag: Halal',
          'Tag: Chinese'
        ],
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

  nav.ArticleFeedNav {
    width: 180px;
    height: 100vh;

    top: 56px;
    position: sticky;

    padding-top: 24px;

    > div {
      font-size: 16px;
      font-weight: 600;

      margin-bottom: 10px;
      line-height: 16px;
      height: 16px;
    }
  }

  .ArticleFeedList {
    margin: 8px auto -24px;

    .ArticleFeedItem {
      padding-top: 16px;
      padding-bottom: 16px;
    }
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
