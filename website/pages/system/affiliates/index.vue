<template>
  <div class="container pt-48 pb-128">
    <div>
      <h1>Affiliate Data Management</h1>
      <div class="mt-16">
        <button @click="onStatus(s.type)" v-for="s in statusList" :key="s.type" class="small mr-16" :class="{
          'outline': s.type !== status,
          'blue': s.type === status,
        }">
          {{s.name}}
        </button>
      </div>
    </div>

    <div class="ptb-12">
      <nuxt-link :to="`/system/affiliates/${affiliate.uid}`" class="block ptb-12 text-decoration-none"
                 v-for="affiliate in affiliates" :key="affiliate.uid">
        <div class="overflow-hidden border-3 border p-16 hover-pointer">
          <h5>{{affiliate.type}}: {{affiliate.brand.name}}</h5>
          <pre class="small">{{affiliate.placeStruct}}</pre>
        </div>
      </nuxt-link>
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
    },
    data() {
      return {
        statusList: [
          {type: 'PENDING', name: 'PENDING'},
          {type: 'REAPPEAR', name: 'REAPPEAR'},
          {type: 'LINKED', name: 'LINKED'},
          {type: 'DROPPED', name: 'DROPPED'},
          {type: 'DELETED_SOURCE', name: 'DELETED SOURCE'},
          {type: 'DELETED_MUNCH', name: 'DELETED MUNCH'},
        ]
      }
    },
    methods: {
      onStatus(type) {
        this.status = type
        this.affiliates.splice(0)

        return this.$api.get('/admin/affiliates', {params: {status: this.status}})
          .then(({data: affiliates, cursor}) => {
            this.affiliates = affiliates
            this.cursor = cursor
          })
      }
    }
  }
</script>

<style scoped lang="less">

</style>
