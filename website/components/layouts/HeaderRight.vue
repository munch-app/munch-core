<template>
  <no-ssr>
    <div class="HeaderRight">
      <div @click="onClickLogin" class="Name hover-pointer" v-if="!isLoggedIn">Login</div>
      <nuxt-link to="/profile" v-else class="Name">{{displayName}}</nuxt-link>
      <img v-if="false" class="Menu hover-pointer" src="/img/layouts/menu.svg" @click="onClickMenu"/>
    </div>
  </no-ssr>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderRight",
    computed: {
      ...mapGetters('user', ['isLoggedIn', 'displayName']),
    },
    methods: {
      onClickMenu() {
        this.$store.commit('toggleFocus', 'HeaderMenu')
      },
      onClickLogin() {
        this.$store.commit('focus', 'Login')
      }
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
