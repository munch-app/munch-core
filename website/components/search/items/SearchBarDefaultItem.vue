<template>
  <div class="Default flex-align-center">
    <simple-svg class="wh-24px mr-16" fill="rgba(0,0,0,.8)" :filepath="icon"/>
    <div class="text weight-600">
      <div>{{name}}</div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchBarDefaultItem",
    props: {
      name: {
        type: String,
        required: true
      }
    },
    computed: {
      icon() {
        switch (this.name) {
          case 'Feed':
            return require('~/assets/icon/search/feed.svg')
          case 'Nearby':
            return require('~/assets/icon/search/nearby.svg')
          case 'Anywhere':
            return require('~/assets/icon/search/anywhere.svg')
          case 'EatBetween':
            return require('~/assets/icon/search/between.svg')
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Default {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    padding-top: 6px;
    padding-bottom: 6px;
  }
</style>
