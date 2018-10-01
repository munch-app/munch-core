<template>
  <div class="no-select index-5" v-on-clickaway="onClickAway">
    <div class="FilterBar index-header hr-bottom">
      <div class="Buttons container">
        <div v-for="button in buttons" :key="button.type" class="FilterButton" @click="onButton(button)"
             :class="{
             'primary-500-bg white': button.type === selected,
             'whisper-100-bg black-a-75': button.type !== selected && !button.applied,
             'primary-050-bg black': button.type !== selected && button.applied,
             'Combined': button.type === 'combined'
             }">
          <div>{{button.name}}</div>
          <div class="BulletDivider" v-if="button.count">•</div>
          <div v-if="button.count">{{button.count}}</div>
        </div>

        <div class="FilterButton elevation-hover-2 elevation-1 white-bg ClearAllButton" @click="onClearAll"
             v-if="isClearAll">
          <div>Clear All</div>
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
        const price = query.filter.price && query.filter.price.min !== undefined && query.filter.price
        const cuisine = SearchBarFilterTag.$$reduce(query, 'cuisine')
        const amenities = SearchBarFilterTag.$$reduce(query, 'amenities')
        const establishments = SearchBarFilterTag.$$reduce(query, 'establishments')

        const filters =
          (price ? 1 : 0) +
          cuisine.length +
          establishments.length +
          amenities.length +
          (query.filter.hour && query.filter.hour.name ? 1 : 0)

        const locationName = () => {
          const type = query.filter.location && query.filter.location.type
          if (type === 'Where') {
            return query.filter.location.areas.map(value => {
              return value.name
            }).join(', ')
          }
          return type || 'Location'
        }

        return [
          {
            type: 'location',
            name: locationName(),
            applied: query.filter.location && query.filter.location.type !== 'Anywhere'
          },
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
      },
      isClearAll() {
        const query = this.$store.state.filter.query
        const price = query.filter.price && query.filter.price.min !== undefined && query.filter.price

        const filters =
          (price ? 1 : 0) +
          (query.filter.tag.positives.length) +
          (query.filter.hour && query.filter.hour.name ? 1 : 0)

        return filters > 0
      }
    },
    methods: {
      onButton(button) {
        if (this.$store.state.filter.selected !== button.type) {
          this.$store.commit('filter/selected', button.type)
          this.$store.commit('focus', 'Filter')
          this.$store.dispatch('filter/start')
        } else {
          this.$store.commit('filter/selected', null)
          this.$store.commit('unfocus', 'Filter')
        }
      },
      onClickAway() {
        if (this.selected) {
          this.$store.commit('filter/selected', null)
          this.$store.commit('unfocus', 'Filter')
        }
      },
      onClearAll() {
        this.$store.commit('unfocus', 'Filter')
        this.$store.dispatch('filter/reset')
        this.$store.dispatch('search/start', this.$store.state.filter.query)
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

    /*box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05), 0 1px 1px rgba(0, 0, 0, 0.10);*/
  }

  .Buttons {
    height: 100%;
    display: flex;
    align-items: center;

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

    .ClearAllButton {
      margin-left: auto;
      margin-right: 0;

      display: flex;
    }
  }
</style>
