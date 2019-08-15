<template>
  <div class="flex-align-center" :class="{Clear: clear}">
    <div v-if="!isLoggedIn" class="flex-no-shrink">
      <button @click.prevent.stop="onClickLogin" class="small">
        Sign In
      </button>
      <button @click.prevent.stop="onClickLogin" class="small blue-outline">
        Get Started
      </button>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import * as Cookies from 'js-cookie'

  export default {
    name: "HeaderRight",
    computed: {
      ...mapGetters(['isFocused']),
      ...mapGetters('account', ['isLoggedIn', 'name']),
    },
    props: {
      clear: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        getApp: null
      }
    },
    methods: {
      onClickLogin() {
        this.$store.commit('focus', 'Login')
      },

      // MARK: GetApp Dialog & Notification
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
      onClickGetTheApp() {
        this.openGetApp()
        this.$track.download('GetAppDialog', 'GB14: Clicked GetAppDialog')
      },
      openGetApp() {
        // TODO: Change to localStorage
        Cookies.set('GetAppDialog', 'seen')

        if (window.innerWidth <= 768) {
          this.getApp = 'GetAppNotification'
        } else {
          this.getApp = 'GetAppDialog'
        }
      },
      closeGetApp() {
        this.getApp = null
      },
      onClickStore() {
        if (this.mobileOS() === 'Android') {
          this.onClickAndroid()
        } else {
          this.onClickIOS()
        }
      },
      onClickIOS() {
        this.closeGetApp()
        this.$track.download('AppleAppStore', 'GB15: GetAppDialog')
        window.open('https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754?mt=8', '_blank');
      },
      onClickAndroid() {
        this.closeGetApp()
        this.$track.download('GooglePlayStore', 'GB15: GetAppDialog')
        window.open('https://play.google.com/store/apps/details?id=app.munch.munchapp', '_blank');
      },
    },
    mounted() {
      if (this.$route.query.download === 'GB20') {
        return setTimeout(() => {
          this.openGetApp()
          this.$track.download('GetAppDialog', 'GB20: Forced GetAppDialog')
        }, 1000);
      }

      // Disabled, After Ign, Focusing on Web
      // if (Cookies.get('GetAppDialog') === 'seen') return
      // setTimeout(() => {
      //   this.openGetApp()
      //   this.$track.view('GetAppDialog', 'GB13: Popup GetAppDialog')
      // }, 45000)
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

    color: rgba(0, 0, 0, 0.8);

    &:hover {
      cursor: pointer;
    }
  }

  .Clear {
    a {
      color: white;
    }
  }

  .ImageBanner img {
    max-width: 240px;
  }

  .ContentRight {
    padding: 32px;
  }
</style>
