<template>
  <div>
    <h2>{{collection.name}}</h2>
    <p class="b-a85" v-if="collection.description">{{collection.description}}</p>

  </div>
</template>

<script>
  export default {
    name: "SearchCardCollectionHeader",
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    computed: {
      collection() {
        return this.card.collection
      }
    }
  }
</script>

<style scoped lang="less">

</style>
