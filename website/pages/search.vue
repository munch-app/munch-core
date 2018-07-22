<template>
  <div class="Results">
    <div class="SuggestPlaces">

    </div>
    <div class="ResultPlaces">

    </div>
  </div>
</template>

<script>
  export default {
    head() {
      return {
        title: 'query' + ' | Munch',
      }
    },
    asyncData({$axios, params}) {
      return $axios.$get('/api/places/' + params.placeId)
        .then(({data}) => {
          return {data: data};
        });
    },
    computed: {
      suggestPlaces() {
        return []
      },

      resultPlaces() {
        return []
      }
    }
  }
</script>

<style scoped lang="less">
  .Results {

  }
</style>
