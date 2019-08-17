<template>
  <div class="HeaderMenu flex-justify-end index-top-elevation no-select" v-if="isFocused('HeaderMenu')">
    <ul class="NavLink fixed bg-white w-100 elevation-3 text index-top-elevation border">

      <div v-if="isLoggedIn" class="text-decoration-none">
        <nuxt-link :to="`/@${username}`">
          <div class="flex-row flex-align-center">
            <div class="flex-no-shrink wh-40px border-circle bg-blue">
            </div>

            <div class="ml-8 flex-shrink text-ellipsis-1l">
              <div class="black">{{profile.name}}</div>
              <div class="blue small">@{{profile.username}}</div>
            </div>
          </div>
        </nuxt-link>

        <hr class="mtb-8">

        <nuxt-link class="black" :to="`/me/articles/_`">New article</nuxt-link>
        <nuxt-link class="black" :to="`/me/articles`">My articles</nuxt-link>
        <hr class="mtb-8">
      </div>

      <div class="text-decoration-none black">
        <nuxt-link class="Mobile black" to="/">Home</nuxt-link>
        <nuxt-link class="black" :to="`/@${username}`" v-if="profile">Profile</nuxt-link>
        <nuxt-link class="black" to="/support" v-if="isLoggedIn">Help</nuxt-link>
        <nuxt-link class="black" to="/me/signout" v-if="isLoggedIn">Sign out</nuxt-link>
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
      ...mapGetters('account', ['isLoggedIn', 'name', 'profile', 'username']),
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
    font-weight: 500;
    display: block;
    padding: 8px 24px;
  }

  .NavLink {
    padding: 8px 0;
  }

  @media (min-width: 768px) {
    .NavLink {
      width: 240px;
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
