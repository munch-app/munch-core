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
  <div class="DialogPortal index-dialog flex-center zero">
    <portal-target class="Dialog Full index-elevation" name="dialog-full"/>
    <portal-target class="Dialog Blank elevation-3 index-elevation" name="dialog-blank"/>
    <portal-target class="Dialog W400 elevation-3 index-elevation" name="dialog-w400"/>
    <portal-target class="Dialog W768 elevation-3 index-elevation" name="dialog-w768"/>
    <portal-target class="Dialog Styled elevation-3 index-elevation" name="dialog-styled"/>
    <portal-target class="Dialog ActionSheet elevation-3 index-elevation" name="dialog-action-sheet"/>
    <portal-target class="Dialog elevation-3 index-elevation" name="dialog"/>

    <!-- Universal Dialog -->
    <profile-on-boarding v-if="isFocused('Login')"/>

    <div class="DialogOverlay elevation-overlay index-content-overlay"
         v-observe-visibility="{callback:visibilityChanged,throttle: 1}"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import ProfileOnBoarding from "../profile/ProfileOnBoarding";
  import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

  export default {
    name: "DialogPortal",
    components: {ProfileOnBoarding},
    computed: {
      ...mapGetters(['isFocused']),
    },
    methods: {
      visibilityChanged(isVisible) {
        // Lock scrolling if dialog is visible
        if (isVisible) {
          const scroll = document.querySelector('#dialog-portal-scroll')
          disableBodyScroll(scroll)
        } else {
          clearAllBodyScrollLocks()
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .DialogPortal {
    position: fixed;
    top: 0;
    bottom: 0;
  }

  .DialogOverlay {
    display: none;
  }

  // Disable if no content
  .Dialog:empty {
    display: none !important;
  }

  // Dialog overlay will only be shown if there is a preceding not empty dialog
  .Dialog:not(:empty) ~ .DialogOverlay {
    display: initial !important;
  }

  .Dialog {
    border-radius: 3px;
    background: white;
    padding: 24px;

    border: 1px solid rgba(0, 0, 0, .1);
  }

  // Responsive sizing
  .Dialog {
    min-width: 340px;
    max-width: 576px;

    position: absolute;
    margin-left: 50vw;

    @media (max-width: 575.98px) {
      position: fixed;
      min-width: 300px;
      margin: 0;
      border-radius: 0;
      left: 0;
      right: 0;
    }
  }

  .W400 {
    max-width: 400px;

    @media (max-width: 575.98px) {
      max-width: 576px;
    }
  }

  .W768 {
    min-width: 768px;
    max-height: 80vh;
    overflow-y: auto;
  }

  .Blank,
  .Full,
  .ActionSheet {
    padding: 0;
  }

  .Blank, .Full {
    border: 0;
    background: initial;
  }

  .Full {
    max-width: 100vw;
    width: 100vw;
    max-height: 100vh;
    height: 100vh;
  }

  .Styled,
  .ActionSheet {
    display: flex;
    flex-direction: column;

    border: 1px solid rgba(0, 0, 0, 0.1);

    @media (max-width: 300px) {
      border-style: solid none solid none;
    }

    @media (min-width: 300px) and (max-width: 575.98px) {
      min-width: 200px;
      border-radius: 3px;
      left: 24px;
      right: 24px;
    }
  }

  .ActionSheet {
    max-width: 320px;

    @media (min-width: 300px) and (max-width: 575.98px) {
      margin-left: auto;
      margin-right: auto;
    }

    @media (min-width: 576px) {
      min-width: 280px;
    }
  }
</style>

<style lang="less">
  .Dialog.Styled {
    > div {
      margin-left: -8px;
      margin-right: -8px;
      margin-bottom: 8px;

      > * {
        margin-left: 8px;
        margin-right: 8px;
      }
    }

    > div:last-child {
      margin-bottom: 0;
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

  .Dialog.ActionSheet {
    padding-top: 10px;

    hr {
      margin-top: 10px;
    }

    > div {
      padding-top: 12px;
      padding-bottom: 12px;

      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
      text-align: center;
      color: rgba(0, 0, 0, 0.75);

      &.close {
        font-weight: 400;
      }

      &:hover {
        cursor: pointer;
      }
    }
  }
</style>
