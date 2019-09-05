<!--
 Global Default are components shared and used by all layouts globally.
-->
<template>
  <div>
    <global-dialog/>

    <global-notification/>

    <!-- Dialog Portal, TODO(fuxing): Refactoring required -->
    <dialog-portal/>

    <!-- Notification Event on the bottom TODO(fuxing): remove when eat between is refactored -->
    <notification-event/>
  </div>
</template>

<script>

  import NotificationEvent from "../layouts/NotificationEvent";
  import DialogPortal from "../layouts/DialogPortal";
  import GlobalDialog from "./GlobalDialog";
  import GlobalNotification from "./GlobalNotification";

  export default {
    name: "GlobalDefault",
    components: {GlobalNotification, GlobalDialog, DialogPortal, NotificationEvent}
  }
</script>
