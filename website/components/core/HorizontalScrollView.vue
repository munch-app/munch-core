<template>
  <div class="HorizontalScrollViewWrapper" :class="{'container-no-gutter': container}">
    <div @click="onPrev" v-if="hasPrev" class="ScrollControlPrev index-navigation hover-pointer"/>
    <div @click="onNext" v-if="hasNext" class="ScrollControlNext index-navigation hover-pointer"/>

    <div class="HorizontalScrollView zero-spacing index-content" :class="{container}">
      <div ref="scrollArea" class="ScrollArea index-content" :style="container ? '' : 'margin: 0 -15px'">
        <div class="Content" :style="{paddingLeft: `${15-padding/2}px`, paddingRight: `${15-padding/2}px`}">
          <div class="Item" :style="{paddingLeft: `${padding/2}px`, paddingRight: `${padding/2}px`}"
               v-for="item in items" :key="mapKey(item)">
            <slot :item="item">{{item}}</slot>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
    },
    data() {
      return {windowWidth: 0, scrollWidth: 0, distance: 0, offset: 0}
    },
    mounted() {
      this.windowWidth = window.innerWidth <= 1200 ? window.innerWidth : 1200
      this.distance = this.windowWidth * 0.7 // Distance to cover per nav click
      this.scrollWidth = this.$refs.scrollArea.scrollWidth
    },
    computed: {
      hasPrev() {
        return this.offset > 0
      },
      hasNext() {
        return this.windowWidth < this.scrollWidth
      },
    },
    methods: {
      onPrev() {
        const scrollArea = this.$refs.scrollArea
        scrollArea.scrollLeft = scrollArea.scrollLeft - this.distance
        this.offset = scrollArea.scrollLeft - this.distance
      },
      onNext() {
        const scrollArea = this.$refs.scrollArea
        scrollArea.scrollLeft = scrollArea.scrollLeft + this.distance
        this.offset = scrollArea.scrollLeft + this.distance
      }
    }
  }
</script>

<style scoped lang="less">
  .HorizontalScrollView {
    height: 100%;
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
      scroll-behavior: smooth;
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

  .HorizontalScrollViewWrapper {
    height: 100%;
    overflow-y: visible;
    position: relative;

    .ScrollControlPrev, .ScrollControlNext {
      @media (max-width: 767.98px) {
        display: none;
      }

      position: absolute;
      height: 100%;
      width: 48px;

      transition: all 0.3s cubic-bezier(.25, .8, .25, 1);

      &::after {
        position: absolute;
        content: "";

        top: calc(50%);
        margin-top: -6px;
        height: 14px;
        width: 14px;
        border-right: 2px solid black;
        border-bottom: 2px solid black;
      }
    }

    .ScrollControlPrev {
      left: -25px;

      &::after {
        left: 20px;
        transform: rotate(135deg);
      }
    }

    .ScrollControlNext {
      right: -25px;

      &::after {
        right: 20px;
        transform: rotate(-45deg);
      }
    }
  }
</style>
