<template>
  <div class="ZeroSpacing Container Authenticate flex-center">
    <moon-loader class="Loader" color="#084E69" size="56px"/>
  </div>
</template>

<script>
  import BeatLoader from "vue-spinner/src/BeatLoader";
  import MoonLoader from "vue-spinner/src/MoonLoader";
  export default {
    components: {MoonLoader, BeatLoader},
    head() {
      return {title: 'Authenticating | Munch'}
    },
    mounted() {
      if (process.client) {
        const authenticator = require('~/services/authenticator').default
        authenticator.getIdToken()
          .then(() => {
            this.$router.push({path: '/'})
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
