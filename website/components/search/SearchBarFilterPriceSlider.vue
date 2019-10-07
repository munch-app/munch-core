<template>
  <div>
    <vue-slider ref="slider" @input="onInput" @click.native="onClick" v-bind="{
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
        backgroundColor: '#faf0f0',
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
  </div>
</template>

<script>
  let components = {}
  if (process.client) {
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
      onClick() {
        this.$nextTick(() => {
          this.$emit('drag-end', this.value[0], this.value[1])
        })
      },
      refresh() {
        this.$refs.slider.refresh()
      }
    }
  }
</script>

<style scoped lang="less">

</style>
