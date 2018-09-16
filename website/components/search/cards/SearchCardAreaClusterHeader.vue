<template>
  <div>
    <h2>{{area.name}}</h2>
  </div>
</template>

<script>
  import ImageSize from "../../core/ImageSize";

  export default {
    name: "SearchCardAreaClusterHeader",
    components: {ImageSize},
    props: {
      area: {
        type: Object,
        required: true
      }
    },
    computed: {
      image() {
        return this.area.images && this.area.images[0]
      }
    }
  }
</script>

<style scoped lang="less">
  .Image {
    width: 100%;
    height: 216px;
  }

  h2 {
  }
</style>
