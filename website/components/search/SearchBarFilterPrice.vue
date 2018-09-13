<template>
  <div class="PriceView">
    <div class="PriceButtonList">
      <div class="PriceButton HoverPointer" v-for="name in ['$','$$','$$$']" :key="name" @click="toggle(name)" :class="{
           'Primary500Bg White': isSelectedPrice(name),
           'Peach200Bg BlackA75': !isSelectedPrice(name)}">
        {{name}}
      </div>
    </div>
    <div class="PriceGraph" v-if="priceGraph">
      <search-bar-filter-price-graph class="Graph" :price-graph="priceGraph"/>
      <search-bar-filter-price-slider class="Slider" @drag-end="onDragEnd" v-model="value" :min="min" :max="max"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import SearchBarFilterPriceGraph from "./SearchBarFilterPriceGraph";
  import SearchBarFilterPriceSlider from "./SearchBarFilterPriceSlider";

  export default {
    name: "SearchBarFilterPrice",
    components: {SearchBarFilterPriceSlider, SearchBarFilterPriceGraph},
    data() {
      const price = this.$store.state.searchBar.query.filter.price || {}
      const priceGraph = this.$store.state.searchBar.priceGraph || {}

      return {
        value: [price.min || 0, price.max || 200],
        min: priceGraph.min || 0,
        max: priceGraph.max || 200
      }
    },
    computed: {
      ...mapGetters('searchBar', ['isSelectedPrice']),
      priceGraph() {
        return this.$store.state.searchBar.priceGraph
      },
      price() {
        return this.$store.state.searchBar.query.filter.price
      }
    },
    watch: {
      priceGraph(priceGraph) {
        this.value = [priceGraph.min, priceGraph.max]
        this.min = priceGraph.min
        this.max = priceGraph.max
      },
      price(price) {
        this.value = [price.min || this.min, price.max || this.max]
      }
    },
    methods: {
      toggle(name) {
        const priceGraph = this.$store.state.searchBar.priceGraph
        if (priceGraph && name) {
          const range = priceGraph.ranges[name]
          this.value = [range.min, range.max]
          this.$store.dispatch('searchBar/price', {name, min: range.min, max: range.max})
        }
      },
      onDragEnd() {
        this.$store.dispatch('searchBar/price', {min: this.min, max: this.max})
      }
    },
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

      text-align: center;
      white-space: nowrap;

      border-radius: 3px;
      line-height: 28px;
      height: 28px;
      width: 72px;

      margin-left: 8px;
      margin-right: 8px;

      &:hover {
        cursor: pointer;
      }
    }
  }

  .PriceGraph {
    margin-top: 16px;

    .Graph {
      height: 100px;
      margin-right: 6px;

      @media (min-width: 768px) {
        height: 165px;
        width: 400px;
      }
    }

    .Slider {
      margin-top: -6px;
      margin-left: -6px;
      margin-right: 6px;
    }
  }
</style>
