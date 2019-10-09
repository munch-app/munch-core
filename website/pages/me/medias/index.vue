<template>
  <div class="container pt-48 pb-64">
    <div>
      <h1>Your media</h1>
    </div>
    <div class="mt-24">
      <button class="blue-outline">Accounts</button>
      <button class="blue-outline">Status</button>
    </div>

    <div class="mt-32">
      <div class="m--12 flex-1-2-3-4-5">
        <div class="p-12" v-for="media in medias" :key="media.id">
          <div class="border border-3 overflow-hidden">
            <cdn-img :image="media.images[0]">

            </cdn-img>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../../components/utils/image/CdnImg";

  export default {
    components: {CdnImg},
    asyncData({$api}) {
      return $api.get('/me/medias')
        .then(({data: medias, cursor}) => {
          return {medias, cursor}
        })
    },
    computed: {

    },
    methods: {
      loadNext() {

      },
    }
  }
</script>

<style scoped lang="less">

</style>
