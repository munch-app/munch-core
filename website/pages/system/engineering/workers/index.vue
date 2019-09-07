<template>
  <div class="container pt-48 pb-128">
    <div>
      <h1>Worker Groups</h1>
    </div>

    <div class="ptb-12">
      <div class="ptb-12" v-for="group in groups" :key="group.uid">
        <nuxt-link :to="`/system/engineering/workers/${group.uid}`" class="block p-16-24 bg-steam border-3 text-decoration-none">
          <h4>{{group.name}}</h4>
          <p class="m-0">{{group.description}}</p>
          <div class="mt-8" v-if="group.reports && group.reports.length > 0">
            <div class="bg-white p-6-12 mt-16 border-3" v-for="report in group.reports" :key="report.uid">
              <small>{{report.status}}</small>
              <small class="ml-8">{{formatMillis(report.completedAt || report.startedAt)}}</small>
            </div>
          </div>
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'

  export default {
    layout: 'system',
    asyncData({$api}) {
      return $api.get('/admin/workers/groups')
        .then(({data: groups}) => {
          return {groups}
        })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">

</style>
