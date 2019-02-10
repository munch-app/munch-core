<template>
  <div class="ContentImage mtb-24">
    <image-sizes :sizes="image.sizes" width="1000" height="1000" object-fit="contain"/>
  </div>
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "CreatorEditorImage",
    components: {ImageSizes},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      image: {
        get() {
          return this.node.attrs.image
        },
        set(image) {
          this.updateAttrs({
            image,
          })
        },
      },
    }

  }
</script>
