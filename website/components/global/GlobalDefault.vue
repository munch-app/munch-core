<!--
 Global Default are components shared and used by all layouts globally.
-->
<template>
  <div>
    <global-dialog/>

    <global-notification/>

    <!-- Dialog Portal, TODO(fuxing): Refactoring required -->
    <dialog-portal/>
  </div>
</template>

<script>

  import DialogPortal from "../layouts/DialogPortal";
  import GlobalDialog from "./GlobalDialog";
  import GlobalNotification from "./GlobalNotification";

  export default {
    name: "GlobalDefault",
    components: {GlobalNotification, GlobalDialog, DialogPortal}
  }
</script>
