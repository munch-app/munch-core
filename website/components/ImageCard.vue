<template>
  <div>
    <div v-if="image">
      <img class="InstagramImageCard" v-bind:src='image.sizes[0].url' width="270px" height="270px">
    </div>
  </div>
</template>
<script>
    export default {
      props: ["image"],
      name: "ImageCard"
    }
</script>

<style>

  .InstagramImageCard {
    background-color: #D8D8D8;
    box-shadow: 0 3px 6px 0 rgba(0,0,0,0.23), 0 3px 6px 0 rgba(0,0,0,0.17);
    z-index: -100;
  }
</style>
