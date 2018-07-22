<template xmlns:v-stream="http://www.w3.org/1999/xhtml">
  <div class="SearchFilter">
    <section v-for="type in types" :key="type.key">
      <div class="Header">{{type.title}}</div>
      <div class="Content">
        <div class="Item Border24" :class="isSelected(type.key, item) ? 'Primary300Bg' : 'TagBg'"
             v-for="item in $data[type.key]"
             :key="item" @click="onSelect(type.key, item)" v-stream:click="click$">
          {{item}}
        </div>
      </div>
    </section>

    <div class="Action">
      <munch-button class="Button" text="RESET" variant="clear" size="small" @click="onReset"></munch-button>
      <munch-button class="Button" :text="'SEE '+(count || 0)+' RESTAURANTS'" variant="primary" size="small"
                    @click="onApply"></munch-button>
    </div>
  </div>
</template>

<script>
  import MunchButton from "../core/MunchButton";
  import {Subject} from 'rxjs'
  import {pluck, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchFilter",
    components: {MunchButton},
    data() {
      return {
        types: [
          // {title: 'Timing', key: 'timings'},
          {title: 'Cuisines', key: 'cuisines'},
          {title: 'Establishments', key: 'establishments'},
          {title: 'Amenities', key: 'amenities'},
        ],
        prices: ['$', '$$', '$$$'],
        timings: ['Open Now', 'Breakfast', 'Lunch', 'Dinner', 'Supper'],
        cuisines: ['Singaporean', 'Japanese', 'Italian', 'Thai', 'Chinese', 'Korean', 'Mexican', 'Mediterranean'],
        cuisineMores: ['Chinese', 'Singaporean', 'Western', 'Italian', 'Japanese', 'Indian', 'Cantonese', 'Thai', 'Korean', 'English', 'Fusion', 'Asian', 'Hainanese', 'American', 'French', 'Hong Kong', 'Teochew', 'Taiwanese', 'Malaysian', 'Mexican', 'Shanghainese', 'Indonesian', 'Vietnamese', 'European', 'Peranakan', 'Sze Chuan', 'Spanish', 'Middle Eastern', 'Modern European', 'Filipino', 'Turkish', 'Hakka', 'German', 'Mediterranean', 'Swiss', 'Hawaiian', 'Australian'],
        establishments: ['Hawker', 'Drinks', 'Bakery', 'Dessert', 'Snacks', 'Cafe', 'Bars & Pubs', 'Fast Food', 'BBQ', 'Buffet', 'Hotpot & Steamboat', 'High Tea', 'Fine Dining'],
        amenities: ['Romantic', 'Supper', 'Brunch', 'Business Meal', 'Scenic View', 'Child-Friendly', 'Large Group', 'Vegetarian Options', 'Halal', 'Healthy', 'Alcohol', 'Vegetarian', 'Private Dining', 'Budget', 'Pet-Friendly', 'Live Music', 'Vegan', 'Vegan Options'],
        selected: {
          timings: [],
          cuisines: [],
          establishments: [],
          amenities: [],
        }
      }
    },
    computed: {
      tags() {
        const tags = [];
        [].push.apply(tags, this.selected.cuisines);
        [].push.apply(tags, this.selected.establishments);
        [].push.apply(tags, this.selected.amenities);
        return tags
      },
      hour() {
        return {
          // TODO Hours
        }
      },
      searchQuery() {
        return {
          filter: {
            tag: {
              positives: this.tags
            },
            hours: this.hour
          },
          sort: {}
        }
      }
    },
    methods: {
      isSelected(key, item) {
        return this.selected[key].includes(item)
      },
      onSelect(key, item) {
        const index = this.selected[key].indexOf(item)
        if (index >= 0) {
          this.$delete(this.selected[key], index)
        } else {
          this.selected[key].push(item)
        }
      },
      onReset() {
        this.selected.timings = []
        this.selected.cuisines = []
        this.selected.establishments = []
        this.selected.amenities = []
      },
      onApply() {
        // TODO Forward to search.vue
      }
    },
    subscriptions() {
      this.click$ = new Subject()

      return {
        count: this.click$.pipe(
          debounceTime(500),
          switchMap(() => {
            return this.$axios.$post('/api/search/filter/count', this.searchQuery, {progress: false})
          }),
          map(({data}) => data.count)
        )
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchFilter {
    background: white;
    border-top: 1px solid #DDD;
    padding: 16px;

    section {
      .Header {
        font-size: 12px;
        font-weight: 500;
        text-transform: uppercase;
      }

      .Content {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        margin-bottom: 12px;
        margin-top: -4px;
        margin-left: -8px;

        .Item {
          margin-top: 8px;
          margin-left: 8px;
          padding: 4px 12px;

          font-size: 12px;

          &:hover {
            cursor: pointer;
          }

          &.TagBg {
            color: black;
          }

          &.Primary300Bg {
            color: white;
          }
        }
      }
    }

    .Action {
      display: flex;
      justify-content: flex-end;

      .Button {
        margin-left: 16px;
      }
    }
  }
</style>
