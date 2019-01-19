<template>
  <div class="flex-align-center">
    <button class="primary small mr-16" @click="onClickGetApp">GET THE APP</button>
    <a class="ml-16 flex-shrink mr-24 text-ellipsis-1l" href="https://partner.munch.app" target="_blank">Join as
      Partner</a>

    <a @click.prevent.stop="onClickLogin" class="flex-no-shrink" v-if="!isLoggedIn">Login</a>
    <nuxt-link class="flex-no-shrink" to="/profile" v-else>{{displayName}}</nuxt-link>

    <img v-if="false" class="ml-16 hover-pointer" src="~/assets/icon/menu.svg" @click="onClickMenu"/>

    <portal to="dialog-blank" v-if="isFocused('HeaderRightGetApp')">
      <div class="flex-row bg-white border-4 overflow-hidden" id="dialog-portal-scroll" v-on-clickaway="onCloseGetApp">
        <div class="flex-shrink bg-p100 ImageBanner">
          <img src="~/assets/img/account/download-feed-ios.jpg">
        </div>
        <div class="ContentRight flex-basis-content">
          <img style="height: 40px" src="~/assets/img/MunchLogoTitled.svg">
          <h1 class="s500 mt-24 mb-16">Eat on the go with Munch.</h1>
          <h5 class="">Download Munch on the App Store.</h5>

          <div class="flex-row mt-24 hover-pointer">
            <img @click="onClickGetAppApple" class="mr-16" src="~/assets/img/AppleAppStore.svg">
            <img @click="onClickGetAppGoogle" src="~/assets/img/GooglePlayStore.svg">
          </div>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import * as Cookies from 'js-cookie'

  export default {
    name: "HeaderRight",
    computed: {
      ...mapGetters(['isFocused']),
      ...mapGetters('user', ['isLoggedIn', 'displayName']),
    },
    methods: {
      onClickMenu() {
        this.$store.commit('toggleFocus', 'HeaderMenu')
      },
      onClickLogin() {
        this.$store.commit('focus', 'Login')
      },
      onClickGetApp() {
        this.$store.commit('toggleFocus', 'HeaderRightGetApp')
        this.$track.download('GetAppDialog', 'GB14: Clicked GetAppDialog')

        Cookies.set('GetAppDialog', 'seen')
      },
      onCloseGetApp() {
        this.$store.commit('unfocus', 'HeaderRightGetApp')
      },
      onClickGetAppApple() {
        this.$store.commit('unfocus', 'HeaderRightGetApp')
        this.$track.download('AppleAppStore', 'GB15: GetAppDialog')
        window.open('https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754?mt=8', '_blank');
      },
      onClickGetAppGoogle() {
        this.$store.commit('unfocus', 'HeaderRightGetApp')
        this.$track.download('GooglePlayStore', 'GB15: GetAppDialog')
        window.open('https://play.google.com/store/apps/details?id=app.munch.munchapp', '_blank');
      }
    },
    mounted() {
      if (Cookies.get('GetAppDialog') === 'seen') return

      setTimeout(() => {
        Cookies.set('GetAppDialog', 'seen');
        this.$store.commit('focus', 'HeaderRightGetApp')
        this.$track.view('GetAppDialog', 'GB13: Popup GetAppDialog')
      }, 30000)
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

  .ImageBanner img {
    max-width: 240px;
  }

  .ContentRight {
    padding: 32px;
  }
</style>
