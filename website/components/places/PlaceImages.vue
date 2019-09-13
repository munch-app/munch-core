<template>
  <div class="m--8">
    <div class="ImageList flex overflow-hidden">
      <div class="p-8" v-for="image in images" :key="image.uid">
        <div class="aspect r-1-1 border-3 overflow-hidden flex-no-shrink">
          <cdn-img :image="image" type="640x640"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";
  export default {
    name: "PlaceImages",
    components: {CdnImg},
    props: {
      images: {
        type: Array,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageList {
    > div {
      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 576px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 33.33333%;
        max-width: 33.33333%;
      }

      @media (min-width: 992px) {
        flex: 0 0 25%;
        max-width: 25%;
      }
    }
  }
</style>
