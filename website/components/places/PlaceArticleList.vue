<template>
  <div>
    <div class="container ArticleHeader">
      <h2>Recent Articles</h2>
      <div class="Controls">
        <div class="Left elevation-1 border-3" @click="prev"
             :class="{'whisper-100-bg': hasPrev, 'white-bg': !hasPrev}">
          <simple-svg class="Icon" fill="black" filepath="/img/places/caret_left.svg"/>
        </div>
        <div class="Right elevation-1 border-3" @click="next"
             :class="{'whisper-100-bg': hasNext, 'white-bg': !hasNext}">
          <simple-svg class="Icon" fill="black" filepath="/img/places/caret_right.svg"/>
        </div>
      </div>
    </div>

    <horizontal-scroll-view ref="scroll" class="PlaceArticleList" :items="articles" :map-key="a => a.articleId"
                            :padding="24" :nav="false" @notify="notify">
      <template slot-scope="{item,index}">
        <a :href="item.url" target="_blank" rel="noreferrer noopener nofollow" data-place-activity="partnerArticleItem"
           :data-place-activity-data="index">
          <div class="ArticleCard whisper-050-bg no-select elevation-1 elevation-hover-2 border-3 hover-pointer">
            <image-size class="Thumbnail index-content border-3-top" :image="item.thumbnail"/>
            <div class="Content">
              <div class="text text-ellipsis-2l black-a-85 Title">{{item.title}}</div>
              <div class="text text-ellipsis-4l Description">{{item.content || item.description}}</div>

              <div class="secondary-700 Brand">
                <simple-svg class="Icon" fill="black" filepath="/img/feed/article.svg"/>
                <div class="Name text text-ellipsis-1l">{{item.domain.name}}</div>
              </div>
            </div>
          </div>
        </a>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "PlaceArticleList",
    components: {HorizontalScrollView, ImageSize},
    props: {
      placeId: {
        required: true,
        type: String
      },
      preload: {
        required: true,
        type: Array
      }
    },
    data() {
      return {
        articles: this.preload,
        hasPrev: false,
        hasNext: false,
      }
    },
    methods: {
      prev() {
        this.$refs.scroll.prev()
      },
      next() {
        this.$refs.scroll.next()
      },
      notify({hasPrev, hasNext}) {
        this.hasPrev = hasPrev
        this.hasNext = hasNext
      }
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .ArticleHeader {
    h2 {
      margin-bottom: 16px;
    }

    display: flex;
    justify-content: space-between;

    .Controls {
      display: flex;
      height: 36px;
      margin-top: -2px;

      .Left, .Right {
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;

        width: 36px;
        height: 36px;

        .Icon {
          width: 20px;
          height: 20px;
        }
      }

      .Left {
        margin-right: 16px;
      }
    }
  }

  @image-height: 88px;
  @height: 202px + @image-height;
  .PlaceArticleList {
    height: @height + 8px;
    margin-bottom: -8px;

    .ArticleCard {
      width: 328px;
      height: @height;

      @media(max-width: 375.98px) {
        width: calc(100vw - 64px);
      }

      .Thumbnail {
        height: @image-height;
        width: 100%;
      }

      .Content {
        padding: 12px 18px;

        .Title {
          font-size: 17px;
          font-weight: 600;

          line-height: 27px;
          min-height: 27px;
          max-height: 54px;
        }

        .Description {
          margin-top: 8px;

          font-size: 15px;
          font-weight: 400;

          line-height: 22px;
          min-height: 22px;
          max-height: 88px;
        }

        .Brand {
          margin-top: 8px;

          display: flex;

          .Icon {
            width: 16px;
            height: 21px;
          }

          .Name {
            margin-left: 6px;

            font-size: 14px;
            font-weight: 600;

            line-height: 21px;
            height: 21px;
          }
        }
      }
    }
  }
</style>
