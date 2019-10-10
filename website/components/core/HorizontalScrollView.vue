<!-- @deprecated -->

<template>
  <div class="ScrollWrapper relative" :class="{'container-full': container}">
    <div class="flex-align-center absolute wh-100" :class="{container: container}" v-if="nav">
      <div @click="prev" v-if="hasPrev"
           class="Control Prev elevation-1 relative index-navigation hover-pointer"/>
      <div @click="next" v-if="hasNext"
           class="Control Next elevation-1 relative index-navigation hover-pointer"/>
    </div>


    <div ref="scrollView" class="ScrollView h-100 relative zero index-content" :class="{container}">
      <div ref="scrollArea" class="ScrollArea absolute-0 index-content">
        <div ref="contentArea" class="inline-flex" :style="style.content">
          <div v-if="container">
            <div class="gutter"/>
          </div>

          <div class="flex-shrink" :style="style.item"
               v-for="(item, index) in items" :key="mapKey(item, index)">
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
    },
    data() {
      return {
        contentWidth: 0,
        scrollWidth: 0,
        distance: 0, // Distance to cover per nav click
        offset: 0 // Current offset
      }
    },
    mounted() {
      this.contentWidth = this.$refs.contentArea.scrollWidth
      this.scrollWidth = this.$refs.scrollView.scrollWidth
      this.distance = this.scrollWidth * 0.7


      this.notify()
    },
    computed: {
      hasPrev() {
        return this.offset > 0
      },
      hasNext() {
        return this.scrollWidth < this.contentWidth
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
  .ScrollWrapper {
    overflow-y: visible;
  }

  .ScrollView {
    overflow-x: visible;
    overflow-y: hidden;
  }

  .ScrollArea {
    bottom: -24px;

    overflow-x: scroll;
    overflow-y: hidden;
    scroll-behavior: smooth;
    -webkit-overflow-scrolling: touch;
  }

  .Control {
    width: 46px;
    height: 46px;

    border-radius: 23px;
    background: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);

    &::after {
      position: absolute;
      content: "";

      top: calc(50%);
      margin-top: -6px;
      height: 14px;
      width: 14px;
      border-right: 2px solid rgba(0, 0, 0, 0.85);
      border-bottom: 2px solid rgba(0, 0, 0, 0.85);
    }
  }

  .Control.Prev {
    margin-left: -18px;
    margin-right: auto;

    &::after {
      margin-left: 19px;
      transform: rotate(135deg);
    }
  }

  .Control.Next {
    margin-left: auto;
    margin-right: -18px;

    &::after {
      margin-left: 13px;
      transform: rotate(-45deg);
    }
  }
</style>
