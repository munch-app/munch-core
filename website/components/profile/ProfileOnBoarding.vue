<!--suppress CssUnknownTarget-->
<!--Need to merge this to use portal-dialog-->

<template>
  <div class="zero-spacing">
    <div class="ProfileOnBoarding index-dialog">
      <div class="Dialog elevation-3">
        <div class="LoginBanner">
          <div class="BrandLogo">
            <img src="/img/MunchLogo_White.svg">
            <h1>Munch</h1>
          </div>
          <div class="CloseButton" @click="$store.commit('unfocus', 'Login')">
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
          <div class="FacebookButton" @click="onSignInFacebook">
            Sign in with Facebook
          </div>
          <div class="text PrivacyPolicy">
            By signing up, you agree to Munch's terms of use and privacy policy.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Slick from 'vue-slick';
  import "slick-carousel/slick/slick.css";

  export default {
    name: "ProfileOnBoarding",
    components: {Slick},
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
  @height: 400px; // Banner Height
  @width: 360px; // FirebaseUI Button Restriction

  .ProfileOnBoarding {
    position: fixed;
    top: 50%;
    right: 0;
    bottom: 0;
    left: 0;

    .Dialog {
      border-radius: 4px;
      overflow: hidden;
      width: @width;
      height: @height + 120px;

      background-color: white;
      margin: -(@height + 120px)/2 auto 0;
    }

    @media (max-width: 767.98px) {
      position: fixed;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;

      .Dialog {
        border-radius: 0;
        width: 100vw;
        margin: 0;
      }

      .LoginBanner {
        width: 100vw;

        .CloseButton {
          width: 100vw;
        }

        .BrandLogo {
          width: 100vw;
        }

        .BannerContent {
          width: 100vw;

          .Contain {
            width: 100vw;
          }
        }
      }
    }
  }

  .LoginBanner {
    margin: 0 auto;
    height: @height;

    .BrandLogo {
      width: @width;
      position: absolute;
      z-index: 1;
      margin-top: 48px;
      display: flex;
      align-items: center;
      justify-content: center;

      h1 {
        text-align: center;
        color: white;
        margin-left: 8px;
      }
    }

    .CloseButton {
      width: @width;
      position: absolute;
      z-index: 1;
      font-weight: 600;
      font-size: 15px;
      line-height: 1.5;
      color: rgba(255, 255, 255, 0.95);
      text-transform: uppercase;

      padding-top: 12px;
      padding-right: 16px;
      display: flex;
      justify-content: flex-end;
    }

    .Slick {
      display: flex;
    }

    .BannerContent {
      flex-shrink: 0;
      height: @height;
      width: @width;
      overflow: hidden;

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
        height: @height;
        width: @width;
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
  }

  .LoginBottom {
    display: flex;
    flex-direction: column;
    padding: 16px;

    .FacebookButton {
      border-radius: 3px;
      height: 48px;
      line-height: 48px;
      text-align: center;
      margin: 0 0;
      font-size: 16px;
      font-weight: 600;
      background-color: #3b5998;
      color: white;
    }

    .PrivacyPolicy {
      font-size: 12px;
      line-height: 16px;
      margin-top: 12px;
      margin-bottom: 16px;
    }
  }
</style>
