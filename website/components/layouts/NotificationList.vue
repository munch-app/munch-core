<template>
  <div class="NotificationList mr-24 mb-24 fixed index-overlay" v-if="notifications">
    <div v-for="notification in notifications" :key="notification.id">
      <div class="NotificationBox border-4 relative elevation-2 cubic-bezier"
           :class="{
           'Message': notification.type === 'message',
           'Error': notification.type === 'error'
           }">
        <div>
          <div class="heading" v-if="notification.title">
            {{notification.title}}
          </div>
          <div class="text" v-if="notification.message">
            {{notification.message}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NotificationList",
    computed: {
      notifications() {
        if (this.$store.state.notifications) {
          return this.$store.state.notifications
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .NotificationList {
    right: 0;
    bottom: 0;
  }

  .heading,
  .text {
    margin-bottom: 8px;
  }
  .heading {
    font-size: 15px;
    color: rgba(0, 0, 0, 0.85);
  }
  .text {
    font-size: 15px;
  }

  .NotificationBox {
    width: 300px;
    padding: 16px 20px;
    margin-top: 12px;

    > div {
      margin-bottom: -8px;
    }

    animation-name: move-up;
    animation-duration: 400ms;

    @keyframes move-up {
      0% {
        top: 40px;
      }
      100% {
        top: 0;
      }
    }
  }

  .Error {
    background: #ffb4a2;
  }

  .Message {
    background: #fdfdfd;
    border: 1px solid rgba(0, 0, 0, .1);
  }

  @media (max-width: 575.98px) {
    .NotificationList {
      left: 0;
      right: 0;
      margin: 0;
    }

    .NotificationBox {
      width: 100vw;
      border-radius: 0;
      margin-top: 0;
    }

    .Error {
      border-top: 1px solid white;
    }

    .Message {
      border-style: solid none none none;
    }
  }
</style>
