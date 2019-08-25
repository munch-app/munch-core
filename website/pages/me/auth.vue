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

      const authenticator = require('~/services/authenticator').default
      const redirect = this.$route.query.redirect && decodeURIComponent(this.$route.query.redirect) || '/'

      return authenticator.getIdToken()
        .then(() => {
          this.$router.replace({path: redirect})
        })
        .catch(err => {
          // If token is provided try login with the provided token
          const token = this.$route.query.token
          if (token) {
            return this.$store.dispatch('account/signInCustomToken', token)
              .then(() => {
                this.$router.replace({path: redirect})
              })
              .catch(err => this.onError(err))
          } else {
            this.onError(err)
          }
        })
    },
    methods: {
      onError(error) {
        console.log(error)
        this.$store.dispatch('account/signOut')
        this.$router.replace({path: '/'})
      }
    }
  }
</script>
