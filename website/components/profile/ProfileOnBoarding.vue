<!--suppress CssUnknownTarget-->
<!--Need to merge this to use portal-dialog-->

<template>
  <portal to="dialog-w400" class="zero-spacing">
    <div class="LoginBanner">
      <div class="BrandLogo flex-center">
        <img src="/img/MunchLogo_White.svg">
        <h1>Munch</h1>
      </div>

      <div class="CloseButton hover-pointer" @click="$store.commit('unfocus', 'Login')">
        Close
      </div>
      <slick class="Slick no-select" ref="slick" :options="options">
        <div class="BannerContent B_1">
          <div class="Contain">
            <h2>Welcome to Munch</h2>

            <p>
              Whether you're looking for the perfect date spot or the hottest bar in town - Munch helps you answer
              the question:
            </p>
            <p class="weight-600">
              'What do you want to eat?'
            </p>
          </div>
        </div>
        <div class="BannerContent B_2">
          <div class="Contain">
            <h2>Discover Delicious</h2>
            <p>Explore thousands of restaurants, bars and hawkers in the app. Find places nearby or on the other end
              of the island.</p>
          </div>
        </div>
        <div class="BannerContent B_3">
          <div class="Contain">
            <h2>Never Forget</h2>
            <p>Save places that you want to check out or create themed lists to keep track of places.</p>
          </div>
        </div>
      </slick>
    </div>

    <div class="LoginBottom">
      <div class="FacebookButton border-3 text-center hover-pointer" @click="onSignInFacebook">
        Sign in with Facebook
      </div>
      <div class="text PrivacyPolicy">
        By signing up, you agree to Munch's terms of use and privacy policy.
      </div>
    </div>
  </portal>
</template>

<script>
  export default {
    name: "ProfileOnBoarding",
    data() {
      return {
        options: {
          mobileFirst: true,
          speed: 300,
          infinite: false,
          slidesToShow: 1,
          variableWidth: false,
          centerMode: false,
          arrows: false,
          slidesToScroll: 1,
        }
      }
    },
    methods: {
      onSignInFacebook() {
        this.$store.dispatch('user/signInFacebook')
        this.$store.commit('unfocus', 'Login')
      }
    }
  }
</script>

<style scoped lang="less">
  .LoginBanner {
    position: relative;
    .BrandLogo {
      z-index: 1;
      position: absolute;
      margin-top: 48px;
      width: 100%;

      h1 {
        color: white;
        margin-left: 8px;
      }
    }

    .CloseButton {
      width: 100%;

      position: absolute;
      z-index: 1;
      font-weight: 600;
      font-size: 15px;
      line-height: 1.5;

      color: rgba(255, 255, 255, 0.95);
      text-transform: uppercase;

      padding-top: 16px;
      padding-right: 18px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .Slick {
    display: flex;
  }

  .BannerContent {
    overflow: hidden;
    border-radius: 4px;

    &.B_1 {
      background: url('/img/profile/onboarding_1.jpg') no-repeat center/cover;
    }

    &.B_2 {
      background: url('/img/profile/onboarding_2.jpg') no-repeat center/cover;
    }

    &.B_3 {
      background: url('/img/profile/onboarding_3.jpg') no-repeat center/cover;
    }

    .Contain {
      height: 400px;
      background: rgba(0, 0, 0, 0.44);

      display: flex;
      flex-direction: column;
      flex-wrap: wrap;
      align-content: flex-end;
      justify-content: flex-end;
      padding-bottom: 16px;
    }

    h1, h2, h3, p {
      text-align: center;
      color: white;
      padding: 0 24px;
    }

    h2 {
      padding-top: 48px;
      padding-bottom: 24px;
    }

    p {
      color: white;
      font-size: 16px;
      padding-bottom: 8px;
    }
  }

  .LoginBottom {
    margin-top: 16px;

    .FacebookButton {
      height: 48px;
      line-height: 48px;
      font-size: 16px;
      font-weight: 600;

      margin: 0 0;
      background-color: #3b5998;
      color: white;
    }

    .PrivacyPolicy {
      font-size: 12px;
      line-height: 16px;
      margin-top: 8px;
    }
  }
</style>
