<template>
  <!-- Cannot use index-7 because it covers the Header Suggest DropDown-->
  <!-- Cannot use index-5 because it doesn't cover the Header Search Filter-->
  <div :id="anchor" :class="{'Sticky index-6': card.sticky}">
    <h2>{{title}}</h2>
    <hr>
  </div>
</template>

<script>
  export default {
    name: "SearchCardHeader",
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      title() {
        return this.card.title || 'Discover'
      },
      anchor() {
        return this.card._uniqueId
      }
    }
  }
</script>

<style scoped lang="less">
  h2 {
    margin-top: 8px;
    margin-bottom: 8px;
  }

  hr {
    display: none;
  }

  .Sticky {
    background-color: white;

    position: sticky !important;
    top: calc(48px + 56px);

    /*padding-bottom: 18px !important;*/
    /*margin-bottom: -18px !important;*/

    @media (max-width: 576px) {
      top: calc(56px);
      flex: 0 0 calc(100% + 24px) !important;
      max-width: calc(100% + 24px) !important;

      margin-left: -12px;
      margin-right: -12px;
      padding-left: 24px !important;
      padding-right: 24px !important;

      hr {
        display: block;
        margin-left: -24px;
        margin-right: -24px;
      }

      h2 {
        margin-top: -8px;
        margin-bottom: 8px;
      }
    }
  }
</style>
