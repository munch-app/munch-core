<template>
  <div class="zero-spacing">
    <feed-nav-bar/>

    <div class="container">
      <div class="ImageFeed">
        <div class="ImageFeedLane" v-for="(lane, index) in lanes" :key="index">
          <div class="ImageFeedItem" v-for="i in lane.indexes" :key="i" @click="selected = i">
            <feed-image-card :item="items[i]"/>
          </div>

          <no-ssr v-if="!lane.requested" style="flex-grow:1">
            <div v-observe-visibility="{callback: (v) => visibilityChanged(v,index),throttle:2}"/>
          </no-ssr>
        </div>
      </div>
    </div>

    <div v-if="selected !== -1">
      <feed-selected-instagram-dialog @close="selected = -1" v-if="items[selected].instagram" :item="items[selected]"/>
    </div>

    <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
      <beat-loader color="#084E69" v-if="more" size="14px"/>
    </no-ssr>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import FeedImageCard from "../../components/feed/FeedImageCard";
  import FeedNavBar from "../../components/feed/FeedNavBar";
  import ImageSize from "../../components/core/ImageSize";
  import FeedSelectedInstagramDialog from "../../components/feed/FeedSelectedInstagramDialog";

  export default {
    components: {FeedSelectedInstagramDialog, ImageSize, FeedNavBar, FeedImageCard},
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
        lanes: [],
        cursor: 0,
        selected: -1
      }
    },
    computed: {
      ...mapGetters('feed/images', ['items', 'more'])
    },
    mounted() {
      this.$nextTick(() => {
        let index = Math.round(window.innerWidth / 300)
        if (index < 2) index = 2

        this.lanes.splice(0, this.lanes.length)
        for (let i = 0; i < index; i++) {
          this.lanes.push({indexes: [], requested: true})
        }

        this.fill()
      })
    },
    methods: {
      visibilityChanged(isVisible, index) {
        if (!isVisible) return
        this.lanes[index].requested = true

        this.$nextTick(() => {
          this.fill()
        })

        if (this.cursor < this.items.length) return

        return this.$store.dispatch('feed/images/append')
          .then(() => {
            this.$nextTick(() => {
              this.fill()
            })
          })
      },
      fill() {
        this.lanes.forEach(lane => {
          const index = this.cursor
          if (lane.requested && this.items[this.cursor]) {
            lane.indexes.push(index)
            lane.requested = false
            this.cursor = this.cursor + 1
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageFeed {
    display: flex;

    margin: 12px -12px;

    .ImageFeedLane {
      flex-grow: 1;
      flex-basis: 0;

      padding-left: 12px;
      padding-right: 12px;

      display: flex;
      flex-direction: column;
    }
  }

  .ImageFeedItem {
    padding-top: 12px;
    padding-bottom: 12px;
  }

  .LoadingIndicator {
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
