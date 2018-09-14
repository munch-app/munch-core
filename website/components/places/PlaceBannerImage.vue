<template>
  <div class="PlaceBannerImage">
    <div class="NavigationRight" v-if="images.length > 3" @click="onNext"></div>
    <slick class="Slick" ref="slick" :options="options">
      <div v-for="image in images" :key="image.imageId">
        <div class="BannerImage NoSelect">
          <image-size class="Image" :image="image">
            <div class="BannerContainer">
              <div v-if="image.profile && image.profile.name" class="Small UsernameButton Border4 Elevation1">
                {{image.profile.name}}
              </div>
            </div>
          </image-size>
        </div>
      </div>
    </slick>
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
        options: {
          mobileFirst : true,
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
        },
      }
    },
    methods: {
      onNext() {
        this.$refs.slick.next();
      },
      onPrev() {
        this.$refs.slick.prev();
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

  .NavigationRight {
    display: none;
    position: absolute;

    width: 40px;
    height: 100%;
    right: 0;
    margin-right: 15px;
    z-index: 1;

    background-color: rgba(108, 160, 181, 0.82);

    &::after {
      display: inline-block;
      position: absolute;
      content: "";

      height: 18px;
      width: 18px;
      border-right: 3px solid white;
      border-bottom: 3px solid white;

      transform: rotate(-45deg);

      left: 7px;
      top: calc(50% - 14px);
    }

    @Secondary500: #0A6284;

    &:hover {
      cursor: pointer;
      background: @Secondary500;
    }
  }

  /* < 576 Condition & Normal Condition */
  .BannerImage {
    width: 100vw;
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

    .NavigationRight {
      display: block;
    }
  }

  .BannerContainer {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;

    display: flex;
    justify-content: flex-end;
    align-items: flex-end;
  }

  .UsernameButton {
    background-color: rgba(255, 255, 255, 0.85);
    padding: 3px 8px;
    margin-right: 8px;
    margin-bottom: 8px;
  }
</style>
