<template>
  <div class="SearchFilterBar NoSelect" v-on-clickaway="onClickAway">
    <div class="FilterBar Elevation1">
      <div class="Buttons Container">
        <div v-for="button in buttons" :key="button" class="FilterButton"
             :class="button.toLowerCase() === selected ? 'Primary400Bg': 'Whisper100Bg'" @click="onButton(button)">
          {{button}}
        </div>

        <div class="Combined TagBg FilterButton" @click="onButton('Price')">
          Filters
        </div>
      </div>
    </div>

    <search-bar-filter-list :selected="selected" @selected="onButton"/>
  </div>
</template>

<script>
  import SearchBarFilterList from "./SearchBarFilterList";

  export default {
    name: "SearchBarFilter",
    components: {SearchBarFilterList},
    data() {
      return {
        buttons: ['Price', 'Cuisine', 'Location', 'Amenities', 'Establishments', 'Timing'],
        selected: null
      }
    },
    methods: {
      onButton(name) {
        name = name.toLowerCase()
        if (this.selected !== name) {
          this.selected = name
        } else {
          this.selected = null
        }
      },
      onClickAway() {
        this.selected = null
      }
    }
  }
</script>

<style scoped lang="less">
  .FilterBar {
    position: fixed;
    top: 56px;
    height: 48px;
    width: 100%;
    z-index: 50;
    background: white;
  }

  .Buttons {
    height: 100%;
    display: flex;
    align-items: center;

    .FilterButton {
      font-size: 13px;
      font-weight: 600;
      line-height: 1;

      border-radius: 3px;
      padding: 7px 12px;
      margin-right: 16px;

      &.Primary400Bg {
        color: white;
      }

      &.Whisper100Bg {
        color: rgba(0, 0, 0, 0.75);
      }

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
