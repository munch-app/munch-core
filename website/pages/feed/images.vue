<template>
  <div class="zero-spacing">
    <div class="container ImageWall">
      <masonry-wall ref="masonry" :items="items" @append="onAppend" :min="2"
                    :options="{lanes:{2:{padding: 9}}}"
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
      const meta = [
        {name: 'robots', content: `follow,index`}
      ]
      return {title: `Images Feed · Munch Singapore`, meta}
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
