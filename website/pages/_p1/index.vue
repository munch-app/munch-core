<template>
  <profile-page v-if="type ==='Profile'" :profile="profile"/>

  <div class="container pt-24 pb-64" v-else>
    <h1>Page Not Found</h1>

    <div class="mt-16">
      <nuxt-link to="/">Home Page</nuxt-link>
    </div>
  </div>
</template>

<script>
  import ProfilePage from "../../components/profile/ProfilePage";

  export default {
    components: {ProfilePage},
    validate({params: {p1}, query}) {
      // /@username pages
      if (/^@[a-z0-9]{3,32}$/.test(_.toLower(p1))) {
        return true
      }
      // L13 Id pages
      if (/^(?:[0-9a-z-]{0,1000}-)?[0123456789abcdefghjkmnpqrstvwxyz]{13}$/.test(p1)) {
        return true
      }

      return false
    },
    head() {
      switch (this.type) {
        case "Profile":
          const {name, username, bio, image} = this.profile
          return this.$head({
            robots: {follow: true, index: true},
            canonical: `https://www.munch.app/@${username}`,
            graph: {
              title: `${name} · Munch`,
              description: bio,
              type: 'profile',
              image: image,
              url: `https://www.munch.app/@${username}`,
            },
            breadcrumbs: [
              {
                name: name,
                item: `https://www.munch.app/@${username}`
              },
            ]
          })

        case "Place":
        case "Article":
        case "Brand":
        case "Tag":
        case "Publication":
      }
    },
    asyncData({$api, params: {p1}, query}) {
      // Profile
      if (/^@[a-z0-9]{3,32}$/.test(_.toLower(p1))) {
        const username = p1.replace('@', '')
        return $api.get(`/profiles/${username}`)
          .then(({data: profile}) => {
            return $api.get(`/profiles/${username}/articles`, {params: {size: 10}})
              .then(({data: articles, cursor}) => {
                profile.articles = articles
                profile.articles.cursor = cursor
                return {type: 'Profile', profile}
              })
          })
      }

      const [, id, postfix] =
        /^(?:[0-9a-z-]{0,1000}-)?([0123456789abcdefghjkmnpqrstvwxyz]{12}([0123456789abcdefghjkmnpqrstvwxyz]))$/.exec(p1)

      switch (postfix) {
        // Place
        case '0':
          return $api.get(`/places/${id}`)
            .then(({data: place}) => {
              return {type: 'Place', place: place}
            })

        // Article
        case '1':
          return $api.get(`/articles/${id}`)
            .then(({data: {id, slug, profile: {username}}}) => {
              return this.$router.replace({path: `/@${username}/${slug}-${id}`})
            })

        // Location
        case '2':
          return {type: 'Location'}

        // Brand
        case '3':
          return {type: 'Brand'}

        // Tag
        case '4':
          return {type: 'Tag'}

        // Publication
        case '5':
          return $api.get(`/publications/${id}`)
            .then(({data: publication}) => {
              return {type: 'Publication', publication: publication}
            })
      }

      return {type: null}
    }
  }
</script>
