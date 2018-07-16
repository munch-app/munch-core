<template>
  <div class="ImageRow">
    <b-row>
      <b-col cols="12" md="3" v-for="index in 4" :key="index" class="d-flex align-items-stretch">
        <image-card v-bind:image="images[index-1]"></image-card>
      </b-col>
    </b-row>
  </div>
</template>

<script>
  import ImageCard from '~/components/ImageCard.vue'
  export default {
    props: ["images"],
    name: "ImageRow",
    components: {
      ImageCard
    }
  }
</script>

<style>
  .ImageRow{
    min-width:1000px;
  }
</style>
