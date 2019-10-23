<template>
  <div class="hr-bot pb-32">
    <div class="mtb-24">
      <h1>{{publication.name}}</h1>
    </div>

    <div class="m--12 flex-wrap">
      <section class="p-12 Main">
        <div class="m--12 flex-1-2">
          <div class="p-12 Article" v-for="article in mainArticles" :key="article.id">
            <nuxt-link class="text-decoration-none" :to="`/@${article.profile.username}/${article.slug}-${article.id}`">
              <div class="aspect r-5-3 border-2 overflow-hidden flex-no-shrink mr-16">
                <cdn-img :image="article.image" type="640x640">
                  <div class="hover-bg-a20"/>
                </cdn-img>
              </div>
              <div class="mt-16">
                <h4 class="text-ellipsis-1l">{{article.title}}</h4>
                <p class="mt-4 b-a80 text-ellipsis-2l">{{article.description}}</p>
              </div>
            </nuxt-link>
          </div>
        </div>
      </section>

      <section class="p-12 Side">
        <div class="m--8">
          <div class="p-8 Article" v-for="article in sideArticles" :key="article.id">
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
      </section>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "HomePublication",
    components: {CdnImg},
    props: {
      node: {
        type: Object,
        required: true
      }
    },
    computed: {
      publication() {
        return this.node.publication
      },
      articles() {
        return this.node.articles
      },
      mainArticles() {
        return this.node.articles.slice(0, 4)
      },
      sideArticles() {
        return this.node.articles.slice(4)
      },
    }
  }
</script>

<style scoped lang="less">
  .Main {
    flex: 0 0 65%;
    max-width: 65%;
    padding-right: 24px;
  }

  .Side {
    flex: 0 0 35%;
    max-width: 35%;
  }

  @media (max-width: 767.98px) {
    .Main {
      flex: 0 0 100%;
      max-width: 100%;

      padding-right: 12px;
    }

    .Side {
      display: none;
    }
  }
</style>
