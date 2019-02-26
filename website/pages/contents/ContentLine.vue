<template>
  <div class="flex-center">
    <div class="flex">
      <div v-for="i in [1,2,3,4]" :key="i"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ContentLine"
  }
</script>

<style scoped lang="less">
  .flex {
    div {
      margin: 16px;

      height: 6px;
      width: 6px;
      border-radius: 50%;
      background-color: rgba(0, 0, 0, 0.5);
    }
  }
</style>
