<template>
  <div class="HeaderRight">
    <nuxt-link to="/login" class="Login hover-pointer" v-if="!isLoggedIn">Login</nuxt-link>
    <div v-else>{{displayName}}</div>
    <img class="Menu hover-pointer" src="/img/layouts/menu.svg" @click="$emit('clickMenu')"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderRight",
    computed: {
      ...mapGetters('user', ['isLoggedIn', 'displayName']),
    }
  }
</script>

<style scoped lang="less">
  .HeaderRight {
    display: flex;
    align-items: center;

    .Login {
      font-size: 15px;
      font-weight: 600;
      line-height: 32px;
      margin-right: 16px;
      color: rgba(0, 0, 0, 0.82);
    }
  }
</style>
