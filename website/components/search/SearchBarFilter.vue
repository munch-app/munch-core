<template>
  <div class="SearchFilterBar no-select" v-on-clickaway="onClickAway">
    <div class="FilterBar Elevation1 Index5">
      <div class="Buttons Container">
        <div v-for="button in buttons" :key="button.type" class="FilterButton" @click="onButton(button)"
             :class="{
             'Primary500Bg White': button.type === selected,
             'Whisper100Bg BlackA75': button.type !== selected && !button.applied,
             'Primary050Bg BlackA95': button.type !== selected && button.applied,
             'Combined': button.type === 'combined'
             }">
          <div>{{button.name}}</div>
          <div class="BulletDivider" v-if="button.count">•</div>
          <div v-if="button.count">{{button.count}}</div>
        </div>
      </div>
    </div>

    <search-bar-filter-list @selected="onButton"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import SearchBarFilterList from "./SearchBarFilterList";
  import SearchBarFilterTag from "./SearchBarFilterTag";

  export default {
    name: "SearchBarFilter",
    components: {SearchBarFilterList},
    computed: {
      ...mapGetters('filter', ['selected']),
      buttons() {
        const query = this.$store.state.filter.query
        const user = this.$store.state.filter.user
        const price = query.filter.price && query.filter.price.min !== undefined && query.filter.price
        const cuisine = SearchBarFilterTag.$$reduce(query, 'cuisine')
        const amenities = SearchBarFilterTag.$$reduce(query, 'amenities')
        const establishments = SearchBarFilterTag.$$reduce(query, 'establishments')

        const filters =
          (price ? 1 : 0) +
          cuisine.length +
          establishments.length +
          amenities.length +
          (query.filter.area && query.filter.area.type !== 'City' ? 1 : 0) +
          (query.filter.hour && query.filter.hour.name ? 1 : 0)

        return [
          {
            type: 'price',
            name: price ? `$${price.min} - $${price.max}` : 'Price',
            applied: price
          },
          {
            type: 'cuisine',
            name: cuisine.length === 1 ? cuisine[0] : 'Cuisine',
            count: cuisine.length > 1 ? cuisine.length : undefined,
            applied: cuisine.length > 0
          },
          {
            type: 'location',
            name: query.filter.area && query.filter.area.type !== 'City' && query.filter.area.name || 'Location',
            applied: (query.filter.area == null && user.latLng) || (query.filter.area && query.filter.area.type !== 'City')
          },
          {
            type: 'amenities',
            name: amenities.length === 1 ? amenities[0] : 'Amenities',
            count: amenities.length > 1 ? amenities.length : undefined,
            applied: amenities.length > 0
          },
          {
            type: 'establishments',
            name: establishments.length === 1 ? establishments[0] : 'Establishments',
            count: establishments.length > 1 ? establishments.length : undefined,
            applied: establishments.length > 0
          },
          {
            type: 'timings',
            name: query.filter.hour && query.filter.hour.name || 'Timings',
            applied: query.filter.hour && query.filter.hour.name
          },
          {
            type: 'combined',
            name: 'Filters',
            count: filters,
            applied: filters > 0
          }
        ]
      }
    },
    methods: {
      onButton(button) {
        if (this.$store.state.filter.selected !== button.type) {
          this.$store.commit('filter/selected', button.type)
          this.$store.commit('layout/elevationOn', 'filter')
        } else {
          this.$store.commit('filter/selected', null)
          this.$store.commit('layout/elevationOff', 'filter')
        }
      },
      onClickAway() {
        if (this.selected) {
          this.$store.commit('filter/selected', null)
          this.$store.commit('layout/elevationOff', 'filter')
        }
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
    background: white;
  }

  .Buttons {
    height: 100%;
    display: flex;
    align-items: center;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    .FilterButton {
      font-size: 13px;
      font-weight: 600;
      line-height: 28px;

      border-radius: 3px;
      padding: 0 12px;
      height: 28px;
      margin-right: 16px;

      white-space: nowrap;
      overflow: visible;

      &:hover {
        cursor: pointer;
      }

      div {
        line-height: 28px;
        height: 28px;

        white-space: nowrap;
        overflow: visible;

        &.BulletDivider {
          vertical-align: middle;
          font-size: 9px;
          margin: 0 4px;
        }
      }

      display: none;
      @media (min-width: 768px) {
        display: flex;
      }
    }

    .Combined {
      display: flex;
      @media (min-width: 768px) {
        display: none;
      }
    }
  }
</style>