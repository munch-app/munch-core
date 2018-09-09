<template>
  <div class="PlaceTagList">
    <div class="Text Tag TagBg Border24" v-for="tag in tagMax" :key="tag.tagId">
      {{tag.name}}
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceTagList",
    props: {
      tags: {
        required: true,
        type: Array
      },
      max: {
        required: false,
        type: Number,
        default: () => 5
      },
    },
    computed: {
      tagMax() {
        return this.tags.slice(0, this.max)
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceTagList {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: -8px;

    .Tag {
      font-size: 14px;
      font-weight: 600;
      padding: 5px 9px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
