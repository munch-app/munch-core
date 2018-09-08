<template>
  <div class="Container Support">
    <b-row>
      <b-col class="Content" cols="12" md="9" v-html="content"/>
      <b-col cols="12" md="3">
        <hr class="d-md-none d-block">
        <h4>Support articles</h4>
        <ul>
          <li>
            <nuxt-link to="/support/privacy-policy">Privacy Policy</nuxt-link>
          </li>
          <li>
            <nuxt-link to="/support/terms-of-use">Terms of Use</nuxt-link>
          </li>
        </ul>
      </b-col>
    </b-row>
  </div>
</template>

<script>
  export default {
    asyncData({Prismic}) {
      return Prismic.getSingle('terms_of_use')
        .then((document) => {
          return {content: Prismic.asHtml(document.data.content)}
        })
    }
  }
</script>

<style lang="less" scoped>
  .Support {
    margin-top: 24px;
    margin-bottom: 24px;
  }
</style>
