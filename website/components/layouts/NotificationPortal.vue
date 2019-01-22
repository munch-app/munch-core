<template>
  <div class="index-dialog">
    <portal-target class="Notification index-elevation" name="notification"/>
  </div>
</template>

<script>
  export default {
    name: "NotificationPortal",

  }
</script>

<style scoped lang="less">
  .Notification {
    position: fixed;

    left: 0;
    right: 0;
    bottom: 0;
  }
</style>
