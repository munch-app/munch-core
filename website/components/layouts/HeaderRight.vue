<template>
  <div class="flex-align-center">
    <button class="primary small ml-16 mr-8" @click="onClickGetTheApp">GET THE APP</button>
    <a class="ml-16 flex-shrink mr-24 text-ellipsis-1l" href="https://partner.munch.app" target="_blank">Join as
      Partner</a>

    <a @click.prevent.stop="onClickLogin" class="flex-no-shrink" v-if="!isLoggedIn">Login</a>
    <nuxt-link class="flex-no-shrink" to="/profile" v-else>{{displayName}}</nuxt-link>

    <portal to="dialog-blank" v-if="isFocused('HeaderRightGetApp')">
      <div class="none tablet-b desktop-b">
        <div class="flex-row bg-white border-4 overflow-hidden" id="dialog-portal-scroll"
             v-on-clickaway="onCloseGetApp">
          <div class="flex-shrink bg-p100 ImageBanner">
            <img src="~/assets/img/account/download-feed-ios.jpg">
          </div>
          <div class="ContentRight flex-basis-content">
            <img style="height: 40px" src="~/assets/img/MunchLogoTitled.svg">
            <h1 class="s500 mt-24 mb-16">The sexiest thing you’ll see on the internet - ever.</h1>
            <h5 class="">Download Munch on the App Store.</h5>

            <div class="flex-row mt-24 hover-pointer">
              <img @click="onClickIOS" class="mr-16" src="~/assets/img/AppleAppStore.svg">
              <img @click="onClickAndroid" src="~/assets/img/GooglePlayStore.svg">
            </div>
          </div>
        </div>
      </div>

      <div class="MobileBanner mobile fixed">
        <div class="flex-column bg-whisper050 hr-top p-16-24">
          <div class="flex-row">
            <img style="height: 40px" src="~/assets/img/MunchLogo.svg">
            <div class="ml-24">
              <h3>Drooling already?</h3>
              <p class="mb-16">Wait till you see the app.</p>
            </div>
          </div>
          <div class="flex-end">
            <button class="small mr-16" @click="onCloseGetApp">NO THANKS</button>
            <button class="primary small" @click="onClickGetApp">GET THE APP</button>
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
      mobileOS() {
        const userAgent = navigator.userAgent || navigator.vendor || window.opera;

        // Windows Phone must come first because its UA also contains "Android"
        if (/windows phone/i.test(userAgent)) return "Windows Phone";
        if (/android/i.test(userAgent)) return "Android";

        // iOS detection from: http://stackoverflow.com/a/9039885/177710
        if (/iPad|iPhone|iPod/.test(userAgent) && !window.MSStream) {
          return "iOS";
        }

        return "unknown";
      },
      onClickMenu() {
        this.$store.commit('toggleFocus', 'HeaderMenu')
      },
      onClickLogin() {
        this.$store.commit('focus', 'Login')
      },
      onClickGetTheApp() {
        this.$store.commit('toggleFocus', 'HeaderRightGetApp')
        this.$track.download('GetAppDialog', 'GB14: Clicked GetAppDialog')

        Cookies.set('GetAppDialog', 'seen')
      },
      onCloseGetApp() {
        this.$store.commit('unfocus', 'HeaderRightGetApp')
      },
      onClickGetApp() {
        if (this.mobileOS() === 'Android') {
          this.onClickAndroid()
        } else {
          this.onClickIOS()
        }
      },
      onClickIOS() {
        this.$store.commit('unfocus', 'HeaderRightGetApp')
        this.$track.download('AppleAppStore', 'GB15: GetAppDialog')
        window.open('https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754?mt=8', '_blank');
      },
      onClickAndroid() {
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
      }, 21000)
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

  .MobileBanner {
    bottom: 0;
    left: 0;
    right: 0;
  }
</style>
