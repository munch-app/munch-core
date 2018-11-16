<template>
  <portal to="dialog-full">
    <div ref="dialog" class="DialogNavigation" @click.self="close">
      <div class="DialogContent" :style="{maxWidth}">
        <div class="DialogControl DialogPrev">
          <div class="DialogControlButton flex-center" @click="prev">
            <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/prev.svg')"/>
          </div>
        </div>

        <slot></slot>

        <div class="DialogControl DialogNext">
          <div class="DialogControlButton flex-center" @click="next">
            <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/next.svg')"/>
          </div>
        </div>

        <div class="DialogControl DialogClose">
          <div class="DialogControlButton flex-center" @click="close">
            <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/close.svg')"/>
          </div>
        </div>
      </div>
    </div>
  </portal>
</template>

<script>
  import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

  export default {
    name: "DialogNavigation",
    props: {
      maxWidth: {
        type: String
      }
    },
    mounted() {
      this.$navigation = (evt) => {
        switch (evt.keyCode) {
          case 37: // Left
            this.prev()
            return
          case 39: // Right
            this.next()
            return

          case 8:
          case 27:
            this.close()
            return
        }
      }
      document.addEventListener('keyup', this.$navigation)
      disableBodyScroll(this.$refs.dialog)
    },
    beforeDestroy() {
      document.removeEventListener('keyup', this.$navigation)
    },
    methods: {
      next() {
        this.$emit('next')
        disableBodyScroll(this.$refs.dialog)
      },
      prev() {
        this.$emit('prev')
        disableBodyScroll(this.$refs.dialog)
      },
      close() {
        this.$emit('close')
        clearAllBodyScrollLocks()
      }
    }
  }
</script>

<style scoped lang="less">
  .DialogNavigation {
    background: rgba(0, 0, 0, 0.4);
    width: 100vw;
    height: 100vh;

    padding-top: 48px;
    padding-bottom: 80px;

    position: fixed;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    .DialogContent {
      margin-left: auto;
      margin-right: auto;

      background: white;

      display: flex;
    }

    .DialogControl {
      .DialogControlButton {
        position: fixed;

        width: 80px;
        height: 100px;
        top: calc(50% - 50px);
      }

      .Icon {
        width: 36px;
        height: 36px;
      }

      &.DialogPrev .DialogControlButton {
        margin-left: -80px;
      }

      &.DialogClose {
        .Icon {
          width: 28px;
          height: 28px;
        }

        .DialogControlButton {
          top: 0;
          height: 48px;
        }
      }
    }
  }

  @media (max-width: 575.98px) {
    .DialogNavigation {
      padding-bottom: 0;
    }

    .DialogContent {
      width: 100vw;
    }
  }

  @media (min-width: 576px) {
    .DialogContent {
      width: 576px;
      border-radius: 3px;
    }
  }

  @media (max-width: 735.98px) {
    .DialogControl {
      z-index: 1000000000;
      &.DialogClose .DialogControlButton {
        right: -12px;
      }

      &.DialogPrev .DialogControlButton {
        left: -12px;
        margin-left: 0 !important;
      }

      &.DialogNext .DialogControlButton {
        right: -12px;
      }
    }
  }

  @media (min-width: 768px) {
    .DialogContent {
      width: 80%;
    }
  }
</style>
