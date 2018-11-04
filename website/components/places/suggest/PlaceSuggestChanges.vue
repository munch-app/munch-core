<template>
  <div class="Changes" v-if="hasChanges">
    <h2>Edits</h2>
    <div class="List">
      <div v-for="(value, key) in changes" :key="key">
        <p>{{key}}</p>
        <p class="text-ellipsis-1l">{{value}}</p>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceSuggestChanges",
    props: {
      changes: {
        type: Object
      }
    },
    data() {
      return {hasChanges: false}
    },
    watch: {
      changes: {
        handler(val){
          this.hasChanges = Object.keys(val).length > 0
        },
        deep: true
      },
    }
  }
</script>

<style scoped lang="less">
  .Changes {
    margin-top: 48px;

    .List {
      margin-top: 24px;
      margin-bottom: 24px;
    }
  }
</style>
