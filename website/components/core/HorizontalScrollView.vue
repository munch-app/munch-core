<template>
  <div class="HorizontalScrollView zero-spacing" :class="{container}">
    <div class="ScrollArea" :style="container ? '' : 'margin: 0 -15px'">
      <div class="Content" :style="{paddingLeft: `${15-padding/2}px`, paddingRight: `${15-padding/2}px`}">
        <div class="Item" :style="{paddingLeft: `${padding/2}px`, paddingRight: `${padding/2}px`}" v-for="item in items" :key="mapKey(item)">
          <slot :item="item">{{item}}</slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  if (process.browser) require('intersection-observer')

  export default {
    name: "HorizontalScrollView",
    props: {
      container: {
        type: Boolean,
        default: () => true
      },
      padding: {
        type: Number,
        default: () => 24
      },
      items: {
        type: Array,
        required: true
      },
      mapKey: {
        type: Function,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .HorizontalScrollView {
    overflow-x: visible;
    overflow-y: hidden;
    position: relative;

    .ScrollArea {
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: -24px;

      overflow-x: scroll;
      overflow-y: hidden;
      -webkit-overflow-scrolling: touch;
    }

    .Content {
      display: flex;
      padding-left: 3px;
      padding-right: 3px;

      .Item {
        flex-shrink: 0;
      }
    }
  }
</style>
