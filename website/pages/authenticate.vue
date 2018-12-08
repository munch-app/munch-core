<template>
  <div class="index-9 position-0 fixed bg-white"/>
</template>

<script>
  export default {
    head() {
      return {title: 'Munch'}
    },
    mounted() {
      if (!process.client) return

      function onError(error) {
        console.log(error)
        this.$store.dispatch('user/logout')
        this.$router.push({path: '/'})
      }

      const authenticator = require('~/services/authenticator').default
      const redirect = this.$route.query.redirect && decodeURIComponent(this.$route.query.redirect) || '/'

      return authenticator.getIdToken()
        .then(() => {
          this.$router.push({path: redirect})
        })
        .catch(err => {
          // If token is provided try login with the provided token
          const token = this.$route.query.token
          if (token) {
            return this.$store.dispatch('user/signInCustomToken', token)
              .then(() => {
                this.$router.push({path: redirect})
              })
              .catch(err => onError(err))
          } else {
            onError(err)
          }
        })
    }
  }
</script>
