<template>
  <div v-if="state === 'sign-up' || state === 'sign-in'" class="SignUp dialog-small">
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

      <div class="mt-32" v-if="state === 'sign-up'">
        <div class="FacebookButton border-3 text-center hover-pointer" @click="onSignInFacebook">
          Sign up with Facebook
        </div>
        <div class="flex-center mt-8">
          <p class="tiny">
            By signing up, you agree to Munch's terms of use and privacy policy.
          </p>
        </div>
      </div>
      <div class="mt-32" v-else>
        <div class="FacebookButton border-3 text-center hover-pointer" @click="onSignInFacebook">
          Sign in with Facebook
        </div>
        <div class="flex-center mt-8">
          <p class="tiny">
            By signing in, you agree to Munch's terms of use and privacy policy.
          </p>
        </div>
      </div>
    </div>
  </div>

  <div class="dialog-small" v-else-if="state === 'setup'">
    <div class="flex-column">
      <div>
        <h3>We need more information to setup your account.</h3>
      </div>
      <div class="mt-24">
        <input-text label="Email" v-model="account.email"/>
      </div>
      <div class="mt-24">
        <input-text label="Name" v-model="account.profile.name"/>
      </div>
      <div class="mt-24 flex-end">
        <button @click="onSetup" class="blue-outline">Continue</button>
      </div>
    </div>
  </div>

  <div class="dialog-small" v-else-if="state === 'loading'">
    <div class="flex-center p-24">
      <beat-loader color="#07F" size="16px"/>
    </div>
  </div>

  <div class="dialog-small" v-else-if="state === 'error-popup'">
    <div class="flex-column">
      <h3>Authentication Error</h3>
      <p class="mt-16">The popup window required for authenticating the user has been closed before it can complete it operations.</p>
      <p>Safari private browsing is known to cause this error.</p>
    </div>
  </div>

  <div class="dialog-small" v-else>Unknown State</div>
</template>

<script>
  import authenticator from '~/services/authenticator'
  import InputText from "../core/InputText";

  export default {
    name: "GetStartedDialog",
    components: {InputText},
    props: {
      defaultState: {
        type: String,
        default: 'sign-up'
      }
    },
    data() {
      return {
        state: this.defaultState,
        account: null
      }
    },
    methods: {
      onSignInFacebook() {
        this.state = 'loading'

        this.$store.dispatch('account/signInFacebook').then(() => {
          this.$store.commit('global/clearDialog')
        }).catch(err => {
          if (err.code === 'auth/popup-closed-by-user') {
            this.state = 'error-popup'
            return
          }

          if (err.type === 'err.munch.app/app.munch.err.AuthenticateException') {
            return authenticator.getUser()
              .then(user => {
                this.account = {
                  email: user.email,
                  profile: {name: user.displayName}
                }
                this.state = 'setup'
              })
              .catch(err => {
                this.$store.dispatch('addError', err)
              })
          }

          return this.$store.dispatch('addError', err)
        })
      },
      onSetup() {
        this.state = 'loading'
        this.$store.dispatch('account/setup', this.account).then(() => {
          this.$store.commit('global/clearDialog')
        }).catch((err) => {
          this.state = 'setup'
          this.$store.dispatch('addError', err)
        })
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

  .SignUp {
    background: url('https://cdn.munch.app/eyJidWNrZXQiOiJtaDQiLCJrZXkiOiJnZXQtc3RhcnRlZC1iYWNrZ3JvdW5kLmpwZyIsImVkaXRzIjp7InJlc2l6ZSI6eyJ3aWR0aCI6MTA4MCwiaGVpZ2h0IjoxMDgwLCJmaXQiOiJvdXRzaWRlIn19fQ==') no-repeat center/cover;
  }
</style>
