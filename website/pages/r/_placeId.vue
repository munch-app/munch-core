<template>
  <div>
    <div class="temp">

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
          <article-row v-bind:articles="data.articles"></article-row>
        </b-row>
        <b-row>
          <b-col>
            <p class="title">INSTAGRAM</p>
          </b-col>
        </b-row>
        <b-row>
          <image-row v-bind:images="data.place.images"></image-row>
        </b-row>

      </b-container>

    </div>
  </div>
</template>

<script>
  import axios from 'axios';
  import ArticleRow from '~/components/ArticleRow.vue';
  import ImageRow from '~/components/ImageRow.vue';
  import GoogleMap from '~/components/GoogleMap.vue'

  export default {
    components: {
      ArticleRow,
      ImageRow,
      GoogleMap,
    },

    asyncData({params}) {
      return axios.get('https://api.munch.app/v0.12.0/places/' + params.placeId)
        .then((res) => {
          return {data: res.data.data};
        });
    }
  }


</script>


<style lang="less">

  .justify-content-md-center {
    padding-top: 100px;
  }

  .partner-s-content {
    padding-top: 20px;
  }

  .temp {
    top: 100px;
  }

  .partner-s-content {
    color: #000000;
    font-family: "Open Sans";
    font-size: 32px;
    font-weight: 600;
  }

  .title {
    color: #000000;
    font-family: "Open Sans";
    font-size: 24px;
    font-weight: 600;
  }

</style>
