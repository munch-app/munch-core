<template>
  <div>
    <div class="container">
      <div class="mt-48">
        <advert class="Article_Top"
                :google="{slot: 4700759194, format: 'auto', style: {display: 'inline-block', height: '120px', width: '100%'}}"
        />

        <div class="flex-wrap mt-48">
          <article-content class="ArticleContent" :article="article" ref="ArticleContent"/>
          <aside id="aside" class="flex-grow">
            <article-context-map v-if="showMap" :article="article" :get-contexts="getContexts"/>

            <advert class="Article_Aside mt-32"
                    :google="{slot: 9676262046, format: 'auto', style: {display: 'block', width: '100%'}}"
            />
          </aside>
        </div>
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
        <div class="flex-no-shrink wh-80px border-circle overflow-hidden flex-center bg-steam">
          <cdn-img v-if="article.profile.image" :image="article.profile.image" type="320x320"/>
          <simple-svg v-else class="wh-64px" fill="#ccc" :filepath="require('~/assets/icon/icons8-person.svg')"/>
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

    <div class="container mt-64 mb-24">
      <advert class="Article_Bottom"
              :google="{slot: 7065221202, format: 'auto'}"
      />
    </div>

    <profile-footer :profile="article.profile" :extra="extra"/>
  </div>
</template>

<script>
  import ArticleContent from "../../components/article/ArticleContent";
  import ArticleContextMap from "../../components/article/ArticleContextMap";
  import CdnImg from "../../components/utils/image/CdnImg";
  import Advert from "../../components/utils/ads/Advert";
  import ProfileFooter from "../../components/profile/ProfileFooter";

  function addAsideObserver() {
    let aside = document.getElementById('aside')
    if (aside) {
      const observer = new MutationObserver(() => {
        aside.style.height = ''
        aside.style.minHeight = ''
      })
      observer.observe(aside, {
        attributes: true,
        attributeFilter: ['style']
      })
    }
  }

  export default {
    components: {
      ProfileFooter, Advert, CdnImg, ArticleContextMap, ArticleContent
    },
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
    asyncData({$api, error, params: {username, id}, query: {uid}}) {
      if (uid) {
        return $api.get(`/articles/${id}/revisions/${uid}`)
          .then(({data: article}) => {
            return {article}
          })
      } else {
        return $api.get(`/articles/${id}`, {params: {fields: 'extra.profile.articles,extra.profile.medias'}})
          .then(({data: article, extra}) => {
            return {article, extra}
          })
          .catch(err => {
            if (err.response.status === 404) {
              return error({statusCode: 404, message: 'Article Not Found'})
            }
            throw err
          })
      }
    },
    computed: {
      showMap() {
        return this.article.options.map && this.article.content.some(s => s.type === 'place')
      }
    },
    mounted() {
      const {profile: {username}, slug, id} = this.article
      this.$path.replace({path: `/@${username}/${slug}-${id}`})
      addAsideObserver()
    },
    methods: {
      getContexts() {
        return this.$refs['ArticleContent'].getContexts()
      },
    }
  }
</script>

<style scoped lang="less">
  .ArticleContent {
    flex: 0 0 65%;
    max-width: 65%;
  }

  aside {
    flex: 0 0 35%;
    max-width: 35%;
  }

  @media (max-width: 992px) {
    .ArticleContent {
      flex: 0 0 100%;
      max-width: 100%;
    }

    aside {
      display: none;
    }
  }

  @media (min-width: 992px) {
    aside {
      padding-left: 32px;

      position: sticky;
      top: calc(72px + 24px);
      height: 100% !important;
    }
  }

  @media (min-width: 1200px) {
    aside {
      padding-left: 48px;
    }
  }
</style>
