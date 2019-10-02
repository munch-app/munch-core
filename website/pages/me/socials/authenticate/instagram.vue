<template>
  <div/>
</template>

<script>
  export default {
    mounted() {
      const code = this.$route.query.code
      const redirectUri = `${process.env.origin}/me/socials/authenticate/instagram`

      if (!code) {
        this.$store.dispatch('addMessage', {type: 'error', message: 'Code required but cannot be found.'})
      }

      this.$store.commit('global/setDialog', 'LoadingDialog')
      this.$api.post('/me/socials/instagram/authenticate', {code, redirectUri})
        .then(() => {
          this.$store.dispatch('addMessage', {title: 'Added Social Account'})
          this.$router.push({path: `/me/socials`})
        })
        .catch(err => {
          this.$store.dispatch('addError', err)
        })
        .finally(() => {
          this.$store.commit('global/clearDialog')
        })
    }
  }
</script>
