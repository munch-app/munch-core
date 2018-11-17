<!--suppress CssUnknownTarget-->
<template>
  <portal to="dialog-w400" class="zero">
    <div class="relative">
      <div class="BrandLogo absolute flex-center">
        <img src="~/assets/img/MunchLogoOnboarding.svg" style="width: 66%">
      </div>

      <div class="CloseButton p-16 flex-end weight-600 absolute white w-100 hover-pointer" @click="$store.commit('unfocus', 'Login')">
        CLOSE
      </div>

      <div class="Banner border-4">
        <div class="Container border-4 flex-column-justify-end white text-center p-16-24">
          <p>
            Whether you're looking for the perfect date spot or the hottest bar in town - Munch helps you answer
            the question:
          </p>

          <p class="weight-600 mt-16">
            'What do you want to eat?'
          </p>
        </div>
      </div>
    </div>

    <div class="mt-16">
      <div class="FacebookButton border-3 text-center hover-pointer" @click="onSignInFacebook">
        Sign in with Facebook
      </div>
      <p class="PrivacyPolicy mt-8">
        By signing up, you agree to Munch's terms of use and privacy policy.
      </p>
    </div>
  </portal>
</template>

<script>
  export default {
    name: "ProfileOnBoarding",
    methods: {
      onSignInFacebook() {
        this.$store.dispatch('user/signInFacebook')
        this.$store.commit('unfocus', 'Login')
      }
    }
  }
</script>

<style scoped lang="less">
  .BrandLogo {
    margin-top: 48px;
  }

  .CloseButton {
    font-size: 15px;
    line-height: 1.5;
  }

  .Banner {
    background: url('/img/profile/onboarding_1.jpg') no-repeat center/cover;
  }

  .Container {
    height: 400px;
    background: rgba(0, 0, 0, 0.5);
  }

  .FacebookButton {
    height: 48px;
    line-height: 48px;

    font-size: 16px;
    font-weight: 600;

    color: white;
    background-color: #3b5998;
  }

  .PrivacyPolicy {
    font-size: 12px;
    line-height: 16px;
  }
</style>
