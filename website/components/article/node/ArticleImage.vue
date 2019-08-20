<template>
  <figure>
    <cdn-img :image="image" type="1080x1080"/>
    <figcaption v-if="caption">{{caption}}</figcaption>
  </figure>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";

  export default {
    name: "ArticleImage",
    components: {CdnImg},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      image: {
        get() {
          return this.node.attrs.image
        },
        set(image) {
          this.updateAttrs({image})
        },
      },
      caption: {
        get() {
          return this.node.attrs.caption
        },
        set(caption) {
          this.updateAttrs({caption})
        },
      }
    }
  }
</script>
