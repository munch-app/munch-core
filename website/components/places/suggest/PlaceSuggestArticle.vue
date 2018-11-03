<template>
  <div class="Article">
    <p>Own an blog? Want to it published on munch? <a href="https://partner.munch.app" target="_blank">partner.munch.app</a></p>

    <div>
      <div class="ArticleItem flex" v-for="article in articles" :key="article.articleId">
        <div>
          <image-sizes v-if="article.thumbnail" :sizes="article.thumbnail.sizes"/>
        </div>
        <div>
          <h5>{{article.title}}</h5>
          <h6>by {{article.domain.name}}</h6>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceSuggestArticle"
  }
</script>

<style scoped lang="less">

  .Article {
    .ArticleItem {
      height: 60px;
      width: 80px;
    }
  }
</style>
