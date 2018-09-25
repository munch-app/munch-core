<template>
  <div class="Authenticate"/>
</template>

<script>
  export default {
    head() {
      return {title: 'Authenticating | Munch'}
    },
    mounted() {
      if (process.client) {
        const redirect = this.$route.query.redirect && decodeURIComponent(this.$route.query.redirect) || '/'
        const authenticator = require('~/services/authenticator').default
        authenticator.getIdToken()
          .then(() => {
            this.$router.push({path: redirect})
          })
          .catch(error => {
            console.log(error)
            this.$store.dispatch('user/logout')
            this.$router.push({path: '/'})
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .Authenticate {
    z-index: 999999999;
    background: white;
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
  }
</style>
