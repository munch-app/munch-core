<template>
  <div>
    <!--  TODO: New Place Directory -->
  </div>
</template>

<script>
  export default {
    asyncData({$api, params: {id}}) {
      return $api.get(`/places/${id}`)
        .then(({data: place}) => {
          return {
            place
          }
        })
    }
  }
</script>

<style scoped lang="less">

</style>
