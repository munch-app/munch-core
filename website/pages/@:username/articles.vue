<script>
  import index from './index'

  export default {
    extends: index,
    head() {
      const {name, username, bio, image} = this.profile
      return this.$head({
        robots: {follow: true, index: true},
        title: `${name} - Articles · Munch`,
        description: bio,
        image: image,
        url: `https://www.munch.app/@${username}/articles`,
        canonical: `https://www.munch.app/@${username}/articles`,
        breadcrumbs: [
          {
            name: name,
            item: `https://www.munch.app/@${username}`
          },
          {
            name: 'Articles',
            item: `https://www.munch.app/@${username}/articles`
          },
        ]
      })
    },
  }
</script>
