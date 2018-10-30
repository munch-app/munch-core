<script>
  import index from './index'
  export default {
    extends: index,
    head() {
      const {title, name, description, keywords} = this.$store.state.search.seo
      console.log(title)

      const meta = []
      meta.push({name: 'robots', content: 'follow,index'})
      if (description) meta.push({hid: 'description', name: 'description', content: description})
      if (keywords) meta.push({name: 'keywords', content: keywords})
      return {title, meta}
    },
    asyncData() {

    }
  }
</script>
