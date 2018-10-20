<script>
  export default {
    asyncData({redirect}) {
      return redirect({status: 301, path: '/feed/images'})
    }
  }
</script>
