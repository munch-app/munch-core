<template>
  <div class="PlaceTagList">
    <div class="text Tag bg-whisper100 border-3" v-for="tag in tagMax" :key="tag.tagId" @click="onClick(tag)">
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
    },
    methods: {
      onClick(tag) {
        this.$store.commit('filter/putTag', tag.name)
        this.$store.dispatch('search/start')

        this.$track.search(`Search - PlaceTagList`, this.$store.getters['search/locationType'])
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
      color: rgba(0, 0, 0, 0.8);
      font-size: 13px;
      font-weight: 600;
      padding: 5px 12px;
      margin-right: 12px;
      margin-bottom: 8px;
    }
  }
</style>
