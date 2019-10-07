<template>
  <div class="container pt-64 pb-64">
    <div class="flex-align-center flex-justify-between">
      <h1>Your connected accounts</h1>
      <button @click="onConnectInstagram" class="pink-outline">Connect Instagram</button>
    </div>

    <div class="mt-32">
      <div class="" v-if="socials.length > 0">
        <div class="border-4 bg-steam overflow-hidden" v-for="social in socials" :key="social.uid">
          <div class="p-16-24-20 hover-bg-a10 hover-pointer">
            <div>
              <h4>{{social.name}}</h4>
            </div>
            <div class="mt-8 flex-align-center">
              <div v-if="social.type === 'INSTAGRAM'">
                <simple-svg class="wh-24px" :filepath="require('~/assets/icon/icons8-instagram.svg')"/>
              </div>
              <div class="ml-8 p-6 border-blue border-2 tiny-bold lh-1">
                {{social.status}}
              </div>
            </div>
          </div>
        </div>
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
      onConnectInstagram() {
        const clientId = '74fc31293dde4a45b593a5deedc3ebbe'
        const redirectUri = `${process.env.origin}/me/socials/authenticate/instagram`
        const url = `https://api.instagram.com/oauth/authorize/?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`
        window.open(url, '_self')
      }
    }
  }
</script>

<style scoped lang="less">

</style>
