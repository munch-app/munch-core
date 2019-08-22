<template>
  <div class="container pt-48 pb-128">
    <div class="flex-align-center flex-between">
      <h1>Publications</h1>
      <div>
        <nuxt-link :to="`/system/publications/new`">
          <button class="blue-outline">
            New publication
          </button>
        </nuxt-link>
      </div>
    </div>

    <div class="mt-48">
      <table class="w-100">
        <thead>
        <tr class="b-a60">
          <th>Summary</th>
          <th>Body</th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="publication in publications" :key="publication.id">
          <td>
            <nuxt-link :to="`/system/publications/${publication.id}`">
              <div class="flex-align-center">
                <div class="wh-64px border-3 overflow-hidden">
                  <cdn-img :image="publication.image" type="320x320"/>
                </div>
                <div class="ml-16">
                  <h5>{{publication.name}}</h5>
                  <p>{{publication.description}}</p>
                </div>
              </div>
            </nuxt-link>
          </td>
          <td>
            <small>{{publication.body}}</small>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="flex-center ptb-48" v-if="next">
      <button class="blue-outline" @click="onLoadMore">Load More</button>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../../components/utils/image/CdnImg";
  export default {
    components: {CdnImg},
    layout: 'system',
    computed: {
      next() {
        return this.cursor?.next
      },
    },
    asyncData({$api}) {
      return $api.get('/admin/publications')
        .then(({data: publications, cursor}) => {
          return {publications, cursor}
        })
    },
    methods: {
      onLoadMore() {
        return this.$api.get('/admin/publications', {params: {cursor: this.next}})
          .then(({data: publications, cursor}) => {
            this.profiles.push(...publications)
            this.cursor = cursor
          })
      },
    }
  }
</script>

<style scoped lang="less">

</style>
