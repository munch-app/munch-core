<template>
  <div class="NotificationList index-overlay" v-if="notifications">
    <div v-for="notification in notifications" :key="notification.Id">
      <div class="NotificationBox Error elevation-2 Peach200Bg" v-if="notification.type === 'error'">
        <div class="heading">
          {{notification.error.name}}
        </div>
        <div class="text">
          {{notification.error.message}}
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
    position: fixed;
    right: 0;
    bottom: 0;
    margin-right: 24px;
    margin-bottom: 24px;
  }

  .NotificationBox {
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    width: 272px;
    padding: 16px 24px;
    border-radius: 4px;
    margin-top: 16px;
  }

  .Error {
    .heading {
      margin-bottom: 4px;
    }
  }
</style>
