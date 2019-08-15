<template>
  <div class="flex-no-shrink hover-pointer" v-if="isLoggedIn" @click="$store.commit('toggleFocus', 'HeaderMenu')">
    <div class="ProfileImage">
      <div class="bg-blue wh-100 flex-center">
        <span class="h5 text-center text-capitalize white">{{name && name[0]}}</span>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderProfile",
    computed: {
      ...mapGetters('account', ['isLoggedIn', 'name']),
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
    height: 36px;
    width: 36px;

    > * {
      border-radius: 50%;
    }

    @media (max-width: 767.98px) {
      display: none;
    }
  }
</style>
