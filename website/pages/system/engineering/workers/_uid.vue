<template>
  <div class="container pt-48 pb-128">
    <div>
      <h1>Worker: <span>{{group.name}}</span></h1>
      <p>{{group.description}}</p>
    </div>

    <div>
      <div class="ptb-16" v-for="report in group.reports" :key="report.uid">
        <div class="bg-steam p-24 border-4">
          <div>
            <p class="m-0">{{report.uid}}</p>
            <h5>Started: {{formatMillis(report.startedAt)}}</h5>
            <h5>{{report.status}}: {{formatMillis(report.completedAt || report.startedAt)}}</h5>
          </div>
          <div v-if="report.details && report.details.exception" class="bg-white border-4 p-12 mt-16 overflow-hidden">
            <h5>Exception: {{report.details.exception.type}}</h5>
            <pre>{{report.details.exception.message}}</pre>
            <pre>{{report.details.exception.stacktrace}}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'

  export default {
    layout: 'system',
    asyncData({$api, params: {uid}}) {
      return $api.get(`/admin/workers/groups/${uid}`)
        .then(({data: group}) => {
          return {group}
        })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">

</style>
