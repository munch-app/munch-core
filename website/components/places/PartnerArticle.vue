<template>
  <div class="PartnerArticle">
    <slick class="Slick" ref="slick" :options="options" @afterChange="onAfterChange">
      <a v-for="article in articles" :key="article.url" :href="article.url" target="_blank"
         rel="nofollow">
        <div class="ArticleCard no-select Elevation1 Border48">
          <image-size class="Image Border48Top" :image="article.thumbnail"/>

          <div class="Content">
            <h4 class="Title">{{article.title}}</h4>
            <div class="Description Regular">{{article.description}}</div>
            <div class="Brand Small">@{{article.brand}}</div>
          </div>
        </div>
      </a>
    </slick>
    <div class="Controls">
      <div class="Left Elevation1 Border24" @click="onPrev"
           :class="{'Secondary500Bg': hasPrev, 'Secondary100Bg': !hasPrev}">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_left.svg"/>
      </div>
      <div class="Right Elevation1 Border24" @click="onNext"
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
    name: "PartnerArticle",
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
          slidesToScroll: 1,
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
        articles: this.preload,
        currentSlide: 0,
        next: null,
      }
    },
    computed: {
      hasPrev() {
        return this.currentSlide > 0
      },
      hasNext() {
        return this.currentSlide + 1 < this.articles.length
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
  .PartnerArticle {
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

  .ArticleCard {
    &:hover {
      cursor: pointer;
    }
  }

  .Image {
    width: 100%;
    padding-top: 60%;
  }

  a {
    text-decoration: none;
    color: black;
  }

  /* < 576 Condition & Normal Condition */
  .ArticleCard {
    margin: 8px 18px 8px 1px;
    width: 244px;
    height: 310px;
  }

  .Content {
    padding: 10px 16px 16px 16px;
  }

  .Title {
    font-size: 16px;
    max-height: 47px;
    -webkit-line-clamp: 2;

    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
  }

  .Description {
    font-size: 13px;
    max-height: 56px;
    margin-top: 8px;

    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
  }

  .Brand {
    font-size: 11px;
    margin-top: 8px;
    height: 18px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  /* > 576 Condition */
  @media (min-width: 576px) {
    .ArticleCard {
      margin-right: 24px;
      width: 290px;
      height: 408px;
    }

    .Content {
      padding: 16px 24px 24px 24px;
    }

    .Title {
      font-size: 19px;
      max-height: 56px;
      -webkit-line-clamp: 2;
    }

    .Description {
      font-size: 16px;
      margin-top: 8px;
      max-height: 96px;
    }

    .Brand {
      margin-top: 16px;
      font-size: 12px;
      height: 18px;
    }
  }

  /* > 768 Condition */
  @media (min-width: 768px) {
    .ArticleCard {
      margin-right: 28px;
      width: 290px;
      height: 408px;
    }

    .Content {
      padding: 16px 24px 24px 24px;
    }

    .Title {
      font-size: 19px;
      max-height: 56px;
      -webkit-line-clamp: 2;
    }

    .Description {
      font-size: 16px;
    }

    .Brand {
      font-size: 12px;
    }
  }

  /* 1200 Condition*/
  @media (min-width: 1200px) {
    .ArticleCard {
      margin-right: 28px;
      width: 350px;
      height: 444px;
    }

    .Title {
    }

    .Description {
    }

    .Brand {

    }
  }
</style>
