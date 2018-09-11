<template>
  <div class="TagColumn">
    <div class="TagRow" v-for="timing in timings" :key="timing"
         :class="isSelected(timing) ? 'Primary400Bg': 'Whisper100Bg'"
         @click="toggle(timing)">
      {{timing}}
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchBarFilterTiming",
    data() {
      return {}
    },
    computed: {
      timings: () => ['Open Now', 'Breakfast', 'Lunch', 'Dinner', 'Supper']
    },
    methods: {
      isSelected(timing) {
        return this.$store.getters['searchBar/isSelectedTiming'](timing)
      },
      toggle(timing) {
        this.$store.commit('searchBar/toggleTiming', timing)
      }
    }
  }
</script>

<style scoped lang="less">
  .TagColumn {
    padding: 8px 0;
    display: flex;
    flex-flow: row nowrap;
    margin-left: -16px;

    overflow-x: scroll;
  }

  .TagRow {
    font-size: 15px;
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
