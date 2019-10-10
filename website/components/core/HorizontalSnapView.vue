<!-- @deprecated -->

<template>
  <div>
    <div class="relative" ref="container">
      <div class="ArrowButton flex-align-center absolute wh-100">
        <div @click="prev" v-if="hasPrev"
             class="Control Prev elevation-1 relative index-navigation hover-pointer"/>
        <div @click="next" v-if="hasNext"
             class="Control Next elevation-1 relative index-navigation hover-pointer"/>
      </div>


      <div class="ContentListParent mtb-24">
        <div class="ContentList flex relative" ref="scrollable">
          <slot>
          </slot>

          <div>
            <div class="gutter"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "HorizontalSnapView",
    props: {
      cardWidth: {
        type: [Number],
        required: true
      }
    },
    data() {
      return {
        hasNext: true,
        hasPrev: false,
      }
    },
    mounted() {
      this.notifyButton(this.$refs.scrollable.scrollLeft)
    },
    methods: {
      prev() {
        let scrollable = this.$refs.scrollable
        let scrollLeft = scrollable.scrollLeft - this.cardWidth
        scrollable.scrollLeft = scrollLeft

        this.notifyButton({scrollLeft})
      },
      next() {
        let scrollable = this.$refs.scrollable
        let scrollLeft = scrollable.scrollLeft + this.cardWidth
        scrollable.scrollLeft = scrollLeft

        this.notifyButton({scrollLeft})
      },
      notifyButton({scrollLeft}) {
        this.hasPrev = scrollLeft > 0
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentListParent {
    overflow-y: hidden;

    margin-left: -24px;
    margin-right: -24px;
    margin-bottom: -24px;
    width: calc(100% + 48px);
    height: 100%;
  }

  .ContentList {
    padding-bottom: 24px;
    margin-bottom: -24px;

    overflow-x: scroll;
    overflow-y: hidden;
    scroll-behavior: smooth;
    -webkit-overflow-scrolling: touch;
    scroll-snap-type: x mandatory;

    > * {
      scroll-snap-align: start;
      flex-shrink: 0;
    }
  }

  .ArrowButton {
    visibility: hidden;
  }

  @media (min-width: 792px) {
    .ContentList {
      overflow-x: hidden;
    }

    .ArrowButton {
      visibility: initial;
    }
  }

  @media (min-width: 1200px) {
    .ContentListParent {
      margin-left: -12px;
      margin-right: -12px;
      width: calc(100% + 24px);
    }

    .ContentList {
      display: flex;
    }
  }
</style>

<style scoped lang="less">
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
