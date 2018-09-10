<template>
  <div class="SearchFilterBar Elevation1 NoSelect">
    <div class="FilterBar">
      <div class="Buttons Container">
        <div v-for="button in buttons" :key="button" class="TagBg FilterButton" @click="onButton(button)">
          {{button}}
        </div>

        <div class="Combined TagBg FilterButton" @click="onButton('Combined')">
          Filters
        </div>
      </div>
    </div>

    <div class="FilterBarSpace"/>
  </div>
</template>

<script>
  export default {
    name: "SearchBarFilter",
    data() {
      return {
        buttons: ['Price', 'Cuisine', 'Location', 'Amenities', 'Establishments', 'Timing']
      }
    },
    methods: {
      onButton(name) {
        console.log(name)
      }
    }
  }
</script>

<style scoped lang="less">
  .FilterBar {
    position: fixed;
    height: 48px;
    width: 100%;
    background-color: white;
  }

  .FilterBarSpace {
    height: 48px;
  }

  .Buttons {
    height: 100%;
    display: flex;
    align-items: center;

    .FilterButton {
      color: rgba(0, 0, 0, 0.8);
      font-size: 13px;
      font-weight: 600;
      line-height: 1;

      border-radius: 3px;
      padding: 7px 12px;
      margin-right: 16px;

      &:hover {
        cursor: pointer;
      }

      display: none;
      @media (min-width: 768px) {
        display: block;
      }
    }

    .Combined {
      display: block;
      @media (min-width: 768px) {
        display: none;
      }
    }
  }
</style>
