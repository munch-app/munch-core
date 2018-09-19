<template>
  <div class="HeaderRight">
    <no-ssr>
      <div @click="$store.commit('focus', 'Login')" class="Name hover-pointer" v-if="!isLoggedIn">Login</div>
      <nuxt-link to="/profile" v-else class="Name">{{displayName}}</nuxt-link>
    </no-ssr>
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

    .Name {
      font-size: 15px;
      font-weight: 600;
      line-height: 32px;
      color: rgba(0, 0, 0, 0.80);

      margin-right: 16px;
    }
  }
</style>
