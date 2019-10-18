<template>
  <div class="hr-bot pb-32">
    <div class="mtb-24">
      <h1>{{publication.name}}</h1>
    </div>

    <div class="Content flex-wrap">
      <section class="Main">
        <div class="flex-1-2">
          <div class="Article" v-for="article in mainArticles" :key="article.id">
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

      <section class="Side">
        <div>
          <div class="Article" v-for="article in sideArticles" :key="article.id">
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
  .Content {
    margin: -12px;
  }

  .Main {
    flex: 0 0 65%;
    max-width: 65%;
    padding: 12px 24px 12px 12px;

    > div {
      margin: -12px;
    }

    .Article {
      padding: 12px;
    }
  }

  .Side {
    flex: 0 0 35%;
    max-width: 35%;
    padding: 12px;

    > div {
      margin: -8px;
    }

    .Article {
      padding: 8px;
    }
  }

  @media (max-width: 767.98px) {
    .Main {
      flex: 0 0 calc(100% + 24px);
      max-width: calc(100% + 24px);
      padding: 12px;
    }

    .Side {
      display: none;
    }
  }
</style>
