<template>
  <div class="SearchFilterBar Elevation1 NoSelect">
    <div class="FilterBar">
      <div class="Buttons container">
        <div v-for="button in buttons" :key="button" class="TagBg Button Border3" @click="onButton(button)">
          {{button}}
        </div>

        <div class="Combined TagBg Button Border3" @click="onButton('Combined')">
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
    height: 64px;
    width: 100%;
    background-color: white;
  }

  .Buttons {
    height: 100%;
    display: flex;
    align-items: center;

    .Button {
      padding: 4px 12px;
      margin-right: 18px;

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

  .FilterBarSpace {
    height: 64px;
  }
</style>
