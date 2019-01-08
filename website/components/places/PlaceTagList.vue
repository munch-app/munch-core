<template>
  <div class="PlaceTagList flex-wrap" v-if="filteredTag.length > 0">
    <div class="text Tag bg-whisper100 border-3 hover-pointer" v-for="tag in filteredTag" :key="tag.tagId"
         @click="onClick(tag)">
      {{tag.name}}
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'

  export default {
    name: "PlaceTagList",
    props: {
      tags: {
        required: true,
        type: Array
      },
    },
    computed: {
      filteredTag() {
        const tags = _.filter(this.tags, tag => tag.type !== 'timing')
        return tags.slice(0, 10)
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
    color: rgba(0, 0, 0, 0.75);
    font-size: 12px;
    font-weight: 600;

    padding: 5px 10px;

    margin-right: 10px;
    margin-bottom: 10px;
  }
</style>
