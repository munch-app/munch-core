<template>
  <figure>
    <cdn-img class="Image" :class="{Editing: editing}" :image="image" type="1080x1080" :alt="caption"/>
    <figcaption class="mt-8 flex-between small b-a75">
      <span class="opacity-0">Credit: @{{username}}</span>

      <template v-if="editing">
        <input class="small flex-grow text-center" placeholder="insert image caption" v-model="caption"/>
        <span>Credit: @{{username}}</span>
      </template>

      <template v-else>
        <span v-if="caption" class="flex-grow text-center">{{caption}}</span>
        <nuxt-link class="text-decoration-none b-a75" :to="`/@${username}`">Credit: @{{username}}</nuxt-link>
      </template>
    </figcaption>
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
      username() {
        return this.image?.profile?.username
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
      },
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
