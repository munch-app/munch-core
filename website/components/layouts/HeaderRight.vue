<template>
  <no-ssr>
    <div class="HeaderRight">
      <div @click="$store.commit('focus', 'Login')" class="Name hover-pointer" v-if="!isLoggedIn">Login</div>
      <nuxt-link to="/profile" v-else class="Name">{{displayName}}</nuxt-link>
      <img v-if="false" class="Menu hover-pointer" src="/img/layouts/menu.svg" @click="$emit('clickMenu')"/>
    </div>
  </no-ssr>
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
    }

    img {
      margin-left: 16px;
    }
  }
</style>
