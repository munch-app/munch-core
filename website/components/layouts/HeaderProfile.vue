<template>
  <div class="flex-no-shrink hover-pointer" v-if="isLoggedIn" @click="$store.commit('toggleFocus', 'HeaderMenu')">
    <div class="ProfileImage">
      <div class="bg-blue wh-100 flex-center border-circle">
        <div class="NameChar text-capitalize white">
          {{name && name[0]}}
        </div>
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
  .NameChar {
    font-size: 18px;
    font-weight: 600;
    line-height: 36px;
  }

  .ProfileImage {
    height: 36px;
    width: 36px;

    @media (max-width: 767.98px) {
      display: none;
    }
  }
</style>
