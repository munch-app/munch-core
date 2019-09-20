<template>
  <div class="Dialog dialog-small">
    <div class="flex-column">
      <div class="flex-center">
        <img class="LogoBanner" src="~/assets/img/LOGO_NAME.svg">
      </div>

      <div class="mt-16 flex-center">
        <h4>Something for everyone.</h4>
      </div>

      <div class="mt-24">
        <div class="flex-1-2 m--12">
          <div class="p-12 flex-self-stretch">
            <div class="border border-3 p-8-12 wh-100">
              <h5>For foodies</h5>
              <p class="m-0">
                Follow creators and places you love.
              </p>
            </div>
          </div>

          <div class="p-12 flex-self-stretch">
            <div class="border border-3 p-8-12 wh-100">
              <h5>For creators</h5>
              <p class="m-0">
                A place to publish your work.
              </p>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-32">
        <div class="FacebookButton border-3 text-center hover-pointer" @click="onSignInFacebook">
          Sign in with Facebook
        </div>
        <div class="flex-center mt-8">
          <p class="tiny">
            By signing up, you agree to Munch's terms of use and privacy policy.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "GetStartedDialog",
    methods: {
      onSignInFacebook() {
        this.$store.dispatch('account/signInFacebook')
        this.$store.commit('unfocus', 'Login')
      }
    }
  }
</script>

<style scoped lang="less">
  .LogoBanner {
    height: 64px;
  }

  .FacebookButton {
    height: 48px;
    line-height: 48px;

    font-size: 16px;
    font-weight: 600;

    color: white;
    background-color: #3b5998;
  }

  .Dialog {
    background: url('https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJnZXQtc3RhcnRlZC1iYWNrZ3JvdW5kLmpwZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTA4MCwiaGVpZ2h0IjoxMDgwLCJmaXQiOiJvdXRzaWRlIn19fQ==') no-repeat center/cover;
  }
</style>
