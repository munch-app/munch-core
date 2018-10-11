<template>
  <div class="FeedArticleCard no-select elevation-2 elevation-hover-3 border-3 hover-pointer">
    <image-size v-if="imageHeight" class="Image border-3-top" :image="article.thumbnail"
                :style="{paddingTop: imageHeight}"
    />

    <div class="Content">
      <h3 class="Title">{{article.title}}</h3>
      <p class="Description">{{article.content || article.description}}</p>

      <h4 class="Brand secondary">{{article.domain.name}}</h4>
    </div>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "FeedArticleCard",
    components: {ImageSize},
    props: {
      article: {
        required: true,
        type: Object
      },
    },
    computed: {
      imageHeight() {
        const thumbnail = this.article.thumbnail
        const size = thumbnail && thumbnail.sizes && thumbnail.sizes[0]
        if (size) {
          const width = parseFloat(size.width)
          const height = parseFloat(size.height)
          return `${height/width * 100}%`
        }
      }
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .FeedArticleCard {
    .Image {
      width: 100%;
      height: auto;
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
</style>
