<template>
  <no-ssr>
    <div class="flex-align-center">
      <a class="ml-16 mr-24 text-ellipsis-1l" href="https://partner.munch.app" target="_blank">Join as Partner</a>

      <a @click.prevent.stop="onClickLogin" class="flex-no-shrink" v-if="!isLoggedIn">Login</a>
      <nuxt-link to="/profile" v-else>{{displayName}}</nuxt-link>

      <img v-if="false" class="ml-16 hover-pointer" src="~/assets/icon/menu.svg" @click="onClickMenu"/>
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
  a {
    height: 32px;
    line-height: 32px;
    overflow: hidden;

    font-size: 15px;
    font-weight: 600;

    color: rgba(0, 0, 0, 0.80);

    &:hover {
      cursor: pointer;
    }
  }
</style>
