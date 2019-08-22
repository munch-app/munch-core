<template>
  <div class="fixed position-0 bg-overlay flex-center index-dialog">
    <div>
      <div class="flex-justify-end">
        <div class="CloseButton absolute hover-pointer" @click="onClose">
          <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>

      <div class="Dialog dialog-xlarge flex-row wh-100" v-on-clickaway="onClose">
        <div class="flex-grow">
          <h2>Published Articles</h2>

          <div class="mtb-24">
            <div class="ArticleList flex-wrap overflow-y-auto">
              <div class="p-12" v-for="article in articles" :key="article.id" @click="onArticle(article)">
                <div class="aspect r-1-1 bg-steam hover-pointer border-3">
                  <div class="zero p-16">
                    <h4 class="m-0 text-ellipsis-2l">{{article.title}}</h4>
                    <p class="text-ellipsis-3l">{{article.description}}</p>
                    <h6 class="mt-8">Profile: {{article.profile.username}}</h6>
                  </div>
                </div>
              </div>
            </div>

            <div class="flex-center ptb-48" v-if="next">
              <button class="blue-outline" @click="onLoadMore">Load More</button>
            </div>

            <div class="flex-center ptb-48" v-if="!loaded">
              Loading...
            </div>

            <div class="flex-center ptb-48" v-if="loaded && articles.length === 0">
              <p>You don't have any articles available.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "SystemArticleSelectionDialog",
    components: {CdnImg},
    data() {
      return {
        articles: [],
        cursor: {},
        loaded: false,
      }
    },
    computed: {
      next() {
        return this.cursor?.next
      },
    },
    mounted() {
      this.reload()
    },
    methods: {
      reload() {
        this.loaded = false

        this.$api.get('/admin/articles', {params: {status: 'PUBLISHED', size: 25}})
          .then(({data: articles, cursor}) => {
            this.articles.splice(0)
            this.articles.push(...articles)
            this.cursor = cursor
            this.loaded = true
          })
      },
      onLoadMore() {
        this.$api.get('/admin/articles', {params: {status: 'PUBLISHED', size: 25, cursor: this.next}})
          .then(({data: articles, cursor}) => {
            this.articles.push(...articles)
            this.cursor = cursor
          })
      },
      onClose() {
        this.$emit('on-close')
      },
      onArticle(image) {
        this.$emit('on-article', image)
      },
    }
  }
</script>

<style scoped lang="less">
  .Dialog {
    height: 600px;
  }

  .CloseButton {
    margin-top: -48px;
  }

  .ArticleList {
    margin: -12px;

    > div {
      flex: 0 0 100%;
      max-width: 100%;
    }

    > div {
      @media (min-width: 1000px) {
        flex: 0 0 25%;
        max-width: 25%;
      }
    }
  }
</style>
