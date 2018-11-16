<template>
  <div class="container prismic-content">
    <article>
      <h1 v-if="title">{{title}}</h1>
      <div v-html="content"/>
    </article>

    <aside>
      <hr>
      <h4>Support Articles</h4>
      <ul>
        <li v-for="article in articles" :key="article.to">
          <nuxt-link :to="article.to">{{article.text}}</nuxt-link>
        </li>
      </ul>
    </aside>
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
    asyncData({$prismic, params, error}) {
      return $prismic.get('support_article', params.uid).then((document) => {
        if (document && document.data) {
          return {
            title: $prismic.asText(document.data.title),
            content: $prismic.asHtml(document.data.content),
            description: $prismic.asText(document.data.content).substring(0, 300)
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
    padding-bottom: 80px;

    display: flex;
    flex-wrap: wrap;
    flex-grow: 1;
    align-self: flex-start;
  }

  article {
    @media (min-width: 1200px) {
      flex: 0 0 75%;
      max-width: 75%;
    }
  }

  aside {
    width: 100%;

    hr {
      display: block;
    }

    @media (min-width: 1200px) {
      flex: 0 0 25%;
      max-width: 25%;
      padding: 0 0 0 48px;

      hr {
        display: none;
      }
    }
  }
</style>
