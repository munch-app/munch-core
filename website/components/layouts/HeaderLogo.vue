<template>
  <div class="HeaderLogo" @click="onClickLogo">
    <img class="Image" src="/img/MunchLogo.svg">
    <img class="HeaderIndicator" src="/img/layouts/caret-down.svg">
  </div>
</template>

<script>
  export default {
    name: "HeaderLogo",
    methods: {
      onClickLogo() {
        if (window.innerWidth < 768) {
          this.$store.commit('toggleFocus', 'HeaderMenu')
        } else {
          this.$router.push({path: '/'})
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .HeaderLogo {
    margin-top: 12px;
    margin-bottom: 12px;
    height: 32px;

    display: flex;

    &:hover {
      cursor: pointer;
    }
  }

  .Image {
    float: left;
    height: 32px;
    width: 32px;
  }

  .HeaderIndicator {
    display: block;
    height: 32px;
    width: 16px;
    padding: 10px 0 10px 4px;

    @media (min-width: 768px) {
      display: none;
    }
  }
</style>
