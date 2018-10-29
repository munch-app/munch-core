<template>
  <div class="zero-spacing">
    <!--<feed-nav-bar/>-->

    <div class="container ImageWall">
      <masonry-wall ref="masonry" :items="items" @append="onAppend" :min="2">
        <template slot-scope="{item, index}">
          <div @click="selected = index">
            <feed-image-card :item="item"/>
          </div>
        </template>
      </masonry-wall>
    </div>

    <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
      <beat-loader color="#084E69" v-if="more" size="14px"/>
    </no-ssr>

    <feed-selected-instagram-dialog v-if="selectedItem && selectedItem.instagram" :item="selectedItem"
                                    @next="onNext" @prev="selected--" @close="selected = -1"
    />
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedImageCard from "../../components/feed/FeedImageCard";
  import FeedNavBar from "../../components/feed/FeedNavBar";
  import ImageSize from "../../components/core/ImageSize";
  import FeedSelectedInstagramDialog from "../../components/feed/FeedSelectedInstagramDialog";
  import MasonryWall from "../../components/core/MasonryWall";

  export default {
    components: {MasonryWall, FeedSelectedInstagramDialog, ImageSize, FeedNavBar, FeedImageCard},
    head() {
      const meta = [
        {name: 'robots', content: `follow,index`}
      ]
      return {title: `Images · Munch Feed`, meta}
    },
    async fetch({store}) {
      return store.dispatch('feed/images/start')
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
    margin-top: 24px;
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
