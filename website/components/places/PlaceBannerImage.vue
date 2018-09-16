<template>
  <div class="PlaceBannerImage NoSelect">
    <slick class="Slick" ref="slick" :options="banner.options" @afterChange="onBannerAfter">
      <div v-for="(value, index) in images" :key="value.imageId" @click="onFullScreen(index)">
        <div class="BannerImage NoSelect">
          <image-size class="Image" :image="value">
            <div class="BannerContainer">
              <div v-if="value.profile && value.profile.name" class="Small UsernameButton Border4 Elevation1">
                {{value.profile.name}}
              </div>
            </div>
          </image-size>
        </div>
      </div>
    </slick>

    <div v-if="fullScreen.selected">
      <slick class="Slick FullScreen IndexOverlay NoSelect" ref="fullScreen" :options="fullScreen.options" @afterChange="onFullScreenAfter">
        <div v-for="image in images" :key="image.imageId">
          <div class="ImageContainer">
            <image-size class="Image" :image="image" :min="1000" :max="2048" object-fit="scale-down"/>
            <div class="Description Text" v-if="image.profile">
              <a :href="getUrl(image)" target="_blank">@{{image.profile.name}}</a>
            </div>
          </div>
        </div>
      </slick>
      <div class="Control">
        <div class="NavigationClose IndexOverlay" @click="onFullScreenClose"></div>
        <div class="NavigationLeft IndexOverlay" v-if="fullScreen.currentSlide !== 0" @click="onFullScreenPrev"></div>
        <div class="NavigationRight IndexOverlay" v-if="fullScreen.currentSlide !== images.length - 1" @click="onFullScreenNext"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import Slick from 'vue-slick';
  import "slick-carousel/slick/slick.css";
  import ImageSize from "../core/ImageSize";

  export default {
    name: "PlaceBannerImage",
    components: {ImageSize, Slick},
    props: {
      images: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        banner: {
          currentSlide: 0,
          options: {
            mobileFirst: true,
            speed: 300,
            infinite: false,
            slidesToShow: 1,
            variableWidth: true,
            centerMode: false,
            arrows: false,
            slidesToScroll: 1,
            dots: false,
            responsive: [
              {
                breakpoint: 1200,
                settings: {
                  slidesToScroll: 2,
                }
              },
              {
                breakpoint: 992,
                settings: {
                  slidesToScroll: 1,
                }
              }
            ]
          }
        },
        fullScreen: {
          selected: false,
          currentSlide: 0,
          options: {
            speed: 200,
            infinite: false,
            variableWidth: false,
            centerMode: false,
            arrows: false,
            dots: false,
            slidesToScroll: 1,
            initialSlide: 0
          },
        },
      }
    },
    methods: {
      onNext() {
        this.$refs.slick.next();
      },
      onPrev() {
        this.$refs.slick.prev();
      },
      getUrl(image) {
        switch (image.profile.type) {
          case 'instagram':
            return `https://instagram.com/${image.profile.name}`
        }
        return null
      },
      onFullScreen(index) {
        this.fullScreen.selected = true
        this.fullScreen.currentSlide = index
        this.fullScreen.options.initialSlide = index
        console.log(this.$refs.fullScreen)
      },
      onFullScreenClose() {
        this.fullScreen.selected = false
      },
      onFullScreenNext() {
        this.$refs.fullScreen.next();
      },
      onFullScreenPrev() {
        this.$refs.fullScreen.prev();
      },
      onFullScreenAfter(event, slick, currentSlide) {
        this.fullScreen.currentSlide = currentSlide
      },
      onBannerAfter(event, slick, currentSlide) {
        this.banner.currentSlide = currentSlide
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceBannerImage {
    width: 100%;
    margin-left: auto;
    margin-right: auto;
    position: relative;

    @media (min-width: 576px) {
      max-width: 540px;
      padding-left: 15px;
      padding-right: 15px;
    }

    @media (min-width: 768px) {
      max-width: 720px;
    }

    @media (min-width: 992px) {
      max-width: 960px;
    }

    @media (min-width: 1200px) {
      max-width: 1140px;
    }
  }

  .Slick {
    display: flex;
  }

  .Image {
    width: 100%;
    padding-top: 58%;
  }

  /* < 576 Condition & Normal Condition */
  .BannerImage {
    width: 100vw;

    .BannerContainer {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;

      display: flex;
      justify-content: flex-end;
      align-items: flex-end;

      .UsernameButton {
        background-color: rgba(255, 255, 255, 0.85);
        padding: 3px 8px;
        margin-right: 8px;
        margin-bottom: 8px;
      }
    }
  }

  /* > 576 Condition */
  @media (min-width: 576px) {
    .BannerImage {
      margin-right: 24px;
      width: 290px;

      .Image {
        padding-top: 60%;
      }
    }
  }

  /* > 768 Condition */
  @media (min-width: 768px) {
    .BannerImage {
      margin-right: 24px;
      width: 270px;
    }
  }

  /* 1200 Condition*/
  @media (min-width: 1200px) {
    .BannerImage {
      margin-right: 24px;
      width: 320px;
    }

    .Image {
      padding-top: 60%;
    }
  }

  .FullScreen {
    background: rgba(0, 0, 0, 0.85);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;

    .ImageContainer {
      position: relative;
      width: 100vw;
      height: 100vh;

      .Image {
        margin-top: 15vh;
        margin-left: auto;
        margin-right: auto;
        width: 70vw;
        padding-top: 70vh;
      }

      .Description {
        margin-top: 4px;
        margin-left: auto;
        margin-right: auto;
        width: 70vw;
        height: 24px;
        text-align: center;

        a {
          font-size: 17px;
          color: rgba(255, 255, 255, 0.85);
        }
      }
    }
  }

  .Control {
    .NavigationLeft, .NavigationRight {
      position: fixed;
      top: 0;
      bottom: 0;
      width: 8vw;
      background: rgba(255, 255, 255, 0.2);
      transition: all 0.3s cubic-bezier(.25, .8, .25, 1);

      &::after {
        display: inline-block;
        position: absolute;
        content: "";

        height: 18px;
        width: 18px;
        border-right: 3px solid white;
        border-bottom: 3px solid white;
        top: calc(50% - 14px);
      }

      &:hover {
        cursor: pointer;
        background: rgba(255, 255, 255, 0.35);
      }
    }

    .NavigationClose {
      position: fixed;
      top: 0;
      right: 8vw;
      margin-right: 8px;
      margin-top: 24px;
      width: 8vw;
      height: 8vw;

      &:hover {
        opacity: 1;
      }
      &::before, &::after {
        position: absolute;
        left: 15px;
        content: ' ';
        height: 28px;
        width: 2px;
        background-color: white;
      }

      &::before {
        transform: rotate(45deg);
      }
      &::after {
        transform: rotate(-45deg);
      }
    }

    .NavigationLeft {
      left: 0;
      &::after {
        right: calc(50% - 14px);
        transform: rotate(135deg);
      }
    }

    .NavigationRight {
      right: 0;
      &::after {
        left: calc(50% - 14px);
        transform: rotate(-45deg);
      }
    }

    @media (max-width: 765.98px) {
      .NavigationLeft, .NavigationRight {
        width: 10vw;
      }

      .NavigationClose {
        right: 10vw;
      }
    }

    @media (max-width: 575.98px) {
      .NavigationLeft, .NavigationRight {
        width: 11vw;
      }

      .NavigationClose {
        right: 11vw;
      }
    }

    @media (min-width: 992px) {
      .NavigationLeft, .NavigationRight {
        width: 64px;
      }

      .NavigationClose {
        right: 64px;
      }
    }
  }
</style>
