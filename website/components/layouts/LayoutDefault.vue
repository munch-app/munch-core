<template>
  <div>
    <!-- Notification Portal -->
    <notification-portal/>

    <!-- Dialog Portal -->
    <dialog-portal/>

    <!-- Notification List on the right -->
    <notification-list/>

    <!-- Notification Event on the bottom -->
    <notification-event/>
  </div>
</template>

<script>
  import NotificationPortal from "./NotificationPortal";
  import NotificationList from "./NotificationList";
  import NotificationEvent from "./NotificationEvent";
  import DialogPortal from "./DialogPortal";

  export default {
    name: "LayoutDefault",
    components: {DialogPortal, NotificationEvent, NotificationList, NotificationPortal}
  }
</script>
