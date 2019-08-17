<template>
  <div>

  </div>
</template>

<script>
  export default {
    mounted() {
      if (!process.client) return

      const username = this.$store.state.account.profile?.username
      if (username) {
        return this.$router.push({path: `/@${username}`})
      }

      this.$store.dispatch('addMessage', {
        type: 'error',
        message: 'Profile not found.'
      })
      return this.$router.push({path: `/`})
    }
  }
</script>

<style scoped lang="less">

</style>
