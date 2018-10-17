<template>
  <div class="NotificationEvent index-overlay">
    <div class="NotificationBox EatBetweenEvent elevation-3 white-bg" v-if="eatBetween" @click="clickEatBetween">
      <div class="Header">
        <h4 class="primary-700">
          Copy Link?
        </h4>
        <simple-svg class="Icon" fill="black" filepath="/img/layouts/copy.svg"/>

        <div class="Close" @click.stop="onCancel">
          <simple-svg class="Icon" fill="black" filepath="/img/layouts/close.svg" />
        </div>
      </div>

      <h6 class="text">
        Teamwork makes the dream work. You've done the tough part now send this link to your friends and ask somebody to
        pick.
      </h6>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "NotificationEvent",
    data() {
      return {
        eatBetween: false
      }
    },
    watch: {
      searchType(searchType) {
        if (searchType !== 'search') return

        const query = this.$store.state.search.query
        if (query && query.filter && query.filter.location && query.filter.location.type === 'Between') {

          this.eatBetween = true
          setTimeout(() => {
            this.eatBetween = false
          }, 8000)
        }
      }
    },
    methods: {
      clickEatBetween() {
        this.eatBetween = false

        const dummy = document.createElement('input')
        const text = window.location.href

        document.body.appendChild(dummy)
        dummy.value = text;
        dummy.select();
        document.execCommand('copy')
        document.body.removeChild(dummy)
        this.$store.dispatch('addMessage', {message: 'Link copied!'})

        this.$gtag('event', 'share', {
          'event_category': 'link',
          'event_label': 'eat_between'
        })
      },
      onCancel() {
        this.eatBetween = false
      }
    },
    computed: {
      searchType() {
        return this.$store.state.search.type
      }
    }
  }
</script>

<style scoped lang="less">
  .NotificationEvent {
    position: fixed;
    right: 0;
    bottom: 0;
    margin-right: 24px;
    margin-bottom: 24px;
  }

  .NotificationBox {
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    width: 300px;
    padding: 16px 24px;
    border-radius: 4px;
    margin-top: 16px;

    .Header {
      display: flex;
      align-items: center;

      h3, .Icon {
        margin-bottom: 8px;
      }

      .Icon {
        width: 20px;
        height: 20px;
        margin-left: 16px;
      }

      .Close {
        margin-left: auto;
        margin-right: 0;
      }
    }

    &:hover {
      cursor: pointer;
    }
  }

  @media (max-width: 575.98px) {
    .NotificationEvent {
      left: 0;
      right: 0;
      margin: 0;
    }

    .NotificationBox {
      border-top: 1px solid rgba(0, 0, 0, 0.1);
      width: 100vw;
      border-radius: 0;
    }
  }

  @media (min-width: 576px) {
    .NotificationBox {
      border: 1px solid rgba(0, 0, 0, 0.1);
    }
  }
</style>
