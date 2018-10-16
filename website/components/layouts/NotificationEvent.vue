<template>
  <div class="NotificationList index-overlay">
    <div class="NotificationBox elevation-3 white-bg border">
      <div class="Header">
        <h4 class="primary-700">
          Copy Link?
        </h4>
        <simple-svg class="Icon" fill="black" filepath="/img/layouts/copy.svg"/>

      </div>
      <h6 class="text">
        Teamwork makes the dream work. You've done the tough part now send this link to your friends and ask somebody to
        pick.
      </h6>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NotificationEvent"
  }
</script>

<style scoped lang="less">
  .NotificationList {
    position: fixed;
    left: 0;
    bottom: 0;
    margin-left: 32px;
    margin-bottom: 32px;
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
    }
  }

  @media (max-width: 575.98px) {
    .NotificationList {
      right: 0;
      margin: 0;
    }

    .NotificationBox {
      width: 100vw;
      border-radius: 0;
    }
  }
</style>
