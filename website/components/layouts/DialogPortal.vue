<!--
2 Dialog portal are supported for now
dialog:
where you have to style everything yourself

dialog-confirmation:
where h1,h2,h3,h4,p,button default styling is enabled
-->
<template>
  <div class="DialogPortal index-dialog flex-center zero-spacing">
    <portal-target class="Dialog elevation-3 index-elevation" name="dialog"/>
    <portal-target class="Dialog Confirmation elevation-3 index-elevation" name="dialog-confirmation"/>
    <div class="DialogOverlay elevation-overlay index-content-overlay"/>
  </div>
</template>

<script>
  export default {
    name: "DialogDelegator",
    data() {
      return {
        isOverlay: false
      }
    },
    methods: {
      visibilityChanged(isVisible) {
        this.isOverlay = isVisible
      },
    }
  }
</script>

<style lang="less">
  // Disable if no content
  .Dialog:empty {
    display: none !important;
  }

  .DialogOverlay {
    display: none;
  }

  // Dialog overlay will only be shown if there is a preceding not empty dialog
  .Dialog:not(:empty) ~ .DialogOverlay {
    display: initial !important;
  }

  .DialogPortal {
    position: fixed;
    top: 0;
    bottom: 0;
  }

  .Dialog {
    position: fixed;
    margin-left: 50vw;

    background: white;
    padding: 24px;
  }

  // Responsive sizing
  .Dialog {
    border-radius: 3px;
    max-width: 576px;

    @media (max-width: 575.98px) {
      margin: 0;
      border-radius: 0;
      left: 0;
      right: 0;
    }
  }

  .Dialog.Confirmation {
    display: flex;
    flex-direction: column;

    h1, h2, h3, h4 {
      margin-bottom: 8px;
    }

    p {
      margin-top: 8px;
      margin-bottom: 8px;
    }

    button {
      margin-top: 16px;
      margin-left: 24px;
      line-height: 1.5;
      align-self: flex-end;
    }

    .left {
      align-self: flex-start;
    }

    .right {
      align-self: flex-end;
    }
  }
</style>
