<template>
  <div class="container pt-48 pb-64">
    <div class="bg-pink border-3 white mb-48">
      <div class="p-8-12 small">
        <b>NOTICE:</b> You are currently managing this profile through your admin escalation.
      </div>
    </div>

    <div class="flex-align-center flex-justify-between">
      <h1>{{profile.name}} Articles</h1>
      <nuxt-link :to="`/system/profiles/${profile.uid}/articles/new`">
        <button class="blue-outline">New article</button>
      </nuxt-link>
    </div>

    <div>
      <div class="hr-bot mt-32 text-decoration-none flex-row">
        <nuxt-link :to="`/system/profiles/${profile.uid}/articles/drafts`" class="mr-24 ptb-8 black hr-bot Selected">
          Drafts
        </nuxt-link>
        <nuxt-link :to="`/system/profiles/${profile.uid}/articles/published`" class="b-a40 ptb-8">
          Published
        </nuxt-link>
      </div>
    </div>

    <div>
      <div v-if="articles.length > 0">
        <nuxt-link :to="`/system/profiles/${profile.uid}/articles/${article.id}`" v-for="article in articles" :key="article.id"
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
          <p>{{profile.name}} have no drafts.</p>
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
    asyncData({$api, params: {uid}}) {
      return $api.get(`/admin/profiles/${uid}`)
        .then(({data: profile}) => {
          return $api.get(`/admin/profiles/${uid}/articles`, {params: {status: 'DRAFT', size: 20}})
            .then(({data: articles, cursor}) => {
              return {
                profile,
                articles,
                next: cursor?.next
              }
            })
        })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onLoadMore() {
        this.$api.get(`/admin/profiles/${this.profile.uid}/articles`, {params: {status: 'DRAFT', size: 20, cursor: this.next}})
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
