<template>
  <article-page v-if="type === 'Article'" :article="article" :more="more"/>
</template>

<script>
  import ArticlePage from "../../components/article/ArticlePage";

  export default {
    components: {ArticlePage},
    validate({params: {p1, p2}}) {
      // /@username
      if (/^@[a-z0-9]{3,32}$/.test(_.toLower(p1))) {
        // /@username/L13-ID
        if (/^(?:[0-9a-z-]{0,1000}-)?[0123456789abcdefghjkmnpqrstvwxyz]{13}$/.test(p2)) {
          return true
        }
      }
    },
    head() {
      switch (this.type) {
        case "Article":
          const {profile: {name, username}, image, title, description, slug, id} = this.article
          return this.$head({
            robots: {follow: true, index: true},
            graph: {
              title: `${title} - ${name} · Munch`,
              description: description,
              type: 'article',
              image: image,
              url: `https://www.munch.app/@${username}/${slug}-${id}`,
            },
            breadcrumbs: [
              {
                name: name,
                item: `https://www.munch.app/@${username}`
              },
              {
                name: title,
                item: `https://www.munch.app/@${username}/${slug}-${id}`
              },
            ]
          })
      }
    },
    asyncData({$api, params: {p2}, query: {revision}}) {
      const [, id] =
        /^(?:[0-9a-z-]{0,1000}-)?([0123456789abcdefghjkmnpqrstvwxyz]{13})$/.exec(p2)

      if (revision) {
        return $api.get(`/articles/${id}/revisions/${revision}`)
          .then(({data: article}) => {
            return {article, type: 'Article'}
          })
      }

      return $api.get(`/articles/${id}`)
        .then(({data: article}) => {
          const profile = article.profile
          return $api.get(`/profiles/${profile.username}/articles`, {params: {size: 8}})
            .then(({data: articles}) => {
              return {
                type: 'Article', article, more: {
                  author: {articles: articles.filter(a => a.id !== article.id)}
                }
              }
            })

        })
    }
  }
</script>
