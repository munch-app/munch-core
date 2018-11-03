<template>
  <div :id="anchor" :class="{'Sticky index-elevation': card.sticky}">
    <h2>{{title}}</h2>
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
  }

  .Sticky {
    position: sticky;
    top: calc(48px + 56px);

    background-color: white;
    z-index: 1;

    padding-bottom: 18px !important;
    margin-bottom: -18px !important;
  }
</style>
