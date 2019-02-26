<template>
  <div>
    <image-sizes class="border-4 overflow-hidden" :sizes="sizes" height="1000"/>
  </div>
</template>

<script>
  import ImageSizes from "../../components/core/ImageSizes";

  export default {
    name: "ContentImage",
    components: {ImageSizes},
    props: {
      item: {
        type: Object,
        required: true,
      }
    },
    computed: {
      sizes() {
        return this.item && this.item.body && this.item.body.image && this.item.body.image.sizes
      }
    }
  }
</script>
