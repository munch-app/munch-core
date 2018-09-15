<template>
  <search-place-card class="NoSelect Initial Pointer" v-if="isCardId('basic_Place_20171211')" :place="card.place"/>
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
  .Initial {
    color: initial;
    text-decoration: initial;
  }

  .Pointer:hover {
    cursor: pointer;
  }
</style>
