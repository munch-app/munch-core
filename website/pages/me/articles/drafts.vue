<template>
  <div class="container pt-64 pb-64">
    <div class="flex-align-center flex-justify-between">
      <h1>Your articles</h1>
      <nuxt-link to="/me/articles/new">
        <button class="blue-outline">New article</button>
      </nuxt-link>
    </div>

    <div>
      <div class="hr-bot mt-32 text-decoration-none flex-row">
        <nuxt-link to="/me/articles/drafts" class="mr-24 ptb-8 black hr-bot Selected">
          Drafts
        </nuxt-link>
        <nuxt-link to="/me/articles/published" class="b-a40 ptb-8">
          Published
        </nuxt-link>
      </div>
    </div>

    <div>
      <div v-if="articles.length > 0">
        <nuxt-link :to="`/me/articles/${article.id}`" v-for="article in articles" :key="article.id"
                   class="p-24-0 hr-bot block text-decoration-none">
          <h4 class="text-ellipsis-1l">{{article.title || 'Untitled Article'}}</h4>
          <div class="">
            <div class="b-a50 text-ellipsis-1l mb-8" v-if="article.description">{{article.description}}</div>
            <div class="b-a50 small">Last edited on {{formatMillis(article.updatedAt)}}</div>
          </div>
        </nuxt-link>
      </div>

      <div v-else>
        <div class="ptb-48 flex-center">
          <p>You have no drafts.</p>
        </div>
      </div>
    </div>

    <div class="flex-center ptb-32" v-if="next">
      <button class="blue-outline" @click="onLoadMore">Load more</button>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'

  export default {
    head() {
      return this.$head({
        graph: {
          title: `Your articles`,
        },
      })
    },
    asyncData({$api}) {
      return $api.get('/me/articles', {params: {status: 'DRAFT', size: 20}})
        .then(({data: articles, cursor}) => {
          return {
            articles,
            next: cursor?.next
          }
        })
        .catch(error => {
          this.$store.dispatch('addError', error)
        })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onLoadMore() {
        this.$api.get('/me/articles', {params: {status: 'DRAFT', size: 20, cursor: this.next}})
          .then(({data: articles, cursor}) => {
            this.articles.push(...articles)
            this.next = cursor?.next
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .Selected {
    border-bottom: 1px solid #000;
  }
</style>
