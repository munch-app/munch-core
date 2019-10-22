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
            <div class="border-3 overflow-hidden" @click="onMedia(media)">
              <cdn-img :image="media.images[0]">
                <div class="flex-end p-12 wh-100">
                  <div class="p-6-12 border-2 h6 lh-1" :class="{
                       'bg-white': media.status === 'PENDING',
                       'bg-success white': media.status === 'PUBLIC',
                       'bg-error white': media.status === 'HIDDEN',
                       }">
                    {{media.status}}
                  </div>
                </div>
              </cdn-img>
            </div>
          </div>
        </div>
      </div>

      <div v-if="medias.length === 0" class="flex-center p-24">
        <p>You don't have any <b v-if="!selected.status.default" class="text-lowercase">{{selected.status.name}}</b>
          media yet.</p>
      </div>
    </div>

    <div v-else class="flex-center ptb-48">
      <beat-loader color="#07F" size="16px"/>
    </div>

    <div v-if="cursor && cursor.next" class="flex-center ptb-48">
      <button class="blue-outline" @click="onLoadMore">Load More</button>
    </div>

    <portal-dialog>
      <div class="dialog-large border" v-if="focused">
        <div class="flex-1-2 m--12">
          <div class="p-12">
            <div class="aspect r-1-1 bg-steam border-4 overflow-hidden hover-pointer">
              <div class="border-3 overflow-hidden">
                <cdn-img :image="focused.images[0]" type="1080x1080">
                </cdn-img>
              </div>
            </div>
          </div>

          <div class="p-12 flex-column-justify-between">
            <div>
              <h3>
                Media privacy
              </h3>
              <div class="mt-16">
                <select-button label="Status" :selected="{name: focused.status}"
                               :selections="[{name: 'PUBLIC'}, {name: 'HIDDEN'}]"
                               @select="(selection) => focused.status = selection.name"
                />
              </div>
            </div>

            <div class="flex-end mt-24">
              <button class="pink-outline" @click="onUpdate(focused)">
                Update
              </button>
            </div>
          </div>
        </div>
      </div>
    </portal-dialog>
  </div>
</template>

<script>
  import CdnImg from "../../../components/utils/image/CdnImg";
  import SelectButton from "../../../components/utils/SelectButton";
  import PortalDialog from "../../../components/dialog/PortalDialog";

  export default {
    components: {PortalDialog, SelectButton, CdnImg},
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
        medias: null,
        focused: null
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
      onMedia(media) {
        this.focused = media
        this.$store.commit('global/setDialog', {
          name: 'PortalDialog'
        })
      },
      onUpdate(media) {
        this.$store.commit('global/setDialog', 'LoadingDialog')
        this.$api.patch(`/me/medias/${media.id}`, media)
          .then(() => {
            this.$store.dispatch('addMessage', {title: 'Updated Media'})
          })
          .catch(err => {
            this.$store.dispatch('addError', err)
          })
          .finally(() => {
            this.$store.commit('global/clearDialog')
          })
      },
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
        this.$api.get('/me/medias', {params: {size: 30, cursor: this.cursor.next}})
          .then(({data: medias, cursor}) => {
            this.medias.push(...medias)
            this.cursor = cursor
          })
          .catch(err => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">

</style>
