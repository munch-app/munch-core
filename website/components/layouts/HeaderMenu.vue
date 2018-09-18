<template>
  <div class="HeaderMenu IndexTopElevation no-select" v-if="$store.state.layout.menu">
    <ul class="NavLink Elevation3 Text IndexTopElevation">
      <div class="MobileOnly">
        <nuxt-link to="/">Home</nuxt-link>
        <nuxt-link to="/profile" v-if="isLoggedIn">Profile</nuxt-link>
        <nuxt-link to="/login" v-else>Login</nuxt-link>
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
      <nuxt-link to="/logout" v-if="isLoggedIn">Logout</nuxt-link>
    </ul>
    <div v-on-clickaway="onClickAway"></div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    name: "HeaderMenu",
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
    },
    methods: {
      onClickAway() {
        if (this.$store.state.layout.menu) {
          this.$store.commit('layout/showMenu', false)
        }
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
