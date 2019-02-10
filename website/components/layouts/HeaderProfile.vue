<template>
  <div class="flex-no-shrink hover-pointer" v-if="isLoggedIn" @click="$store.commit('toggleFocus', 'HeaderMenu')">
    <div class="ProfileImage">
      <img class="wh-100" v-if="profileImageUrl" :src="profileImageUrl">
      <div v-else class="bg-whisper100 wh-100"></div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderProfile",
    computed: {
      ...mapGetters('user', ['profileImageUrl', 'profile', 'isLoggedIn']),
    }
  }
</script>

<style scoped lang="less">
  .ProfileImage {
    height: 40px;
    width: 40px;
    margin-top: 4px;

    > * {
      border-radius: 50%;
    }
  }
</style>
