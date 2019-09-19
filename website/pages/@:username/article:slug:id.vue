<template>
  <div>
    <div class="container pt-48 pb-64">
      <div class="flex-wrap">
        <article-content class="ArticleContent" :article="article" ref="ArticleContent"/>
        <aside class="flex-grow">
          <article-context-map v-if="showMap" :article="article" :get-contexts="getContexts"/>
          <div class="mt-24">
            <Adsense data-ad-client="ca-pub-7144155418390858" data-ad-slot="9676262046"/>
          </div>
        </aside>
      </div>

      <div class="mt-64">
        <div class="flex-wrap m--6">
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
          <div v-else class="wh-100 bg-blue"/>
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

    <div class="container mtb-24">
      <Adsense data-ad-client="ca-pub-7144155418390858" data-ad-slot="7065221202"/>
    </div>

    <div class="bg-steam" v-if="moreFromAuthorArticles.length > 0">
      <div class="container pt-24 pb-48">
        <h4>More from {{article.profile.name}}</h4>

        <div class="mt-24">
          <div class="flex-1-2-3 m--12">
            <div v-for="article in moreFromAuthorArticles" :key="article.id" class="flex-self-stretch p-12">
              <article-card type="mini" :article="article"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ArticleContent from "../../components/article/ArticleContent";
  import ArticleContextMap from "../../components/article/ArticleContextMap";
  import CdnImg from "../../components/utils/image/CdnImg";
  import ArticleCard from "../../components/article/ArticleCard";

  export default {
    components: {ArticleCard, CdnImg, ArticleContextMap, ArticleContent},
    head() {
      const {
        profile: {name: profileName, username},
        image, title, description, slug, id, updatedAt, publishedAt
      } = this.article
      return this.$head({
        robots: {follow: true, index: true},
        url: `https://www.munch.app/@${username}/${slug}-${id}`,
        canonical: `https://www.munch.app/@${username}/${slug}-${id}`,
        type: 'article',
        title: `${title || 'Untitled Article'} - ${profileName} · Munch`,
        description: description,
        image: image,
        schemaType: 'Article',
        headline: title,
        updatedAt: updatedAt,
        publishedAt: publishedAt,
        authorName: profileName,
        breadcrumbs: [
          {
            name: profileName,
            item: `https://www.munch.app/@${username}`
          },
          {
            name: title,
            item: `https://www.munch.app/@${username}/${slug}-${id}`
          },
        ]
      })
    },
    asyncData({$api, params: {username, id}, query: {uid}}) {
      const url = uid ? `/articles/${id}/revisions/${uid}` : `/articles/${id}`
      return Promise.all([
        $api.get(url)
          .then(({data: article}) => article),
        $api.get(`/profiles/${username}/articles`, {params: {size: 5}})
          .then(({data: articles}) => articles)
      ]).then(([article, articles]) => {
        return {
          article, more: {
            author: {articles: articles.filter(a => a.id !== article.id)}
          }
        }
      })
    },
    computed: {
      moreFromAuthorArticles() {
        return this.more?.author?.articles?.slice(0, 3) || []
      },
      showMap() {
        return this.article.options.map && this.article.content.some(s => s.type === 'place')
      }
    },
    mounted() {
      const {profile: {username}, slug, id} = this.article
      window.history.replaceState({}, document.title, `/@${username}/${slug}-${id}`)
    },
    methods: {
      getContexts() {
        return this.$refs['ArticleContent'].getContexts()
      }
    }
  }
</script>

<style scoped lang="less">
  .ArticleContent {
    flex: 0 0 100%;

    @media (min-width: 816px) {
      flex: 0 0 768px;
      max-width: 768px;
    }
  }

  aside {
    @media (max-width: 1088px) {
      margin-top: 48px;
      max-width: 768px;
    }

    // Content Width + Padding + Aside Margin Left
    @media (min-width: 1088px) {
      flex: 0 0 calc(100vw - 48px - 768px - 32px);
      margin-left: 32px;

      position: sticky;
      top: calc(72px + 24px);
      height: 100%;
    }

    @media (min-width: 1200px) {
      flex: 0 0 calc(100vw - 160px - 768px - 48px);
      margin-left: 48px;
    }

    @media (min-width: 1400px) {
      flex: 0 0 424px;
    }
  }
</style>
