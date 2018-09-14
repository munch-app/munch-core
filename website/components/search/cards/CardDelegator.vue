<template>
  <search-place-card v-if="isCardId('basic_Place_20171211')" :place="card.place"/>
  <div v-else style="display: none"></div>
</template>

<script>
  import SearchPlaceCard from "./SearchPlaceCard";
  export default {
    name: "CardDelegator",
    components: {SearchPlaceCard},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    methods: {
      isCardId(cardId) {
        return this.card['_cardId'] === cardId
      }
    }
  }
</script>

<style scoped lang="less">

</style>
