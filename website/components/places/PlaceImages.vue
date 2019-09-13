<template>
  <div>
    <div v-for="image in images" :key="image.uid">
      <div class="aspect r-1-1 border-3 overflow-hidden flex-no-shrink">
        <cdn-img :image="image" type="640x640"/>
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

</style>
