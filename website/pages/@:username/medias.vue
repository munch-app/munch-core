<script>
  import index from './index'

  export default {
    extends: index,
    head() {
      const {name, username, bio, image} = this.profile
      return this.$head({
        robots: {follow: true, index: true},
        title: `${name} - Media · Munch`,
        description: bio,
        image: image,
        url: `https://www.munch.app/@${username}/medias`,
        canonical: `https://www.munch.app/@${username}/medias`,
        breadcrumbs: [
          {
            name: name,
            item: `https://www.munch.app/@${username}`
          },
          {
            name: 'Media',
            item: `https://www.munch.app/@${username}/medias`
          },
        ]
      })
    },
  }
</script>
