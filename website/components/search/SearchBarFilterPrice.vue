<template>
  <div class="PriceView">
    <div class="PriceButtonList">
      <!-- If click too quickly it will use the old '$' -->
      <div class="PriceButton hover-pointer" v-for="name in ['$','$$','$$$']" :key="name" @click="toggle(name)" :class="{
           'bg-p500 white': isSelectedPrice(name),
           'bg-peach100 b-a75': !isSelectedPrice(name)}">
        {{name}}
      </div>
    </div>

    <div class="Header">
      <h5>Price Per Pax</h5>
    </div>

    <div class="PriceGraph">
      <!-- Graph is disabled for now -->
      <search-bar-filter-price-graph v-if="false" class="Graph" :price-graph="priceGraph"/>
      <search-bar-filter-price-slider ref="slider" class="Slider" @drag-end="onDragEnd" v-model="value" :min="min" :max="max"/>
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
      return {
        value: [0, 200],
        min: 0,
        max: 200,
      }
    },
    computed: {
      ...mapGetters(['isStaging']),
      ...mapGetters('filter', ['isSelectedPrice', 'priceGraph', 'selected']),
    },
    mounted() {
      const price = this.$store.state.filter.query.filter.price || {min: 0, max: 200}
      const priceGraph = this.$store.state.filter.result.priceGraph || {min: 0, max: 200}

      this.value = [price.min || priceGraph.min, price.max || priceGraph.max]
      this.min = priceGraph.min
      this.max = priceGraph.max
    },
    watch: {
      priceGraph(priceGraph) {
        this.min = priceGraph.min
        this.max = priceGraph.max
      },
      selected(selected) {
        switch (selected) {
          case 'combined':
          case 'price':
            this.$refs.slider.refresh()
        }
      }
    },
    methods: {
      toggle(name) {
        const graph = this.$store.state.filter.result.priceGraph
        if (graph && name) {
          const range = graph.ranges[name]
          this.value = [range.min, range.max]
          this.$store.dispatch('filter/price', {min: range.min, max: range.max})
        }
      },
      onDragEnd(min, max) {
        this.$store.dispatch('filter/price', {min: min, max: max})
      }
    },
  }
</script>

<style scoped lang="less">
  .PriceView {
    .Header {
      margin-top: 24px;
      margin-bottom: 0;
    }
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
    margin-top: 48px;

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
