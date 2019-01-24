<template>
  <div class="HeaderMenu flex-justify-end container index-top-elevation no-select" v-if="isFocused('HeaderMenu')">
    <ul class="NavLink fixed bg-white w-100 elevation-3 text index-top-elevation">
      <div class="Mobile">
        <nuxt-link to="/">Home</nuxt-link>
        <nuxt-link to="/profile" v-if="isLoggedIn">Profile</nuxt-link>
        <a v-else @click="$store.commit('focus', 'Login')">Login</a>
        <hr class="mtb-8">
        <a href="https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754" target="_blank">Get Munch iOS App</a>
        <a href="https://play.google.com/store/apps/details?id=app.munch.munchapp" target="_blank">Get Munch Android App</a>
        <a href="https://partner.munch.app" target="_blank">Join as Partner</a>
      </div>
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
      }
    }
  }
</script>

<style scoped lang="less">
  a {
    font-size: 15px;
    font-weight: 600;
    display: block;
    color: rgba(0, 0, 0, 0.75);
    padding: 8px 24px;

    &:hover {
      cursor: pointer;
    }
  }

  .NavLink {
    padding: 8px 0;
  }

  @media (min-width: 768px) {
    .NavLink {
      width: 200px;
      border-radius: 4px;
      margin-top: -8px;
    }

    .Mobile {
      display: none;
    }
  }

  @media (max-width: 767.98px) {
    .HeaderMenu {
      padding-right: 0;
      padding-left: 0;
    }

    .NonMobile {
      display: none;
    }
  }
</style>
