<template>
  <div class="Place text">
    <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/search/place.svg"/>
    <span class="Name">{{place.name}}</span>
    <span class="Location BlackA85">, {{location}}</span>
  </div>
</template>

<script>
  export default {
    name: "SearchSuggestPlaceItem",
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      location() {
        return this.place.location.street
          || place.location.neighbourhood
      }
    }
  }
</script>

<style scoped lang="less">
  .Place {
    height: 26px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    .Icon {
      float: left;
      width: 24px;
      height: 24px;
      margin: 1px 15px 1px 0;
    }

    span.Name {
      font-weight: 600;
    }

    span.Location {
      font-size: 14px;
      font-weight: 400;
    }
  }
</style>
