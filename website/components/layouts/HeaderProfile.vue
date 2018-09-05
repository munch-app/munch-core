<template>
  <div class="HeaderProfile">
    <div @click="$emit('click')">
      <img class="ProfileImage">
    </div>
  </div>
</template>

<script>
  export default {
    name: "HeaderProfile"
  }
</script>

<style scoped lang="less">
  .HeaderProfile {
    margin-top: 16px;
    margin-bottom: 16px;


    .ProfileImage {
      height: 32px;
      width: 32px;
      border: 0;
      background-color: black;
      border-radius: 16px;
    }
  }
</style>
