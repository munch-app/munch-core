<template>
  <div>
    <horizontal-scroll-view ref="scroll" class="PlaceArticleList" :items="items" :map-key="a => a.articleId"
                            :padding="24" :container="false">

      <template slot-scope="{item,index}">
        <a :href="item.url" target="_blank" rel="noreferrer noopener nofollow" data-place-activity="partnerArticleItem"
           :data-place-activity-data="index">
          <div class="ArticleCard border border-3 hover-pointer overflow-hidden">
            <div>
              <image-sizes class="Thumbnail index-content border-3-top" v-if="item.thumbnail"
                           :sizes="item.thumbnail.sizes"/>
            </div>
            <div class="ContentArea">
              <h5 class="text-ellipsis-2l">{{item.title}}</h5>
              <p class="subtext mt-16 text-ellipsis-4l">{{item.description}}</p>


              <div class="mt-24 flex-between">
                <div class="Domain">
                  <h6 class="s700 text-ellipsis-1l">{{item.domain.name}}</h6>
                  <p class="subtext">{{format(item.createdMillis, 'mmm dd, yyyy')}}</p>
                </div>

                <div class="Action">
                  <button class="small border">Read More</button>
                </div>
              </div>
            </div>
          </div>
        </a>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import dateformat from 'dateformat'

  import MasonryWall from "../core/MasonryWall";
  import ImageSizes from "../core/ImageSizes";
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "PlaceArticleWall",
    components: {HorizontalScrollView, ImageSizes, MasonryWall},
    props: {
      placeId: {
        required: true,
        type: String
      },
      preload: {
        type: Array,
        required: false
      }
    },
    data() {
      return {
        items: this.preload || [],
        loading: false,
        next: {sort: null}
      }
    },
    mounted() {
      if (this.preload && this.preload.length > 0) {
        this.next.sort = this.preload[this.preload.length - 1].sort
      } else {
        // First Load
        this.append(() => {
          this.$refs.wall.fill()
        }, true)
      }
    },
    methods: {
      append(then, force) {

      },
      format: dateformat,
    }
  }
</script>

<style scoped lang="less">
  .PlaceArticleList {
    height: 400px;
  }

  .ArticleCard {
    width: 330px;
  }

  .Thumbnail {
    height: 128px;
  }

  .ContentArea {
    padding: 16px 20px;
  }

  .Action button {
    font-size: 14px;
  }
</style>
