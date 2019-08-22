<template>
  <div class="container pt-48 pb-128">
    <div class="flex-align-center flex-between">
      <h1>Profiles</h1>
      <div>
        <nuxt-link :to="`/system/profiles/new`">
          <button class="blue-outline">
            New profile
          </button>
        </nuxt-link>
      </div>
    </div>

    <div class="mt-32">
      <table class="w-100">
        <thead>
        <tr class="b-a60">
          <th>Summary</th>
          <th>Bio</th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="profile in profiles" :key="profile.uid">
          <td>
            <nuxt-link :to="`/system/profiles/${profile.uid}`">
              <div class="flex-align-center">
                <div class="wh-64px border-3 overflow-hidden">
                  <cdn-img :image="profile.image" type="320x320"/>
                </div>
                <div class="ml-16">
                  <h5>{{profile.name}}</h5>
                  <h6>@{{profile.username}}</h6>
                </div>
              </div>
            </nuxt-link>
          </td>
          <td>
            <small>{{profile.bio}}</small>
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
      return $api.get('/admin/profiles')
        .then(({data: profiles, cursor}) => {
          return {profiles, cursor}
        })
    },
    methods: {
      onLoadMore() {
        return this.$api.get('/admin/profiles', {params: {cursor: this.next}})
          .then(({data: profiles, cursor}) => {
            this.profiles.push(...profiles)
            this.cursor = cursor
          })
      },
    }
  }
</script>

<style scoped lang="less">

</style>
