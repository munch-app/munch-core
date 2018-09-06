<template>
  <div class="container Support">
    <b-row>
      <b-col class="Content" cols="12" md="9" v-html="content"/>
      <b-col cols="12" md="3">
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
    asyncData({Prismic, PrismicDOM}) {
      return Prismic.getSingle('privacy_policy')
        .then((document) => {
          return {content: PrismicDOM.RichText.asHtml(document.data.content)}
        })
    }
  }
</script>

<style lang="less" scoped>
  .Support {
    margin-top: 32px;
    margin-bottom: 32px;
  }
</style>
