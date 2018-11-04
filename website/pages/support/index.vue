<template>
  <div class="container prismic-content">
    <div v-html="title"></div>
    <div v-html="content"></div>
  </div>
</template>

<script>
  export default {
    asyncData({Prismic}) {
      return Prismic.getSingle('support')
        .then((document) => {
          return {
            title: Prismic.asHtml(document.data.title),
            content: Prismic.asHtml(document.data.content)
          }
        })
    }
  }
</script>

<style lang="less" scoped>
  .prismic-content {
    margin-top: 24px;
    padding-bottom: 24px;
  }
</style>
