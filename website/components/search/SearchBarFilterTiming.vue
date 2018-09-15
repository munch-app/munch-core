<template>
  <div class="TimingCollection">
    <div class="TimingCell" v-for="timing in timings" :key="timing" @click="toggle(timing)"
         :class="{
         'Primary500Bg White': isSelectedTiming(timing),
         'Whisper100Bg BlackA75': !isSelectedTiming(timing)}">
      {{timing}}
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarFilterTiming",
    data() {
      return {
        timings: ['Open Now', 'Breakfast', 'Lunch', 'Dinner', 'Supper']
      }
    },
    computed: {
      ...mapGetters('filter', ['isSelectedTiming'])
    },
    methods: {
      toggle(timing) {
        this.$store.dispatch('filter/timings', timing)
      }
    }
  }
</script>

<style scoped lang="less">
  .TimingCollection {
    padding: 8px 0;
    display: flex;
    flex-flow: row nowrap;
    margin-left: -16px;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;
  }

  .TimingCell {
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;

    line-height: 1.5;
    border-radius: 3px;
    padding: 6px 20px;
    margin-left: 16px;

    &:hover {
      cursor: pointer;
    }
  }
</style>
