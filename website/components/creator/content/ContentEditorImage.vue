<template>
  <div>
    <image-sizes class="border-4 overflow-hidden" :sizes="image.sizes" height="1000"/>
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
