<template>
  <div>
    <div class="container">
      <pre>{{media}}</pre>
    </div>
  </div>
</template>

<script>
  export default {
    head() {
      const {
        profile: {name: profileName, username},
        title, id, images, updatedAt, createdAt,
      } = this.media

      return this.$head({
        robots: {follow: true, index: true},
        url: `https://www.munch.app/@${username}/${id}`,
        canonical: `https://www.munch.app/@${username}/${id}`,
        title: `Media - ${profileName} · Munch`,
        image: images && images[0],
        updatedAt: updatedAt,
        publishedAt: createdAt,
        authorName: profileName,
        breadcrumbs: [
          {
            name: profileName,
            item: `https://www.munch.app/@${username}`
          },
          {
            name: title,
            item: `https://www.munch.app/@${username}/${id}`
          },
        ]
      })
    },
    asyncData({$api, error, params: {id}}) {
      return $api.get(`/medias/${id}`)
        .then(({data}) => {
          return {media: data}
        })
        .catch(err => {
          if (err.response.status === 404) {
            return error({statusCode: 404, message: 'Media Not Found'})
          }
          throw err
        })
    },
    mounted() {
      const {profile: {username}, id} = this.media
      this.$path.replace({path: `/@${username}/${id}`})
    },
  }
</script>

<style scoped lang="less">

</style>
