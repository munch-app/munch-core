<template>
  <div @click="onClick" class="bg-saltpan100 p-24 border-3 white zero">
    <div v-if="this.type === 'activation:login'">
      <h3>Too much food, too little time?</h3>
      <h5 class="mt-16">Create an account and begin saving places in your personal collections!</h5>

      <div class="flex-end mt-24">
        <button class="primary">Create Account</button>
      </div>
    </div>
    <div v-if="this.type === 'referral:share'">
      <h3>Don't tackle all this food alone!</h3>
      <h5 class="mt-16">Share the Feed with your friends and start chowing down together!</h5>

      <div class="flex-end mt-24">
        <button class="primary">Share</button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "FeedImageInjectedCard",
    props: {
      type: {
        type: String,
        required: true
      }
    },
    methods: {
      onClick() {
        switch (this.type) {
          case 'referral:share':
            this.onShare()
            return

          case 'activation:login':
            this.onLogin()
            return
        }
      },
      onShare() {
        const url = window.location.href + `?g=GB9`

        this.$copyText(url).then(() => {
          this.$store.dispatch('addMessage', {title: 'Copied URL!'})
          this.$track.share('Feed Image', 'GB9: Referral Share')
        }, (e) => {
          this.$store.dispatch('addError', e)
        })
      },
      onLogin() {
        this.$track.login('Feed Image', 'GB8: Activation Login')
        this.$store.commit('focus', 'Login')
      }
    }
  }
</script>

<style scoped lang="less">
</style>
