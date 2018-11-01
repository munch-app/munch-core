<template>
  <div class="PlaceMenu">
    <a class="WebsiteMenuButton elevation-1 border-3 border hover-pointer elevation-hover-2" v-if="menu.url"
       :href="menu.url" target="_blank" rel="noopener nofollow noreferrer">
      <simple-svg class="Icon" fill="rgba(0, 0, 0, 0.75)" filepath="/img/places/open_tab.svg"/>
      <div class="Label">Website Menu</div>
    </a>

    <horizontal-scroll-view class="MenuImageList" :items="menu.images" :map-key="i => i.imageId" :container="false"
                            :nav-white="false" :padding="24" v-if="!isProduction">
      <template slot-scope="{item}">
        <div class="MenuImageItem hover-pointer">
          <image-size :image="item" class="Image border-3">
            <div class="MenuImageContainer">
              <div class="Author" v-if="item.profile && item.profile.name">
                <simple-svg v-if="item.profile.type === 'instagram'" class="Icon" fill="rgba(255,255,255,0.95)"
                            :filepath="require('~/assets/icon/feed/instagram.svg')"/>
                <simple-svg v-if="item.profile.type === 'article'" class="Icon" fill="rgba(255,255,255,0.95)"
                            :filepath="require('~/assets/icon/feed/article.svg')"/>
                <div class="Name text-ellipsis-1-line">{{item.profile.name}}</div>
              </div>
            </div>
          </image-size>
        </div>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  import ImageSize from "../core/ImageSize";
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "PlaceMenu",
    components: {HorizontalScrollView, ImageSize},
    props: {
      menu: {
        required: true,
        type: Object
      }
    },
    computed: {
      ...mapGetters(['isProduction']),
    }
  }
</script>

<style scoped lang="less">
  .WebsiteMenuButton {
    padding: 6px 12px;
    display: inline-flex;

    .Icon {
      width: 24px;
      height: 24px;
    }

    .Label {
      margin-left: 8px;
      margin-right: 2px;
      height: 24px;
      line-height: 24px;
      font-weight: 600;
      font-size: 15px;

      color: rgba(0, 0, 0, 0.75);
    }
  }

  .MenuImageList {
    @height: 120px;

    margin-top: 24px;
    height: @height;

    .MenuImageItem {
      width: @height;
      height: @height;

      .Image {
        width: 100%;
        height: 100%;
      }

      .MenuImageContainer {
        width: 100%;
        height: 100%;
        padding: 6px;

        display: flex;
        flex-direction: column;
        justify-content: flex-end;

        transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
        background-color: rgba(0, 0, 0, 0.3);

        &:hover {
          background-color: rgba(0, 0, 0, 0.5);
        }

        .Author {
          display: flex;

          .Icon {
            width: 12px;
            height: 12px;
            flex-shrink: 0;
          }

          .Name {
            flex-shrink: 1;
            margin-left: 3px;

            height: 12px;
            line-height: 12px;
            font-size: 11px;
            font-weight: 500;
            color: rgba(255, 255, 255, 0.95);
          }
        }
      }
    }
  }
</style>
