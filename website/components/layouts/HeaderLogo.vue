<template>
  <div class="HeaderLogo" @click="$emit('click')">
    <img src="/img/MunchLogo.svg">
    <img class="HeaderIndicator" src="/img/layouts/caret-down.svg">
  </div>
</template>

<script>
  export default {
    name: "HeaderLogo",
  }
</script>

<style scoped lang="less">
  .HeaderLogo {
    margin-top: 16px;
    margin-bottom: 16px;
    height: 32px;
    width: 32px;

    display: flex;
  }

  .HeaderIndicator {
    display: block;
    margin-left: 4px;

    @media (min-width: 768px) {
      display: none;
    }
  }
</style>
