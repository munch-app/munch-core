<template>
  <figure>
    <cdn-img class="Image" :class="{Editing: editing}" :image="image" type="1080x1080" :alt="caption"/>
    <template v-if="editing">
      <input class="mt-4 w-100 small b-a75 text-center" placeholder="image caption" v-model="caption"/>
    </template>
    <template v-else>
      <figcaption class="mt-4 w-100 small b-a75 text-center" v-if="caption">{{caption}}</figcaption>
    </template>
  </figure>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";

  export default {
    name: "ArticleImage",
    components: {CdnImg},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      editing() {
        return !!this.updateAttrs
      },
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

<style scoped lang="less">
  input, figcaption {
    outline: none;
    border: none;
    background: none;
    padding: 0;
  }

  .Image.Editing {
    &:hover {
      outline: 3px solid #07F;
    }
  }
</style>
