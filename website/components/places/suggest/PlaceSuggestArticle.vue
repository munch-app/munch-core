<template>
  <div>
    <p>Own a blog, want it published on munch? <a class="text-underline s700" href="https://partner.munch.app"
                                                  target="_blank">partner.munch.app</a></p>

    <div class="Existing" v-if="data.articles.length > 0">
      <h2>Existing Articles</h2>

      <div class="List">
        <div class="Article flex" v-for="article in data.articles" :key="article.articleId">
          <div>
            <image-sizes class="Image border-3 overflow-hidden" v-if="article.thumbnail" :sizes="article.thumbnail.sizes"/>
          </div>

          <div class="Content flex-column-space-between">
            <h5 class="text-ellipsis-2l">{{article.title}}</h5>
            <h6 class="text-ellipsis-1l">by <span class="s700">{{article.domain.name}}</span></h6>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "PlaceSuggestArticle",
    components: {ImageSizes},
    props: {
      data: Object
    }
  }
</script>

<style scoped lang="less">
  .Existing {
    margin-top: 32px;

    .List {
      margin-top: 8px;
    }
  }

  .Article {
    padding: 12px 0;

    .Image {
      width: 100px;
      height: 80px;
    }

    .Content {
      margin-left: 20px;
    }
  }
</style>
