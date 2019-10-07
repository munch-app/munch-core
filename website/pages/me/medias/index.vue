<template>
  <div>

  </div>
</template>

<script>
  export default {
    asyncData({$api}) {
      $api.get('/me/medias')

      return {

      }
    }
  }
</script>

<style scoped lang="less">

</style>
