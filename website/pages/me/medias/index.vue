<template>
  <div class="container pt-48 pb-64">
    <h1>Your media</h1>

    <div class="mt-24">
      <div class="m--12 flex">
        <div class="p-12">
          <select-button label="Account" :selected="selected.social"
                         :selections="selections.socials" @select="onAccount"/>
        </div>
        <div class="p-12">
          <select-button label="Status" :selected="selected.status" :selections="selections.status" @select="onStatus"/>
        </div>
      </div>
    </div>

    <div class="mt-32" v-if="medias">
      <div class="m--12 flex-1-2-3-4-5">
        <div class="p-12" v-for="media in medias" :key="media.id">
          <div class="aspect r-1-1 bg-steam border-4 overflow-hidden hover-pointer">
            <div class="border-3 overflow-hidden">
              <cdn-img :image="media.images[0]">

              </cdn-img>
            </div>
          </div>
        </div>
      </div>

      <div v-if="medias.length === 0" class="flex-center p-24">
        <p>You don't have any <b v-if="!selected.status.default" class="text-lowercase">{{selected.status.name}}</b> media.</p>
      </div>
    </div>

    <div v-else class="flex-center p-24">
      <beat-loader color="#07F" size="16px"/>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../../components/utils/image/CdnImg";
  import SelectButton from "../../../components/utils/SelectButton";

  export default {
    components: {SelectButton, CdnImg},
    asyncData({$api}) {
      return $api.get('/me/socials')
        .then(({data: socials}) => {
          return {
            selections: {
              socials: [
                {name: 'Any', default: true},
                ...socials
              ],
              status: [
                {name: 'Any', default: true},
                {name: 'Pending', type: 'PENDING'},
                {name: 'Public', type: 'PUBLIC'},
                {name: 'Hidden', type: 'HIDDEN'}
              ]
            }
          }
        })
    },
    data() {
      return {
        selected: {
          status: {name: 'Any', default: true},
          social: {name: 'Any', default: true},
        },
        params: {},
        cursor: {},
        medias: null
      }
    },
    mounted() {
      const query = this.$route.query
      if (query.status) {
        this.selected.status = this.selections.status.find(s => s.type === query.status)
      }

      if (query['social.uid']) {
        this.selected.social = this.selections.socials.find(s => s.uid === query['social.uid'])
      }

      this.updateParams()
      this.reload()
    },
    methods: {
      onAccount(selection) {
        this.selected.social = selection
        this.updateParams()
        this.reload()
      },
      onStatus(selection) {
        this.selected.status = selection
        this.updateParams()
        this.reload()
      },
      updateParams() {
        if (this.selected.social?.uid) {
          this.params['social.uid'] = this.selected.social.uid
        } else {
          delete this.params['social.uid']
        }
        if (this.selected.status?.type) {
          this.params.status = this.selected.status.type
        } else {
          delete this.params['status']
        }

        this.$path.replace({query: this.params})
      },
      reload() {
        this.$api.get('/me/medias', {params: {size: 30, ...this.params}})
          .then(({data: medias, cursor}) => {
            this.medias = medias
            this.cursor = cursor
          })
          .catch(err => {
            this.$store.dispatch('addError', err)
          })
      },
      onLoadMore() {
        // TODO(fuxing):
      },
    }
  }
</script>

<style scoped lang="less">

</style>
