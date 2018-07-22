<template>
  <div class="PlaceTags">
    <div class="Tag TagBg Border24" v-for="tag in tagMax" :key="tag.tagId">
      {{tag.name}}
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceTags",
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
  .PlaceTags {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: -8px;

    .Tag {
      font-size: 12px;
      padding: 8px 10px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
