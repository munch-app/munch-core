<template>
  <div class="zero">
    <div class="container ImageWall">
      <div class="Header">
        <h2>Feed</h2>
        <p>Feast your eyes with fresh out the oven food shots by your favorite Instagrammers</p>
      </div>

      <masonry-wall ref="masonry" :items="items" @append="onAppend"
                    :options="{lanes:{2:{padding: 8}},min:2, ssr: {default: wall.lanes}}"
                    :persistence="{getter: 'feed/images/persistence', commit: 'feed/images/persistence'}">
        <template slot-scope="{item, index}">
          <div @click="selected = index">
            <feed-image-card :item="item"/>
          </div>
        </template>
      </masonry-wall>
    </div>

    <no-ssr class="flex-center" style="padding: 64px 0 64px 0">
      <beat-loader color="#084E69" v-if="more" size="14px"/>
    </no-ssr>

    <dialog-navigation v-if="selectedItem" @prev="selected--" @next="onNext" @close="selected = -1"
                       max-width="600px">
      <feed-selected-instagram-dialog v-if="selectedItem.instagram" :item="selectedItem"/>
    </dialog-navigation>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedImageCard from "../../components/feed/FeedImageCard";
  import ImageSize from "../../components/core/ImageSize";
  import FeedSelectedInstagramDialog from "../../components/feed/FeedSelectedInstagramDialog";
  import MasonryWall from "../../components/core/MasonryWall";
  import DialogNavigation from "../../components/layouts/DialogNavigation";

  export default {
    components: {DialogNavigation, MasonryWall, FeedSelectedInstagramDialog, ImageSize, FeedImageCard},
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: `Feed · Munch Singapore`,
          description: 'Feast your eyes with fresh out the oven food shots by your favorite Instagrammers',
          url: `https://www.munch.app/feed/images`,
          keywords: 'feed,images,food,discovery,munch,munch app,singapore,food',
        },
        breadcrumbs: [
          {
            name: 'Feed',
            item: 'https://www.munch.app/feed'
          },
          {
            name: 'Images',
            item: `https://www.munch.app/feed/images`
          },
        ]
      })
    },
    asyncData({store, isMobileOrTablet}) {
      return store.dispatch('feed/images/start')
        .then(() => {
          return {
            wall: {
              lanes: isMobileOrTablet ? 2 : 4
            }
          }
        })
    },
    data() {
      return {
        selected: -1
      }
    },
    computed: {
      ...mapGetters('feed/images', ['items', 'more']),
      selectedItem() {
        return this.items[this.selected]
      },
    },
    methods: {
      onAppend(then) {
        this.$store.dispatch('feed/images/append').then(then)
      },
      onNext() {
        this.selected++
        this.$refs.masonry.scrollTo(this.selected)
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageWall {
    .Header {
      margin-top: 24px;
      margin-bottom: 24px;

      p {
        padding-top: 8px;
        padding-bottom: 8px;
      }
    }
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
