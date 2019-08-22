<template>
  <div class="hr-bot pb-32">
    <div class="mb-24">
      <h1>Featured</h1>
    </div>

    <div class="Content flex-wrap">
      <div class="Main">
        <nuxt-link class="text-decoration-none"
                   :to="`/@${mainArticle.profile.username}/${mainArticle.slug}-${mainArticle.id}`">
          <div class="aspect r-5-2 border-2 overflow-hidden">
            <cdn-img :image="mainArticle.image" type="1080x1080">
              <div class="hover-bg-a20"/>
            </cdn-img>
          </div>

          <h2 class="mt-16 mb-8">{{mainArticle.title}}</h2>
          <p class="text-ellipsis-3l">{{mainArticle.description}}</p>
        </nuxt-link>
      </div>
      <div class="Side1">
        <div class="Article" v-for="article in side1Articles" :key="article.id">
          <nuxt-link class="flex-row text-decoration-none"
                     :to="`/@${article.profile.username}/${article.slug}-${article.id}`">
            <div class="wh-80px border-2 overflow-hidden flex-no-shrink mr-16">
              <cdn-img :image="article.image" type="320x320"/>
            </div>
            <div>
              <h5 class="b-a80 text-ellipsis-1l">{{article.title}}</h5>
              <p class="b-a80 text-ellipsis-2l small">{{article.description}}</p>
            </div>
          </nuxt-link>
        </div>
      </div>
      <div class="Side2">
        <div class="Article" v-for="article in side2Articles" :key="article.id">
          <nuxt-link class="flex-row text-decoration-none"
                     :to="`/@${article.profile.username}/${article.slug}-${article.id}`">
            <div class="wh-80px border-2 overflow-hidden flex-no-shrink mr-16">
              <cdn-img :image="article.image" type="320x320"/>
            </div>

            <div>
              <h5 class="b-a80 text-ellipsis-1l">{{article.title}}</h5>
              <p class="b-a80 text-ellipsis-2l small">{{article.description}}</p>
            </div>
          </nuxt-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "HomeFeatured",
    components: {CdnImg},
    props: {
      node: {
        type: Object,
        required: true
      }
    },
    computed: {
      mainArticle() {
        return this.node.articles[0]
      },
      side1Articles() {
        return this.node.articles.slice(1, 4)
      },
      side2Articles() {
        return this.node.articles.slice(4, 7)
      },
    }
  }
</script>

<style scoped lang="less">
  .Content {
    margin: -12px;
  }

  .Main {
    flex: 0 0 40%;
    max-width: 40%;
    padding: 12px 24px 12px 12px;
  }

  .Side1, .Side2 {
    flex: 0 0 30%;
    max-width: 30%;
    padding: 12px;
    margin: -8px;

    .Article {
      padding: 8px;
    }
  }

  @media (max-width: 1000px) {
    .Main {
      flex: 0 0 55%;
      max-width: 55%;
    }

    .Side1 {
      flex: 0 0 45%;
      max-width: 45%;
    }

    .Side2 {
      display: none;
    }
  }

  @media (max-width: 600px) {
    .Main {
      flex: 0 0 100%;
      max-width: 100%;
      padding: 12px;
    }

    .Side1 {
      flex: 0 0 100%;
      max-width: 100%;
    }
  }
</style>
