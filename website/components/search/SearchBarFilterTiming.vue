<template>
  <div class="TimingCollection">
    <div class="TimingCell" v-for="timing in timings" :key="timing" @click="toggle(timing)"
         :class="{Primary400Bg: isSelectedTiming(timing), Whisper100Bg: !isSelectedTiming(timing)}">
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
      ...mapGetters('searchBar', ['isSelectedTiming'])
    },
    methods: {
      toggle(timing) {
        this.$store.commit('searchBar/toggleTiming', timing)
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
  }

  .TimingCell {
    font-size: 13px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);
    white-space: nowrap;

    line-height: 1.5;
    border-radius: 3px;
    padding: 6px 20px;
    margin-left: 16px;

    &.Primary400Bg {
      color: white;
    }

    &.Whisper100Bg {
      color: rgba(0, 0, 0, 0.75);
    }

    &:hover {
      cursor: pointer;
    }
  }
</style>
