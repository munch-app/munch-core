<template>
  <div class="HeaderMenu flex-justify-end index-top-elevation no-select" v-if="isFocused('HeaderMenu')">
    <ul class="NavLink fixed bg-white w-100 elevation-3 text index-top-elevation border text-decoration-none">

      <div>
        <nuxt-link class="Mobile black" to="/">Home</nuxt-link>
        <a v-if="!isLoggedIn" @click.prevent.stop="onSignIn">Sign In</a>
        <a v-if="!isLoggedIn" @click.prevent.stop="onSignUp">Sign Up</a>
      </div>

      <div v-if="isLoggedIn">
        <nuxt-link :to="`/@${username}`">
          <div class="flex-row flex-align-center">
            <div v-if="profile.image" class="flex-no-shrink wh-40px border-circle overflow-hidden mr-8">
              <cdn-img :image="profile.image" type="320x320"/>
            </div>

            <div class="flex-shrink text-ellipsis-1l">
              <div class="black">{{profile.name}}</div>
              <div class="blue small">@{{profile.username}}</div>
            </div>
          </div>
        </nuxt-link>

        <hr class="mtb-8">

        <nuxt-link class="black" :to="`/me/articles`">Articles</nuxt-link>
        <nuxt-link class="black" :to="`/me/articles/new`">New article</nuxt-link>
        <nuxt-link class="black" :to="`/me/places/new`">New place</nuxt-link>
        <hr class="mtb-8">
      </div>

      <div>
        <nuxt-link class="black" :to="`/me/socials`">Social Accounts</nuxt-link>
        <nuxt-link class="black" :to="`/me/mentions`">Mentions</nuxt-link>
        <hr class="mtb-8">
      </div>

      <div v-if="isLoggedIn">
        <nuxt-link class="black" :to="`/@${username}`" >Profile</nuxt-link>
        <nuxt-link class="black" to="/support">Help</nuxt-link>
        <nuxt-link class="black" to="/me/signout">Sign out</nuxt-link>
      </div>

      <div v-if="isMunchTeam">
        <hr class="mtb-8">
        <nuxt-link class="black" to="/system">Admin System</nuxt-link>
      </div>

    </ul>
    <div v-on-clickaway="onClickAway"></div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "HeaderMenu",
    components: {CdnImg},
    computed: {
      ...mapGetters(['isFocused', 'isMunchTeam']),
      ...mapGetters('account', ['isLoggedIn', 'name', 'profile', 'username']),
    },
    methods: {
      onClickAway() {
        if (this.isFocused('HeaderMenu')) {
          this.$store.commit('unfocus', 'HeaderMenu')
        }
      },
      onSignIn() {
        this.$store.commit('global/setDialog', {name: 'GetStartedDialog', props: {defaultState: 'sign-in'}})
      },
      onSignUp() {
        this.$store.commit('global/setDialog', {name: 'GetStartedDialog', props: {defaultState: 'sign-up'}})
      },
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
