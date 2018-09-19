<template>
  <div class="HeaderMenu IndexTopElevation no-select" v-if="isFocused('HeaderMenu')">
    <ul class="NavLink Elevation3 Text IndexTopElevation">
      <div class="MobileOnly">
        <nuxt-link to="/">Home</nuxt-link>
        <nuxt-link to="/profile" v-if="isLoggedIn">Profile</nuxt-link>
        <a v-else @click="$store.commit('focus', 'Login')">Login</a>
        <hr>
      </div>
      <div class="NonMobileOnly" v-if="isLoggedIn">
        <nuxt-link to="/profile">Profile</nuxt-link>
        <hr>
      </div>
      <nuxt-link to="/support">Support</nuxt-link>
      <nuxt-link to="/about">About Munch</nuxt-link>
      <hr>
      <div><a href="https://partner.munch.app" target="_blank">Content Partners</a></div>
      <hr v-if="isLoggedIn">
      <a @click="onLogout" v-if="isLoggedIn">Logout</a>
    </ul>
    <div v-on-clickaway="onClickAway"></div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    name: "HeaderMenu",
    computed: {
      ...mapGetters(['isFocused']),
      ...mapGetters('user', ['isLoggedIn']),
    },
    methods: {
      onClickAway() {
        if (this.isFocused('HeaderMenu')) {
          this.$store.commit('unfocus', 'HeaderMenu')
        }
      },
      onLogout() {
        this.$store.dispatch('user/logout')
        this.$router.push({path: '/'})
      }
    }
  }
</script>

<style scoped lang="less">
  .HeaderMenu {
    @media (min-width: 768px) {
      max-width: 720px;
      padding-right: 15px;
      padding-left: 15px;
      margin-right: auto;
      margin-left: auto;

      display: flex;
      justify-content: flex-end;
    }

    @media (min-width: 992px) {
      max-width: 960px;
    }

    @media (min-width: 1200px) {
      max-width: 1140px;
    }
  }

  .NavLink {
    position: fixed;
    background: white;
    padding: 8px 0;

    hr {
      margin: 8px 0;
    }

    a {
      font-size: 15px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.75);
      padding: 8px 15px;
      display: block;

      &:hover {
        cursor: pointer;
      }
    }

    width: 100%;

    @media (min-width: 768px) {
      width: 200px;
      border-radius: 4px;
      margin-top: -8px;

      a {
        padding: 8px 18px;
      }
    }
  }

  @media (min-width: 768px) {
    .MobileOnly {
      display: none;
    }
  }

  @media (max-width: 767.98px) {
    .NonMobileOnly {
      display: none;
    }
  }
</style>
