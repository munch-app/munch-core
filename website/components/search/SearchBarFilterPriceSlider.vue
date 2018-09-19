<template>
  <div>
    <no-ssr>
      <vue-slider @input="onInput" @drag-end="onDragEnd" v-bind="{
      value, min, max,
      formatter: '${value}',
      mergeFormatter: '${value1} - ${value2}',
      height: 4,
      dotSize: 12,
      interval: 5,
      sliderStyle: {
        backgroundColor: '#F05F3B',
        width: '16px',
        height: '16px',
        top: '-6px'
      },
      bgStyle: {
        backgroundColor: '#EEDFDF',
      },
      tooltipStyle: {
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        lineHeight: 'normal',
        color: 'black',
        fontWeight: 600
      },
      processStyle: {
        backgroundColor: '#F05F3B'
      }
  }"/>
    </no-ssr>
  </div>
</template>

<script>
  let components = {}
  if (process.browser) {
    components['vue-slider'] = require('vue-slider-component')
  }

  export default {
    name: "SearchBarFilterPriceSlider",
    components: components,
    props: {
      value: {
        type: Array
      },
      min: {
        type: Number,
        required: true
      },
      max: {
        type: Number,
        required: true
      }
    },
    methods: {
      onInput(input) {
        this.$emit('input', input)
      },
      onDragEnd() {
        this.$emit('dragEnd')
      }
    }
  }
</script>

<style scoped lang="less">

</style>
