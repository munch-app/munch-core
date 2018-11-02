<template>
  <div :id="anchor">
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
</style>
