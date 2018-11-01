<template>
  <no-ssr>
    <div class="HeaderRight" v-if="!isProduction">
      <a class="Partner text-ellipsis-1l" href="https://partner.munch.app" target="_blank">Join as Partner</a>

      <a @click.prevent.stop="onClickLogin" class="hover-pointer" v-if="!isLoggedIn">Login</a>
      <nuxt-link to="/profile" v-else>{{displayName}}</nuxt-link>

      <img v-if="false" class="Menu hover-pointer" src="/img/layouts/menu.svg" @click="onClickMenu"/>
    </div>
  </no-ssr>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "HeaderRight",
    computed: {
      ...mapGetters(['isProduction']),
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

    .Partner {
      margin-left: 16px;
      margin-right: 24px;
    }

    a {
      height: 32px;
      line-height: 32px;
      overflow: hidden;

      font-size: 15px;
      font-weight: 600;

      color: rgba(0, 0, 0, 0.80);
    }

    img {
      margin-left: 16px;
    }
  }
</style>
