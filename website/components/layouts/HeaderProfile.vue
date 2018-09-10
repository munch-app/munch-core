<template>
  <div class="HeaderProfile" @click="$emit('click')">
    <div class="ProfileImage"/>
  </div>
</template>

<script>
  export default {
    name: "HeaderProfile"
  }
</script>

<style scoped lang="less">
  .HeaderProfile {
    margin-top: 12px;
    margin-bottom: 12px;

    &:hover {
      cursor: pointer;
    }

    .ProfileImage {
      height: 32px;
      width: 32px;
      background-color: black;
      border-radius: 50%;
    }
  }
</style>
