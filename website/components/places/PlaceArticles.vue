<template>
  <div class="m--12">
    <div class="ArticleList flex overflow-hidden">
      <div class="flex-self-stretch p-12" v-for="article in articles" :key="article.id">
        <article-card type="mini" :article="article"/>
      </div>
    </div>
  </div>
</template>

<script>
  import ArticleCard from "../article/ArticleCard";

  export default {
    name: "PlaceArticles",
    components: {ArticleCard},
    props: {
      articles: {
        type: Array,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ArticleList {
    > div {
      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 576px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 33.33333%;
        max-width: 33.33333%;
      }
    }
  }
</style>
