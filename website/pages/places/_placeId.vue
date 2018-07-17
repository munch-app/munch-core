<template>
  <div>
    <section class="Info">
      <b-container>
        <b-row>
          <b-col cols="5">
            <h2>{{data.place.name}}</h2>
          </b-col>
          <b-col cols="7">
            <p>Image</p>
          </b-col>
        </b-row>
      </b-container>
    </section>
    <section class="Detail">
      <b-container>
        <b-row>
          <b-col>
            <h4>About</h4>
            <p>{{data.place.description}}</p>
          </b-col>
          <b-col>
            <h4>Menu</h4>
          </b-col>
        </b-row>
      </b-container>
    </section>
    <section class="Map"></section>
    <h3 class="text-center">PARTNER’S CONTENT</h3>
    <section class="Article">
      <b-container>
        <h4>Articles</h4>
      </b-container>
    </section>
    <section class="Instagram">
      <b-container>
        <h4>Instagram</h4>
      </b-container>
    </section>
  </div>
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


<style lang="less" scoped>

</style>
