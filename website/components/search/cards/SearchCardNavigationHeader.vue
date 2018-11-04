<template>
  <div>
    <h2>{{card.title || 'Navigation Header'}}</h2>

    <horizontal-scroll-view class="AnchorList" :items="anchors" :map-key="a => a._uniqueId"
                            :padding="24" :container="false">
      <template slot-scope="{item}">
        <div class="Anchor hover-pointer bg-whisper100 border-3" v-scroll-to="{el: '#' + item.uniqueId,offset: -100}">
          {{item.title}}
        </div>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import HorizontalScrollView from "../../core/HorizontalScrollView";

  export default {
    name: "SearchCardNavigationHeader",
    components: {HorizontalScrollView},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      anchors() {
        return this.card.anchors
      }
    }
  }
</script>

<style scoped lang="less">
  .AnchorList {
    height: 48px;
    margin-top: 16px;
  }

  .Anchor {
    line-height: 24px;
    padding: 12px 16px;

    font-weight: 600;
    font-size: 16px;
    color: rgba(0, 0, 0, 0.75);
  }
</style>
