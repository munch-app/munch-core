<template>
  <div class="zero-spacing container Authenticate flex-center">
    <moon-loader class="Loader" color="#084E69" size="56px"/>
  </div>
</template>

<script>
  import MoonLoader from "vue-spinner/src/MoonLoader";
  export default {
    components: {MoonLoader},
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
            authenticator.signOut()
            this.$router.push({path: '/'})
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .Authenticate {
    margin-top: 48px;
    height: 100%;
  }
</style>
