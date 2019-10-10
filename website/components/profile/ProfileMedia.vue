<template>
  <div>
    <div class="aspect r-1-1 border border-3 overflow-hidden">
      <cdn-img :image="media.images[0]">

      </cdn-img>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "ProfileMedia",
    components: {CdnImg},
    props: {
      media: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">

</style>
