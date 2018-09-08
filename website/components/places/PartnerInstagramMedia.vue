<template>
  <div class="PartnerInstagramMedia">
    <slick class="Slick NoSelect" ref="slick" :options="options">
      <a class="MediaCard" v-for="media in medias" :key="media.mediaId" :href="media.link" target="_blank"
         rel="nofollow">
        <image-size class="Image Elevation1 Border48 BorderImage" :image="media.image">
          <div class="ImageBox">
            <div class="Small UsernameButton Border24 Elevation1">@{{media.user.username}}</div>
          </div>
        </image-size>
        <div class="Small UsernameLabel">@{{media.user.username}}</div>
      </a>
    </slick>
  </div>
</template>

<script>
  import Slick from 'vue-slick';
  import "slick-carousel/slick/slick.css";
  import ImageSize from "../core/ImageSize";

  export default {
    name: "PartnerInstagramMedia",
    components: {ImageSize, Slick},
    props: {
      /* PlaceId to query more instagram media */
      placeId: {
        required: true,
        type: String
      },
      /* Preload Instagram Medias */
      preload: {
        required: true,
        type: Array
      }
    },
    data() {
      return {
        options: {
          speed: 300,
          infinite: false,
          slidesToShow: 1,
          variableWidth: true,
          centerMode: false,
          arrows: false,
          slidesToScroll: 2,
          responsive: [
            {
              breakpoint: 768,
              settings: {}
            }
          ]
        },
        medias: this.preload,
        next: null,
      }
    },
    methods: {}
  }
</script>

<style scoped lang="less">
  .PartnerInstagramMedia {
    .Slick {
      padding-left: 15px;
      width: 100%;
      margin-left: auto;
      margin-right: auto;

      @media (min-width: 576px) {
        padding-right: 15px;
        max-width: 540px;
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
  }

  .MediaCard {
    &:hover {
      cursor: pointer;
    }
  }

  a {
    text-decoration: none;
    color: black;
  }

  /* < 576 Condition */
  .Image {
    margin-right: 12px;
    height: 28vw;
    width: 28vw;
  }

  .UsernameButton {
    display: none;
  }

  .UsernameLabel {
    margin-top: 4px;
    width: 28vw;
    display: block;

    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  /* > 576 Condition */
  @media (min-width: 576px) {
    .Image {
      margin-right: 18px;
    }
  }

  /* > 768 Condition */
  @media (min-width: 768px) {
    .Image {
      margin-right: 24px;
      height: 20vw;
      width: 20vw;
    }

    .UsernameButton {
      display: block;
    }

    .UsernameLabel {
      display: none;
      width: 20vw;
    }
  }

  /* 1200 Condition*/
  @media (min-width: 1200px) {
    .Image {
      margin-right: 30px;
      width: 255px;
      height: 255px;
    }
  }

  .ImageBox {
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
    padding: 4px 8px;
    margin-right: 8px;
    margin-bottom: 8px;
  }

  .UsernameLabel {

  }
</style>
