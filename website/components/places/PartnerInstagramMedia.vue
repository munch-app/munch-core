<template>
  <div class="PartnerInstagramMedia">
    <slick class="Slick no-select" ref="slick" :options="options" @afterChange="onAfterChange">
      <a class="MediaCard" v-for="media in medias" :key="media.mediaId" :href="media.link" target="_blank"
         rel="nofollow">
        <image-size class="Image elevation-1 border-4" :image="media.image">
          <div class="ImageBox">
            <div class="Small UsernameButton border-3 elevation-1">@{{media.user.username}}</div>
          </div>
        </image-size>
        <div class="Small UsernameLabel">@{{media.user.username}}</div>
      </a>
    </slick>

    <div class="Controls">
      <div class="Left elevation-1 border-3" @click="onPrev"
           :class="{'Secondary500Bg': hasPrev, 'Secondary100Bg': !hasPrev}">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_left.svg"/>
      </div>
      <div class="Right elevation-1 border-3" @click="onNext"
           :class="{'Secondary500Bg': hasNext, 'Secondary100Bg': !hasNext}">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_right.svg"/>
      </div>
    </div>
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
          mobileFirst : true,
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
        currentSlide: 0,
        next: null,
      }
    },
    computed: {
      hasPrev() {
        return this.currentSlide > 0
      },
      hasNext() {
        return this.currentSlide + 1 < this.medias.length
      }
    },
    methods: {
      onNext() {
        this.$refs.slick.next();
      },
      onPrev() {
        this.$refs.slick.prev();
      },
      onAfterChange(event, slick, currentSlide) {
        this.currentSlide = currentSlide
      }
    }
  }
</script>

<style scoped lang="less">
  .PartnerInstagramMedia {
    .Slick, .Controls {
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

  .Slick {
    display: flex;
  }

  .Controls {
    display: none;
    justify-content: flex-end;
    align-items: flex-end;
    margin-top: 8px;

    .Page, .Left, .Right {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;

      width: 40px;
      height: 40px;
      margin-left: 16px;

      .Icon {
        width: 20px;
        height: 20px;
      }
    }

    @media (min-width: 576px) {
      display: flex;
    }
  }

  .MediaCard {
     margin-top: 8px;
     margin-bottom: 8px;

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
    margin-right: 16px;
    height: 33vw;
    width: 33vw;
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
      height: 22vw;
      width: 22vw;
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
