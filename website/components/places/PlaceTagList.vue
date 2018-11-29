<template>
  <div class="PlaceTagList flex-wrap">
    <div class="text Tag bg-whisper100 border-3 hover-pointer" v-for="tag in tagMax" :key="tag.tagId" @click="onClick(tag)">
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
        this.$store.commit('filter/putTag', tag)
        this.$store.dispatch('search/start')

        this.$track.search(`Search - PlaceTagList`, this.$store.getters['search/locationType'])
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceTagList {
    margin-bottom: -10px;
  }

  .Tag {
    color: rgba(0, 0, 0, 0.8);
    font-size: 13px;
    font-weight: 600;

    padding: 6px 13px;

    margin-right: 10px;
    margin-bottom: 10px;
  }
</style>
