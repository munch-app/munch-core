<template>
  <div class="zero">
    <div class="container">
      <div class="mtb-24">
        <h2>Feed</h2>
        <p class="mtb-8">Never eat ‘Anything’ ever again.</p>
      </div>

      <masonry-wall ref="masonry" :items="items" @append="onAppend"
                    :options="{lanes:{2:{padding: 8}},min:2, ssr: {default: wall.lanes}}"
                    :persistence="{getter: 'feed/images/persistence', commit: 'feed/images/persistence'}">
        <template slot-scope="{item, index}">

          <div v-if="item.injected">
            <feed-image-injected-card :type="item.type"/>
          </div>

          <div v-else @click="onSelect(item, index)">
            <feed-image-card :item="item"/>
          </div>
        </template>
      </masonry-wall>
    </div>

    <no-ssr class="flex-center" style="padding: 64px 0">
      <beat-loader color="#084E69" v-if="more" size="14px"/>
    </no-ssr>

    <dialog-navigation v-if="selectedItem" @prev="selected--" @next="onNext" @close="selected = -1" max-width="1000px">
      <feed-selected-instagram-dialog v-if="selectedItem.instagram" :item="selectedItem"/>
    </dialog-navigation>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedImageCard from "../../../components/feed/images/FeedImageCard";
  import FeedSelectedInstagramDialog from "../../../components/feed/images/FeedSelectedInstagramDialog";
  import MasonryWall from "../../../components/core/MasonryWall";
  import DialogNavigation from "../../../components/layouts/DialogNavigation";
  import FeedImageInjectedCard from "../../../components/feed/images/FeedImageInjectedCard";

  export default {
    components: {
      FeedImageInjectedCard, DialogNavigation, MasonryWall, FeedSelectedInstagramDialog, FeedImageCard
    },
    head() {
      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: `Feed · Munch - Food Discovery App`,
          description: 'The Feed features the best, most delicious places in Singapore. The Feed is a food guide like you’ve never seen or tasted. A must-see must-eat guide that will always leave your stomach rumbling and mouth dripping.',
          url: `https://www.munch.app/feed/images`,
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
            },
            selected: -1
          }
        })
    },
    mounted() {
      if (this.$route.query.g === 'GB9') {
        this.$track.link('Feed Image', 'GB9: Referral Open')
        this.$router.replace({})
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
      onSelect(item, index) {
        this.$track.view('Feed Image', 'Instagram Card')
        if (window.innerWidth <= 768) {
          this.$router.push({path: `/feed/images/${item.itemId}`})
        } else {
          this.selected = index
        }
      },
      onNext() {
        this.selected++
        this.$refs.masonry.scrollTo(this.selected)
      }
    }
  }
</script>

<style scoped lang="less">
</style>
