<template>
  <div class="HorizontalScrollViewWrapper" :class="{'container-full': container}">
    <div class="Navigation" :class="{container: container}" v-if="nav">
      <div @click="prev" v-if="hasPrev" class="ScrollControlPrev index-navigation hover-pointer"
           :class="{NavWhite: navWhite}"/>
      <div @click="next" v-if="hasNext" class="ScrollControlNext index-navigation hover-pointer"
           :class="{NavWhite: navWhite}"/>
    </div>
    <div class="HorizontalScrollView zero index-content" :class="{container}">
      <div ref="scrollArea" class="ScrollArea index-content">
        <div class="Content" :style="style.content">
          <div v-if="container">
            <div class="gutter"/>
          </div>

          <div class="Item" :style="style.item"
               v-for="(item, index) in items" :key="mapKey(item)">
            <slot :item="item" :index="index">{{item}}</slot>
          </div>

          <div v-if="container">
            <div class="gutter"/>
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
      },
      nav: {
        type: Boolean,
        default: () => true
      },
      navWhite: {
        type: Boolean,
        default: () => false
      },
    },
    data() {
      return {windowWidth: 0, scrollWidth: 0, distance: 0, offset: 0}
    },
    mounted() {
      this.windowWidth = window.innerWidth <= 1200 ? window.innerWidth : 1200
      this.distance = this.windowWidth * 0.7 // Distance to cover per nav click
      this.scrollWidth = this.$refs.scrollArea.scrollWidth

      this.notify()
    },
    computed: {
      hasPrev() {
        return this.offset > 0
      },
      hasNext() {
        return this.windowWidth < this.scrollWidth
      },
      style() {
        return {
          content: {
            marginLeft: `-${this.padding / 2}px`,
            marginRight: `-${this.padding / 2}px`
          },
          item: {
            paddingLeft: `${this.padding / 2}px`,
            paddingRight: `${this.padding / 2}px`
          }
        }
      }
    },
    methods: {
      prev() {
        const scrollArea = this.$refs.scrollArea
        scrollArea.scrollLeft = scrollArea.scrollLeft - this.distance
        this.offset = scrollArea.scrollLeft - this.distance
        this.notify()
      },
      next() {
        const scrollArea = this.$refs.scrollArea
        scrollArea.scrollLeft = scrollArea.scrollLeft + this.distance
        this.offset = scrollArea.scrollLeft + this.distance
        this.notify()
      },
      notify() {
        this.$emit('notify', {hasNext: this.hasNext, hasPrev: this.hasPrev})
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

    &> .ScrollArea {
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: -24px;

      overflow-x: scroll;
      overflow-y: hidden;
      -webkit-overflow-scrolling: touch;
      scroll-behavior: smooth;

      &> .Content {
        display: flex;

        .Item {
          flex-shrink: 0;
        }
      }
    }
  }

  .Navigation {
    position: absolute;
    height: 100%;
    width: 100%;
    display: flex;

    .ScrollControlPrev, .ScrollControlNext {
      position: relative;
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

    .ScrollControlPrev, .ScrollControlNext {
      &.NavWhite::after {
        border-right: 2px solid white;
        border-bottom: 2px solid white;
      }
    }

    .ScrollControlPrev {
      margin-left: -3px;
      margin-right: auto;

      &::after {
        margin-right: 12px;
        transform: rotate(135deg);
      }
    }

    .ScrollControlNext {
      margin-left: auto;
      margin-right: -25px;

      &::after {
        margin-left: 12px;
        transform: rotate(-45deg);
      }
    }
  }

  .HorizontalScrollViewWrapper {
    overflow-y: visible;
    position: relative;
  }
</style>
