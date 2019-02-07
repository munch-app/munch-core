<template>
  <div class="HeaderMenu flex-justify-end index-top-elevation no-select" v-if="isFocused('HeaderMenu')">
    <ul class="NavLink fixed bg-white w-100 elevation-3 text index-top-elevation border">
      <div class="Mobile">
        <nuxt-link to="/">Home</nuxt-link>
        <nuxt-link to="/profile" v-if="isLoggedIn">Profile</nuxt-link>
        <a v-else @click="$store.commit('focus', 'Login')">Login</a>
        <nuxt-link to="/logout" v-if="isLoggedIn">Sign out</nuxt-link>
        <hr class="mtb-8">
        <a href="https://partner.munch.app" target="_bla20nk">Join as Partner</a>
        <a href="https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754" target="_blank">Get Munch iOS App</a>
        <a href="https://play.google.com/store/apps/details?id=app.munch.munchapp" target="_blank">Get Munch Android App</a>
      </div>

      <div v-if="creatorName">
        <nuxt-link to="/creator/profiles"><span class="s700 weight-600">Creator: {{creatorName}}</span></nuxt-link>
        <nuxt-link to="/creator/stories" v-if="isLoggedIn">Stories</nuxt-link>
        <nuxt-link to="/creator/series" v-if="isLoggedIn">Series</nuxt-link>
        <hr class="mtb-8">
      </div>

      <div class="NonMobile">
        <nuxt-link to="/profile" v-if="isLoggedIn">Profile</nuxt-link>
        <nuxt-link to="/support" v-if="isLoggedIn">Help</nuxt-link>
        <nuxt-link to="/logout" v-if="isLoggedIn">Sign out</nuxt-link>
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
      ...mapGetters('creator', ['creatorName']),
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
