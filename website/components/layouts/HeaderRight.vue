<template>
  <div class="flex-align-center">
    <div v-if="!isLoggedIn" class="flex-no-shrink">
      <button @click.prevent.stop="onSignIn" class="small">
        Sign In
      </button>
      <button @click.prevent.stop="onSignUp" class="small blue-outline">
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
      ...mapGetters('account', ['isLoggedIn', 'name']),
    },
    methods: {
      onSignIn() {
        this.$store.commit('global/setDialog', {name: 'GetStartedDialog', props: {defaultState: 'sign-in'}})
      },
      onSignUp() {
        this.$store.commit('global/setDialog', {name: 'GetStartedDialog', props: {defaultState: 'sign-up'}})
      },
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
      onApp() {
        if (this.mobileOS() === 'Android') {
          window.open('https://play.google.com/store/apps/details?id=app.munch.munchapp', '_blank');
        } else {
          window.open('https://itunes.apple.com/sg/app/munch-food-discovery/id1255436754?mt=8', '_blank');
        }
      },
    },
  }
</script>
