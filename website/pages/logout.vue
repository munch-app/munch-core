<template>
  <div class="index-9 position-0 fixed bg-white"/>
</template>

<script>
  export default {
    mounted() {
      if (!process.client) return

      this.$store.dispatch('user/logout')
      this.$router.push({path: '/'})
    }
  }
</script>
