<template>
  <div class="container pt-48 pb-128">
    <div>
      <h1>Affiliate Data Management</h1>
    </div>

    <div>
      <div v-for="affiliate in affiliates" :key="affiliate.uid">
        <div class="overflow-hidden">
          <pre>{{affiliate}}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    layout: 'system',
    asyncData({$api}) {
      return $api.get('/admin/affiliates', {params: {status: 'PENDING'}})
        .then(({data: affiliates, cursor}) => {
          return {affiliates, cursor, status: 'PENDING'}
        })
    }
  }
</script>

<style scoped lang="less">

</style>
