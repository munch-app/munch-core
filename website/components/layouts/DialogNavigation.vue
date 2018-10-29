<template>
  <portal to="dialog-blank">
    <div class="DialogNavigation" v-on-clickaway="close">
      <div class="NavButton Close hover-pointer" @click.stop="close">
        <simple-svg class="Icon" fill="black" filepath="/img/layouts/close.svg"/>
      </div>

      <slot></slot>

      <div class="NavButtonBottom">
        <div class="NavButton Prev hover-pointer" @click.stop="prev">
          <simple-svg class="Icon " fill="black" filepath="/img/layouts/prev.svg"/>
        </div>
        <div class="NavButton Next hover-pointer" @click.stop="next">
          <simple-svg class="Icon" fill="black" filepath="/img/layouts/next.svg"/>
        </div>
      </div>
    </div>
  </portal>
</template>

<script>
  export default {
    name: "DialogNavigation",
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
    },
    beforeDestroy() {
      document.removeEventListener('keyup', this.$navigation)
    },
    methods: {
      next() {
        this.$emit('next')
      },
      prev() {
        this.$emit('prev')
      },
      close() {
        this.$emit('close')
      }
    }
  }
</script>

<style scoped lang="less">
  @media (max-width: 575.98px) {
    .DialogNavigation {
      margin-top: 48px;
      height: calc(100vh - 48px);
      width: 100vw;
      background-color: white;
      overflow-x: scroll;

      padding-bottom: 60px;
    }

    .NavButton {
      z-index: 1;
      height: 48px;

      .Icon {
        width: 24px;
        height: 24px;
      }

      &.Close {
        background-color: white;
        top: 0;
        left: 0;
        right: 0;
        position: fixed;

        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 24px;
      }
    }

    .NavButtonBottom {
      background-color: white;
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      display: flex;

      padding-top: 6px;
      padding-bottom: 6px;

      .Prev, .Next {
        flex: 0 0 50%;
        width: 50%;

        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }

  @media (min-width: 576px) {
    .NavButton {
      position: fixed;

      &.Close {
        z-index: 1;
        top: 48px;
        width: calc(576px + 48px);

        display: flex;
        justify-content: flex-end;

        .Icon {
          width: 32px;
          height: 32px;
        }
      }

      &.Prev, &.Next {
        z-index: 0;
        top: 0;
        bottom: 0;
        width: 80px;

        display: flex;
        align-items: center;

        .Icon {
          width: 40px;
          height: 40px;
        }
      }

      &.Prev {
        left: calc(50vw - 288px - 24px - 80px);
        justify-content: flex-end;
      }

      &.Next {
        right: calc(50vw - 288px - 24px - 80px);
        justify-content: flex-start;
      }
    }
  }
</style>
