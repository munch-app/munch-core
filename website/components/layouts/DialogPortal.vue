<!--
3 Dialog portal are supported for now
dialog:
Where you have to style everything yourself.

dialog-styled:
Where there is build in default styling for certain elements.

dialog-w400:
Max-width of 400px if > 576vw
-->
<template>
  <div class="DialogPortal index-dialog flex-center zero-spacing">
    <portal-target class="Dialog elevation-3 index-elevation" name="dialog"/>
    <portal-target class="Dialog W400 elevation-3 index-elevation" name="dialog-w400"/>
    <portal-target class="Dialog Styled elevation-3 index-elevation" name="dialog-styled"/>

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
    min-width: 340px;
    max-width: 576px;

    @media (max-width: 575.98px) {
      min-width: 300px;
      margin: 0;
      border-radius: 0;
      left: 0;
      right: 0;
    }
  }

  .Dialog.Styled {
    display: flex;
    flex-direction: column;

    > div {
      margin-left: -12px;
      margin-right: -12px;
      margin-bottom: 8px;

      > * {
        margin-left: 12px;
        margin-right: 12px;
      }
    }

    h1, h2, h3, h4, h5, h6, p {
      margin-bottom: 8px;
    }

    button {
      margin-top: 16px;
      line-height: 1.5;
      align-self: flex-end;

      &:hover {
        cursor: pointer;
      }
    }

    input {
      font-size: 17px;
      border: 0;
      border-bottom: 1px solid #478DA6; // secondary-300
      padding: 8px 0;
      margin-bottom: 8px;
    }

    .left {
      align-self: flex-start;
    }

    .right {
      align-self: flex-end;
    }
  }

  .Dialog.W400 {
    max-width: 400px;

    @media (max-width: 575.98px) {
      max-width: 576px;
    }
  }
</style>
