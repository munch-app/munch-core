<template>
  <b-container>
    <google-map/>
    <b-row class="justify-content-md-center">
      <p class="partner-s-content">PARTNER'S CONTENT</p>
    </b-row>
    <b-row>
      <b-col>
        <p class="title">ARTICLES</p>
      </b-col>
    </b-row>
    <b-row>
      <article-row :articles="data.articles"></article-row>
    </b-row>
    <b-row>
      <b-col>
        <p class="title">INSTAGRAM</p>
      </b-col>
    </b-row>
    <b-row>
      <image-row :images="data.place.images"></image-row>
    </b-row>

  </b-container>
</template>

<script>
  import ArticleRow from '~/components/ArticleRow.vue';
  import ImageRow from '~/components/ImageRow.vue';
  import GoogleMap from '~/components/GoogleMap.vue'

  export default {
    components: {
      ArticleRow,
      ImageRow,
      GoogleMap,
    },

    asyncData({$axios, params}) {
      return $axios.get('/api/places/' + params.placeId)
        .then((res) => {
          return {data: res.data.data};
        });
    }
  }


</script>


<style lang="less">

</style>
