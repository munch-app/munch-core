<template>
  <nav class="no-select" v-on-clickaway="onClickAway">
    <div class="FilterBar index-6 hr-bot bg-white w-100 fixed">
      <div class="Buttons h-100 flex-align-center container">
        <div v-for="button in buttons" :key="button.type" class="FilterButton hover-pointer" @click="onButton(button)"
             :class="{
             'bg-p500 white': button.type === selected,
             'bg-whisper100 b-a75': button.type !== selected && !button.applied,
             'bg-p050 black': button.type !== selected && button.applied,
             'Combined': button.type === 'combined',
             'Location': button.type === 'location',
             }">
          <div>{{button.name}}</div>
          <div class="BulletDivider" v-if="button.count">•</div>
          <div v-if="button.count">{{button.count}}</div>
        </div>

        <div class="FilterButton ClearAllButton hover-pointer elevation-hover-2 elevation-1 bg-white" @click="onClearAll"
             v-if="isClearAll">
          <div>Clear All</div>
        </div>
      </div>
    </div>

    <search-bar-filter-list @selected="onButton"/>
  </nav>
</template>

<script>
  import {mapGetters} from 'vuex'
  import SearchBarFilterList from "./SearchBarFilterList";
  import SearchBarFilterTag from "./SearchBarFilterTag";

  export default {
    name: "SearchBarFilter",
    components: {SearchBarFilterList},
    computed: {
      ...mapGetters('filter', ['selected', 'locationPoints']),
      buttons() {
        const query = this.$store.state.filter.query
        const price = query.filter.price && (query.filter.price.min && query.filter.price.max) && query.filter.price
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
          switch (type) {
            case 'Anywhere':
              return 'Anywhere'
            case 'Nearby':
              return 'Nearby'

            case 'Where':
              if (query.filter.location.areas.length > 0) {
                return query.filter.location.areas.map(value => {
                  return value.name
                }).join(', ')
              }
              return 'Location'
            case 'Between':
              const length = this.locationPoints.filter(bl => bl.name).length
              if (length > 1) return `Between ${length} Locations`
              return 'Location'

            default:
              return 'Location'
          }
        }

        return [
          {
            type: 'location',
            name: locationName(),
            applied: query.filter.location && query.filter.location.type !== 'Anywhere'
          },
          {
            type: 'price',
            name: price && (price.min || price.max) ? `$${price.min} - $${price.max}` : 'Price',
            applied: price && (price.min || price.max)
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
        this.$store.dispatch('search/start')

        this.$track.search(`Search - Reset`, this.$store.getters['search/locationType'])
      }
    }
  }
</script>

<style scoped lang="less">
  .FilterBar {
    top: 56px;
    height: 48px;
  }

  .BulletDivider {
    vertical-align: middle;
    font-size: 9px;
    margin: 0 4px;
  }

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

    div {
      line-height: 28px;
      height: 28px;

      white-space: nowrap;
      overflow: visible;
    }

    display: flex;

    @media (max-width: 767.98px) {
      display: none;
    }
  }

  .Combined,
  .Location,
  .ClearAllButton {
    display: flex;
  }

  .ClearAllButton {
    margin-left: auto;
    margin-right: 0;
  }

  .Combined {
    @media (min-width: 768px) {
      display: none;
    }
  }
</style>
