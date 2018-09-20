<template>
  <div class="PriceView">
    <div class="PriceButtonList">
      <div class="PriceButton hover-pointer" v-for="name in ['$','$$','$$$']" :key="name" @click="toggle(name)" :class="{
           'primary-500-bg White': isSelectedPrice(name),
           'peach-200-bg black-a-75': !isSelectedPrice(name)}">
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
      const price = this.$store.state.filter.query.filter.price || {}
      const priceGraph = this.$store.state.filter.result.priceGraph || {}

      return {
        value: [price.min || priceGraph.min || 0, price.max || priceGraph.max || 200],
        min: priceGraph.min || 0,
        max: priceGraph.max || 200
      }
    },
    computed: {
      ...mapGetters('filter', ['isSelectedPrice']),
      price() {
        return this.$store.state.filter.query.filter.price
      },
      priceGraph() {
        return this.$store.state.filter.result.priceGraph
      }
    },
    watch: {
      priceGraph(priceGraph) {
        this.min = priceGraph.min
        this.max = priceGraph.max
        this.value = [priceGraph.min, priceGraph.max]

        if (this.price && this.price.name) {
          const range = priceGraph.ranges[this.price.name]
          this.value = [range.min, range.max]
          this.$store.dispatch('filter/price', {name, min: range.min, max: range.max})
        }
      },
      price(price) {
        this.value = [price.min || this.min, price.max || this.max]
      }
    },
    methods: {
      toggle(name) {
        if (this.priceGraph && name) {
          const range = this.priceGraph.ranges[name]
          this.value = [range.min, range.max]
          this.$store.dispatch('filter/price', {name, min: range.min, max: range.max})
        }
      },
      onDragEnd() {
        this.$store.dispatch('filter/price', {min: this.min, max: this.max})
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
