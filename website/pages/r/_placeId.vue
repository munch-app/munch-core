<template>
  <div>
    {{place}}
  </div>
</template>

<script>
  import axios from 'axios';

  export default {
    asyncData({params}) {
      return axios.get('https://api.munch.app/v0.12.0/places/' + params.placeId)
        .then((res) => {
          let data = res.data.data;
          return {
            place: data.place,
            articles: data.articles,
            instagram: data.instagram,
            menus: data.menus,
            awards: data.awards,
          }
        });
    }
  }
</script>

<style lang="less">


</style>
