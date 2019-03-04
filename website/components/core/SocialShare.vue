<template>
  <div class="flex">
    <a :href="twitterUrl" target="_blank">
      <simple-svg class="wh-32px flex-no-shrink mr-16" fill="#000"
                  :filepath="require('~/assets/icon/Social_Share_Twitter.svg')"/>
    </a>
    <a :href="facebookUrl" target="_blank">
      <simple-svg class="wh-32px flex-no-shrink" fill="#000"
                  :filepath="require('~/assets/icon/Social_Share_Facebook.svg')"/>
    </a>
  </div>
</template>

<script>
  export default {
    name: "SocialShare",
    props: {
      url: {
        type: String,
        required: true
      }
    },
    computed: {
      facebookUrl() {
        return `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(this.url)}`
      },
      twitterUrl() {
        return `https://twitter.com/intent/tweet?text=${encodeURIComponent(this.url)}`
      }
    }
  }
</script>

<style scoped lang="less">

</style>
