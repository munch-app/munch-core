<template>
  <div>
    <div class="container-768 pt-48 pb-64">
      <article-content :article="article"/>

      <div class="pb-128">

      </div>

      <div class="flex hr-top mt-24 pt-32">
        <div class="flex-no-shrink wh-80px border-circle overflow-hidden">
          <cdn-img v-if="article.profile.image" :image="article.profile.image" type="320x320"/>
          <div v-else class="w-100 bg-blue"/>
        </div>

        <div class="ml-24 flex-shrink">
          <div class="b-a60 small">WRITTEN BY</div>
          <nuxt-link class="text-decoration-none" :to="`/@${article.profile.username}`">
            <h3 class="mt-4">{{article.profile.name}}</h3>
          </nuxt-link>
          <div class="regular mt-8 b-a60">{{article.profile.bio}}</div>
        </div>
      </div>
    </div>

    <div class="bg-steam" v-if="moreFromAuthorArticles.length > 0">
      <div class="container pt-24 pb-48">
        <h4>More from {{article.profile.name}}</h4>

        <!--  TODO Wrapping -->
        <div>
          <div class="pt-24 flex-1-2-3 MoreFromAuthorList">
            <div v-for="article in moreFromAuthorArticles" :key="article.id" class="p-12">
              <article-card-medium :article="article"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ArticleContent from "./ArticleContent";
  import CdnImg from "../utils/image/CdnImg";
  import ArticleCardMedium from "./ArticleCardMedium";

  export default {
    name: "ArticlePage",
    components: {ArticleCardMedium, CdnImg, ArticleContent},
    props: {
      article: {
        type: Object,
        required: true
      },
      more: Object
    },
    computed: {
      moreFromAuthorArticles() {
        return this.more?.author?.articles
      }
    }
  }
</script>

<style scoped lang="less">
  .MoreFromAuthorList {
    margin: -12px;
  }
</style>
