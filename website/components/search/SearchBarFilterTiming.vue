<template>
  <div class="TimingCollectionContainer">
    <div class="TimingCollection flex">
      <div class="TimingCell hover-pointer" v-for="tag in timings" :key="tag.tagId" @click="toggle(tag)"
           :class="{
              'bg-p500 white': isSelected(tag),
              'bg-whisper100 b-a75': !isSelected(tag)}">
        {{tag.name}}
      </div>
      <div class="TimingCellRight"></div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HorizontalScrollView from "../core/HorizontalScrollView";

  export default {
    name: "SearchBarFilterTiming",
    components: {HorizontalScrollView},
    data() {
      return {
        timings: [
          {name: 'Open Now'},
          {name: 'Breakfast', type: 'Timing', tagId: 'f749ab1a-358c-4ba2-adb8-04a3accf46cb'},
          {name: 'Lunch', type: 'Timing', tagId: '1be094a8-b9f5-43ca-9af7-f0ae2d87afb2'},
          {name: 'Dinner', type: 'Timing', tagId: '32d11ac3-afb2-4e1e-a798-97771958294c'},
          {name: 'Supper', type: 'Amenities', tagId: '97c3121f-7947-4950-8a63-027ef1d6337a'},
        ]
      }
    },
    computed: {
      ...mapGetters('filter', ['isSelectedTiming', 'isSelectedTag'])
    },
    methods: {
      toggle(tag) {
        if (tag.tagId) {
          this.$store.dispatch('filter/tag', tag)
        } else {
          this.$store.dispatch('filter/timings', 'OpenNow')
        }
      },
      isSelected(tag) {
        if (tag.tagId) {
          return this.isSelectedTag(tag)
        } else {
          return this.isSelectedTiming('OpenNow')
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .TimingCollectionContainer {
    margin-left: -24px;
    margin-right: -24px;
  }

  .TimingCollection {
    padding: 8px 24px;
    margin-left: -16px;

    overflow-x: auto;
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
  }

  .TimingCellRight {
    padding-left: 24px;
    min-height: 1px;
  }
</style>
