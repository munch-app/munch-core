<template>
  <div class="container prismic-content">
    <b-row>
      <b-col cols="12" md="9">
        <h1 v-if="title">{{title}}</h1>
        <div v-html="content"/>
      </b-col>

      <b-col cols="12" md="3">
        <hr class="d-md-none d-block">
        <h4>Support Articles</h4>
        <ul>
          <li v-for="article in articles" :key="article.to">
            <nuxt-link :to="article.to">{{article.text}}</nuxt-link>
          </li>
        </ul>
      </b-col>
    </b-row>
  </div>
</template>

<script>
  export default {
    head() {
      const title = `${this.title || 'Support Articles'} · Munch`
      const meta = []
      meta.push({name: 'robots', content: 'follow,index'})
      meta.push({hid: 'description', name: 'description', content: this.description})
      return {title, meta}
    },
    data() {
      return {
        articles: [
          {to: '/support/privacy', text: 'Privacy Policy'},
          {to: '/support/terms', text: 'Terms of Use'},
        ]
      }
    },
    asyncData({Prismic, params, error}) {
      return Prismic.get('support_article', params.uid)
        .then((document) => {
          if (document && document.data) {
            return {
              title: Prismic.asText(document.data.title),
              content: Prismic.asHtml(document.data.content),
              description: Prismic.asText(document.data.content).substring(0, 300)
            }
          } else {
            error({statusCode: 404, message: 'Support Article Not Found'})
          }
        })
    }
  }
</script>

<style lang="less" scoped>
  .prismic-content {
    padding-bottom: 24px;
  }
</style>
