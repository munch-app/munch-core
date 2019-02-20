<template>
  <div class="flex-no-shrink hover-pointer" v-if="isLoggedIn" @click="$store.commit('toggleFocus', 'HeaderMenu')">
    <div class="ProfileImage">
      <img class="wh-100" v-if="!failed && profileImageUrl" :src="profileImageUrl + 'a'" @error="failed = true">
      <div v-else class="bg-s500 wh-100 flex-center">
        <h3 class="m-0 lh-1 white text-capitalize">{{displayName && displayName[0]}}</h3>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderProfile",
    computed: {
      ...mapGetters('user', ['profileImageUrl', 'profile', 'isLoggedIn', 'displayName']),
    },
    data() {
      return {
        failed: false
      }
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

    @media (max-width: 767.98px) {
      display: none;
    }
  }
</style>
