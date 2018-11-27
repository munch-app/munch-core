<template>
  <div class="Place flex-align-center">
    <simple-svg class="wh-24px mr-16" fill="rgba(0,0,0,.7)" :filepath="require('~/assets/icon/search/place.svg')"/>
    <div class="text weight-600">
      <div>{{place.name}}</div>
      <div class="Location small">{{location}}</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchBarPlaceItem",
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
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }

  .text {
    line-height: 1.3;

    > div:first-child {
      font-size: 15px;
    }
  }
</style>
