<template>
  <div>
    <h2>{{title}}</h2>
  </div>
</template>

<script>
  export default {
    name: "SearchCardHeader",
    props: {
      title: {
        type: String,
        default: () => 'Discover'
      }
    }
  }
</script>

<style scoped lang="less">
</style>
