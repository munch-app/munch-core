<template>
  <horizontal-scroll-view class="PlaceAwardList container-remove-gutter" :items="awards" :map-key="a => a.itemId"
                          :padding="18">
    <template slot-scope="{item}">
      <div class="Award flex-center bg-whisper100 border-3">
        <span class="text-center text-ellipsis-2l weight-600 b-a80">{{item.award.name}}</span>
      </div>
    </template>
  </horizontal-scroll-view>
</template>

<script>
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "PlaceAwardList",
    components: {HorizontalScrollView},
    props: {
      awards: {
        type: Array,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceAwardList {
    height: 48px;
  }

  .Award {
    width: 172px;
    height: 48px;
    padding: 0 8px;
  }

  span {
    max-height: 33px;
    font-size: 13px;
    line-height: 1.25;
  }
</style>
