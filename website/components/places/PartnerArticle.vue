<template>
  <div class="PartnerArticle">
    <slick class="Slick container-full" ref="slick" :options="options" @afterChange="onAfterChange">
      <div><div class="gutter"/></div>

      <a v-for="(article, index) in articles" :key="article.url" :href="article.url" target="_blank"
         rel="noreferrer noopener nofollow" data-place-activity="partnerArticleItem" :data-place-activity-data="index">
        <div class="ArticleCard no-select elevation-2 elevation-hover-3 border-3 hover-pointer">
          <image-size class="Image border-3-top" :image="article.thumbnail"/>

          <div class="Content">
            <h3 class="Title">{{article.title}}</h3>
            <p class="Description">{{article.content || article.description}}</p>

            <h4 class="Brand secondary">{{article.domain.name}}</h4>
          </div>
        </div>
      </a>
    </slick>
    <div class="Controls container">
      <div class="Left elevation-1 border-3" @click="onPrev"
           :class="{'secondary-500-bg': hasPrev, 'secondary-100-bg': !hasPrev}">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_left.svg"/>
      </div>
      <div class="Right elevation-1 border-3" @click="onNext"
           :class="{'secondary-500-bg': hasNext, 'secondary-100-bg': !hasNext}">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_right.svg"/>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  const Activity = require('~/services/user/place-activity')

  export default {
    name: "PartnerArticle",
    components: {ImageSize},
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
          mobileFirst: true,
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
        Activity.navigation.partnerArticleItem(this.placeId, currentSlide)
      }
    }
  }
</script>

<style scoped lang="less">
  .Slick {
    display: flex;
    overflow: hidden;
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

  a {
    text-decoration: none;
    color: black;
  }

  .ArticleCard {
    margin: 24px 24px 24px 0;
    width: 300px;
    height: 394px;

    .Image {
      width: 100%;
      padding-top: 55%;
    }

    .Content {
      padding: 16px 24px 16px 24px;

      .Title {
        font-size: 19px;
        max-height: 56px;
        -webkit-line-clamp: 2;

        overflow: hidden;
        display: -webkit-box;
        -webkit-box-orient: vertical;
      }

      .Description {
        font-size: 16px;
        line-height: 1.5;

        max-height: 96px;
        margin-top: 8px;

        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
      }

      .Brand {
        font-size: 16px;
        height: 24px;

        margin-top: 16px;

        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }

  @media (min-width: 1200px) {
    .ArticleCard {
      margin-right: 32px;
      width: 340px;
      height: 420px;
    }
  }
</style>
