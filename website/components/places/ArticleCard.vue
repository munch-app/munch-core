<template>
  <a class="ArticleCard Elevation1 Border48" :href="article.url" target="_blank" rel="nofollow">
    <image-size class="Image Border48Top BorderImage" :image="article.thumbnail"/>

    <div class="Content">
      <p class="Title Large" v-line-clamp="{lines: 2, text: article.title}"></p>
      <p class="Description Regular" v-line-clamp="{lines: 4, text: article.description}"></p>
      <p class="Brand Small">By @{{article.brand}}</p>
    </div>
  </a>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    components: {ImageSize},
    props: ["article"],
    name: "ArticleCard"
  }
</script>

<style scoped lang="less">
  .ArticleCard {
    &:hover {
      cursor: pointer;
    }

    .Image {
      width: 100%;
      padding-top: 60%;
    }

    .Content {
      margin: 16px 24px 24px 24px;

      p {
        margin: 0;
        padding: 0;
      }

      .Title {
      }

      .Description {
        margin-top: 8px;
      }

      .Brand {
        margin-top: 16px;
      }
    }
  }

</style>
