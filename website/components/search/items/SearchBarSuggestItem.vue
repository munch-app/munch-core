<template>
  <div class="text b-a75">
    Did you mean <span class="weight-600 b-a85">{{suggest}}</span>
  </div>
</template>

<script>
  export default {
    name: "SearchBarSuggestItem",
    props: {
      suggest: {
        type: String,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  div {
    padding: 8px 0;
  }
</style>
