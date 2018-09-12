<template>
  <div class="PriceView">
    <div class="PriceButtonList">
      <div class="PriceButton" v-for="range in ranges" :key="range.name" @click="toggle(range)"
           :class="{Primary400Bg: isSelectedPrice(range), Peach100Bg: !isSelectedPrice(range)}">
        {{range.name}}
      </div>
    </div>
    <div class="PriceGraph" v-if="priceGraph">
      <search-bar-filter-price-graph class="Graph" :price-graph="priceGraph"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import SearchBarFilterPriceGraph from "./SearchBarFilterPriceGraph";

  export default {
    name: "SearchBarFilterPrice",
    components: {SearchBarFilterPriceGraph},
    data() {
      return {
        ranges: [
          {name: '$'}, {name: '$$'}, {name: '$$$'}
        ]
      }
    },
    computed: {
      ...mapGetters('searchBar', ['isSelectedPrice']),
      priceGraph() {
        return this.$store.state.searchBar.priceGraph
      }
    },
    methods: {
      toggle(range) {
        this.$store.commit('searchBar/togglePrice', range)
      }
    }
  }
</script>

<style scoped lang="less">
  .PriceView {

  }

  .PriceButtonList {
    display: flex;
    flex-flow: row nowrap;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    margin-left: -8px;
    margin-right: -8px;

    .PriceButton {
      font-size: 13px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.85);

      text-align: center;
      white-space: nowrap;

      border-radius: 3px;
      line-height: 28px;
      height: 28px;
      width: 72px;

      margin-left: 8px;
      margin-right: 8px;

      &.Primary400Bg {
        color: white;
      }

      &.Peach100Bg {
        color: rgba(0, 0, 0, 0.75);
      }

      &:hover {
        cursor: pointer;
      }
    }
  }

  .PriceGraph {
    margin-top: 16px;

    .Graph {
      height: 100px;

      @media (min-width: 768px) {
        height: 165px;
        width: 400px;
      }
    }
  }
</style>
