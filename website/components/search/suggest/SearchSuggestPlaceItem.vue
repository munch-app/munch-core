<template>
  <div class="Place Text">
    <span class="Name">{{item.place.name}}</span>
    <span class="Location">, {{item.place.location.neighbourhood}}</span>
  </div>
</template>

<script>
  export default {
    name: "SearchSuggestPlaceItem",
    props: {
      item: {
        type: Object,
        required: true
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

    span.Name {
      font-weight: 600;
    }

    span.Location {

    }
  }
</style>
