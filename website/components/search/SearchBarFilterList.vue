<template>
  <div class="Container" v-if="selected">
    <div class="FilterListContainer IndexElevation">
      <div class="FilterList IndexElevation">
        <div class="Group Price" :class="{'Selected': selected === 'price'}">
          <h2 class="FilterName">Price</h2>
          <search-bar-filter-price/>
        </div>

        <div class="Group Cuisine" :class="{'Selected': selected === 'cuisine'}">
          <h2 class="FilterName">Cuisine</h2>
          <search-bar-filter-tag type="cuisine"/>
        </div>

        <div class="Group Location" :class="{'Selected': selected === 'location'}">
          <h2 class="FilterName">Location</h2>
          <search-bar-filter-location/>
        </div>

        <div class="Group Amenities" :class="{'Selected': selected === 'amenities'}">
          <h2 class="FilterName">Amenities</h2>
          <search-bar-filter-tag type="amenities"/>
        </div>

        <div class="Group Establishments" :class="{'Selected': selected === 'establishments'}">
          <h2 class="FilterName">Establishments</h2>
          <search-bar-filter-tag type="establishments"/>
        </div>

        <div class="Group Timing" :class="{'Selected': selected === 'timings'}">
          <h2 class="FilterName">Timing</h2>
          <search-bar-filter-timing/>
        </div>
      </div>

      <div class="BottomBar IndexElevation">
        <div class="Button Cancel" @click="onClear">Clear</div>
        <div class="Button Apply" @click="onApply" v-if="applyText"
             :class="{'Secondary500Bg White Weight400': result, 'Secondary050Bg BlackA85 Weight600': !result}">
          {{applyText}}
        </div>
        <beat-loader v-else class="Button Apply Secondary050Bg flex-center" color="#084E69" size="8px"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import SearchBarFilterTag from "./SearchBarFilterTag";
  import SearchBarFilterTiming from "./SearchBarFilterTiming";
  import SearchBarFilterLocation from "./SearchBarFilterLocation";
  import SearchBarFilterPrice from "./SearchBarFilterPrice";
  import BeatLoader from 'vue-spinner/src/BeatLoader.vue'

  export default {
    name: "SearchBarFilterList",
    components: {SearchBarFilterPrice, SearchBarFilterLocation, SearchBarFilterTiming, SearchBarFilterTag, BeatLoader},
    mounted() {
      // LatLng need to be commited before mounting
      this.$store.dispatch('filter/start')
    },
    computed: {
      ...mapGetters('filter', ['count', 'selected']),
      applyText() {
        if (this.$store.state.filter.loading) return

        const count = this.$store.state.filter.result.count
        if (count) {
          if (count >= 100) return `See 100+ Restaurants`
          else if (count <= 10) return `See ${count} Restaurants`
          else {
            const rounded = Math.round(count / 10) * 10
            return `See ${rounded}+ Restaurants`
          }
        }
        return 'No Results'
      },
      result() {
        if (this.$store.state.filter.loading) {
          return true
        }

        return !!this.count;

      },
    },
    methods: {
      onClear() {
        const query = this.$store.state.filter.query
        const tags = SearchBarFilterTag.$$reduce(query, this.selected)
        this.$store.dispatch('filter/clear', {tags})
      },
      onApply() {
        if (this.count && this.count > 0) {
          this.$store.commit('filter/selected', null)
          this.$store.commit('layout/elevationOff', 'filter')
          this.$store.dispatch('search/start', this.$store.state.filter.query)
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .FilterListContainer {
    background: white;
  }

  .FilterName {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    padding-top: 24px;
    margin-bottom: 8px;
  }

  .BottomBar {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;

    .Button {
      line-height: 1.5;
      border-radius: 3px;
      font-size: 16px;

      &:hover {
        cursor: pointer;
      }
    }

    .Apply {
      font-size: 15px;
    }
  }

  @media (max-width: 767.98px) {
    .FilterListContainer {
    }

    .FilterList {
      background: white;
      position: fixed;
      /*padding-bottom: 24px;*/

      top: 0;
      left: 0;
      right: 0;
      bottom: 0;

      padding: 104px 15px 112px 15px;

      overflow-x: visible;
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;

      @media (min-width: 576px) {
        max-width: 540px;
        margin-right: auto;
        margin-left: auto;
      }
    }

    .BottomBar {
      height: 64px;
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;

      background: white;
      box-shadow: 0 -1px 2px 0 rgba(0, 0, 0, 0.2), 0 -1px 3px 0 rgba(0, 0, 0, 0.1);

      .Cancel {
        display: none;
      }

      .Apply {
        height: 48px;
        line-height: 48px;
        text-align: center;

        margin: 8px 15px;
        flex-grow: 1;
      }
    }
  }

  @media (min-width: 768px) {
    .FilterListContainer {
      position: fixed;

      box-shadow: 0 6px 6px 0 rgba(0, 0, 0, 0.26), 0 10px 20px 0 rgba(0, 0, 0, 0.19);
      padding: 16px 24px;
      border-radius: 4px;

      background: white;
      margin-top: 8px;
    }

    .FilterName {
      display: none;
    }

    .Group {
      display: none;
    }

    .Group.Selected {
      display: block;
    }

    .BottomBar {
      .Cancel {
        padding: 6px 24px;
      }

      .Apply {
        text-align: center;
        padding: 6px 0;
        width: 200px;
        margin-left: 8px;
      }
    }
  }
</style>