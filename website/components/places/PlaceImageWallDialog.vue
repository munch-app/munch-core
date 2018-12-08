<template>
  <dialog-navigation @next="$emit('next')" @prev="$emit('prev')" @close="$emit('close')" max-width="600px">
    <div class="ImageWallDialog w-100">
      <div class="Header">
        <h3 class="text-ellipsis-1l">{{item.title || item.caption}}</h3>
        <a v-if="item.instagram" class="Author block" :href="`https://instagram.com/${item.instagram.username}`"
           target="_blank" rel="noreferrer nofollow noopener">
          <h6>by <span class="s700">{{item.instagram.username}}</span> on {{format(item.createdMillis, 'mmm dd, yyyy')}}
          </h6>
        </a>
        <a v-if="item.article" class="Author block" :href="item.article.domain.url"
           target="_blank" rel="noreferrer nofollow noopener">
          <h6>by <span class="s700">{{item.article.domain.name}}</span> on {{format(item.createdMillis, 'mmm dd,yyyy')}}
          </h6>
        </a>
      </div>

      <image-sizes class="Image" :sizes="item.sizes"
                   width="800" height="1000" object-fit="contain"
                   max-height="calc(100vh - 80px - 48px - 64px)">
        <div class="ImageContainer flex-column-justify-end">
          <a v-if="item.instagram" target="_blank" rel="noreferrer nofollow noopener"
             class="flex" :href="`https://instagram.com/${item.instagram.username}`">
            <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/instagram.svg')"/>
            <div class="Name">{{item.instagram.username}}</div>
          </a>
          <a v-else-if="item.article" target="_blank" rel="noreferrer nofollow noopener"
             class="flex" :href="item.article.domain.url">
            <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/article.svg')"/>
            <div class="Name">{{item.article.domain.name}}</div>
          </a>
        </div>
      </image-sizes>

      <div class="Info Article" v-if="item.article">
        <div class="Title text-ellipsis-3l" v-if="item.title">
          {{item.title}}
        </div>
        <a class="ReadMore flex-justify-end" target="_blank" rel="noreferrer nofollow noopener" :href="item.article.url">
          <div class="s700">Read More</div>
        </a>
      </div>

      <div class="Info Instagram" v-if="item.instagram">
        <div class="Title text-ellipsis-3l" v-if="item.caption">
          {{item.caption}}
        </div>
      </div>
    </div>
  </dialog-navigation>
</template>

<script>
  import dateformat from 'dateformat'
  import ImageSizes from "../core/ImageSizes";
  import ImageSize from "../core/ImageSize";
  import DialogNavigation from "../layouts/DialogNavigation";

  export default {
    name: "PlaceImageWallDialog",
    components: {DialogNavigation, ImageSize, ImageSizes},
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    methods: {
      format: dateformat,
    }
  }
</script>

<style scoped lang="less">
  .Header {
    padding: 16px 24px;

    .Author {
      margin-top: 2px;
    }
  }

  .Image {
    background-color: rgba(0, 0, 0, 0.75);

    .ImageContainer {
      padding: 16px 24px;

      .Icon {
        width: 22px;
        height: 22px;
      }

      .Name {
        margin-left: 3px;

        height: 24px;
        line-height: 22px;
        font-size: 14px;
        font-weight: 600;
        color: white;
      }
    }
  }

  .Info {
    padding: 16px 24px 24px;

    .Title {
      font-size: 16px;
      font-weight: 600;

      line-height: 26px;
      min-height: 26px;
      max-height: 78px;

      color: rgba(0, 0, 0, 0.85);
    }

    &.Article {
      .ReadMore {
        height: 24px;
        line-height: 24px;

        font-weight: 600;
        font-size: 17px;

        margin-top: 8px;
      }
    }
  }
</style>
