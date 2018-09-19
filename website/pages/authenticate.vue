<template>
  <div class="ZeroSpacing Container Authenticate">
    <h3>Authenticating...</h3>
  </div>
</template>

<script>
  export default {
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
    margin-top: 24px;
  }
</style>
