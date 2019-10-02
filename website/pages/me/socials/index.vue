<template>
  <div class="container pt-64 pb-64">
    <div class="flex-align-center flex-justify-between">
      <h1>Your socials</h1>
      <button @click="onConnectNew" class="blue-outline">Connect new</button>
    </div>

    <div class="mt-32">
      <div v-if="socials.length > 0">
        <pre>{{socials}}</pre>
      </div>

      <div v-else>
        <div class="ptb-48 flex-center">
          <p>You don't have any connected social media account yet.</p>
        </div>
      </div>
    </div>

    <div class="flex-center ptb-32" v-if="next">
      <button class="blue-outline" @click="onLoadMore">Load more</button>
    </div>

    <portal-dialog>
      <div class="dialog-small border">
        <div class="flex">
          <div class="border-blue border-3 overflow-hidden">
            <div class="p-16-24 hover-bg-a10 hover-pointer" @click="onConnectInstagram">
              <div class="flex-align-center">
                <simple-svg class="wh-32px" fill="black" :filepath="require('~/assets/icon/icons8-instagram.svg')"/>
                <h3 class="ml-8">Instagram</h3>
              </div>
              <div class="mt-4">
                <small>Connecting your instagram with Munch.</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </portal-dialog>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  import dateformat from 'dateformat'
  import PortalDialog from "../../../components/dialog/PortalDialog";

  export default {
    components: {PortalDialog},
    head() {
      return {
        title: `My Socials · ${this.name}`
      }
    },
    computed: {
      ...mapGetters('account', ['name']),
    },
    asyncData({$api}) {
      return $api.get('/me/socials', {params: {status: 'PUBLISHED', size: 20}})
        .then(({data: socials, cursor}) => {
          return {
            socials,
            next: cursor?.next
          }
        })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onLoadMore() {
        this.$api.get('/me/socials', {params: {size: 20, cursor: this.next}})
          .then(({data: socials, cursor}) => {
            this.socials.push(...socials)
            this.next = cursor?.next
          })
      },
      onConnectNew() {
        this.$store.commit('global/setDialog', {
          name: 'PortalDialog'
        })
      },
      onConnectInstagram() {
        // TODO(fuxing): Connect Instagram
      }
    }
  }
</script>

<style scoped lang="less">

</style>
