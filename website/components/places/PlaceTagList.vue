<template>
  <div class="PlaceTagList">
    <div class="text Tag whisper-100-bg border-3" v-for="tag in tagMax" :key="tag.tagId" @click="onClick(tag)">
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
        this.$store.dispatch('filter/start')
        this.$store.dispatch('search/start', this.$store.state.filter.query)
        if (this.$route.name !== 'search') this.$router.push({path: '/search'})
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
      padding: 5px 11px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
