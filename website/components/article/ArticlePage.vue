<template>
  <div>
    <div class="container pt-48 pb-64">
      <div class="flex">
        <article-content :article="article" ref="ArticleContent"/>
        <article-context-map class="Map" :article="article" :get-contexts="getContexts"/>
      </div>

      <div class="mt-64">
        <div class="Tags flex-wrap">
          <div v-for="tag in article.tags" :key="tag.id" class="p-6">
            <div class="bg-steam p-6-12 border-3 b-a75">
              {{tag.name}}
            </div>
          </div>
        </div>
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
  import ArticleContextMap from "./ArticleContextMap";

  export default {
    name: "ArticlePage",
    components: {ArticleContextMap, ArticleCardMedium, CdnImg, ArticleContent},
    props: {
      article: {
        type: Object,
        required: true
      },
      more: Object
    },
    computed: {
      moreFromAuthorArticles() {
        return this.more?.author?.articles || 0
      }
    },
    methods: {
      getContexts() {
        return this.$refs['ArticleContent'].getContexts()
      }
    },
  }
</script>

<style scoped lang="less">
  .MoreFromAuthorList {
    margin: -12px;
  }

  .Tags {
    margin: -6px;
  }

  .container {
    @media (max-width: 1199.98px) {
      max-width: 768px;
    }
  }

  .Map {
    width: 100%;
    height: 320px;
    margin-left: 24px;

    @media (max-width: 1199.98px) {
      display: none;
    }

    @media (min-width: 1300px) {
      margin-left: 48px;
    }
  }
</style>
