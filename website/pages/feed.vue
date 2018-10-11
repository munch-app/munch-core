<template>
  <div class="zero-spacing FeedPage container">
    <div class="FeedContainer">
      <div class="FeedLane" v-for="(lane, index) in lanes" :key="index">
        <div class="FeedItem" :key="index" v-for="(item, index) in lane.items">
          <feed-article-card :article="item"/>
        </div>

        <no-ssr>
          <div v-observe-visibility="{callback: (v) => visibilityChanged(v, index), throttle: 100}"/>
        </no-ssr>
      </div>
    </div>

    <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
      <beat-loader color="#084E69" v-if="more" size="14px"/>
    </no-ssr>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedArticleCard from "../components/feed/FeedArticleCard";

  export default {
    components: {FeedArticleCard},
    head() {
      // Blocked from Indexing for now
      const meta = []
      meta.push({name: 'robots', content: `follow,noindex`})
      return {title: `Feed | Munch`, meta}
    },
    async fetch({store}) {
      return store.dispatch('feed/start', 1440)
    },
    computed: {
      ...mapGetters('feed', ['lanes', 'more']),
    },
    mounted() {
      this.$nextTick(() => {
        window.addEventListener('resize', () => {
          this.$store.dispatch('feed/redraw', window.innerWidth)
        });
        this.$store.dispatch('feed/start', window.innerWidth)
      })
    },
    methods: {
      visibilityChanged(isVisible, index) {
        if (isVisible) {
          this.$store.dispatch('feed/append', index)
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .FeedPage {
  }

  .FeedContainer {
    margin: 32px -24px 64px -24px;
    display: flex;

    .FeedLane {
      padding-left: 24px;
      padding-right: 24px;
      flex-grow: 1;
      flex-basis: 0;

      .FeedItem {
        padding-bottom: 48px;
      }
    }
  }
</style>
