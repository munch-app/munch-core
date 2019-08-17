<template>
  <div class="container-1200">
    <h1>Article</h1>
  </div>
</template>

<script>


  export default {
    validate({params: {p1, p2}}) {
      // /@username
      if (/^@[a-z0-9]{3,32}$/.test(_.toLower(p1))) {

        // /@username/L13-ID
        if (/^(?:[0-9a-z-]{0,1000}-)?[0123456789abcdefghjkmnpqrstvwxyz]{13}$/.test(p2)) {
          return true
        }
      }
    },
    asyncData({$api, params: {p1}, query: {revision}}) {
      const [, id] =
        /^(?:[0-9a-z-]{0,1000}-)?([0123456789abcdefghjkmnpqrstvwxyz]{13})$/.exec(p1)

      if (revision) {
        return $api.get(`/articles/${id}/revisions/${revision}`)
          .then(({data: article}) => {
            return {article}
          })
      }

      return $api.get(`/articles/${id}`)
        .then(({data: article}) => {
          return {article}
        })
    }
  }
</script>

<style scoped lang="less">

</style>
