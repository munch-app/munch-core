<template>
  <div class="container">
    <div class="FilterListContainer bg-white index-elevation">
      <div ref="scrollable" class="FilterList index-elevation" :class="`Selected-${selected}`">
        <div class="Group Location" :class="{'Selected': selected === 'Location'}">
          <h2>Where</h2>
          <search-bar-filter-location/>
        </div>

        <div class="Group Price" :class="{'Selected': selected === 'Price'}">
          <h2>Price</h2>
          <search-bar-filter-price/>
        </div>

        <div class="Group Cuisine" :class="{'Selected': selected === 'Cuisine'}">
          <h2>Cuisine</h2>
          <div class="ScrollList">
            <search-bar-filter-tag type="Cuisine"/>
          </div>
        </div>

        <div class="Group Amenities" :class="{'Selected': selected === 'Amenities'}">
          <h2>Amenities</h2>
          <div class="ScrollList">
            <h4>Requirements</h4>
            <search-bar-filter-tag type="Requirement"/>

            <h4 class="mt-16">Amenities</h4>
            <search-bar-filter-tag type="Amenities"/>
          </div>
        </div>

        <div class="Group Establishments" :class="{'Selected': selected === 'Establishment'}">
          <h2>Establishments</h2>
          <div class="ScrollList">
            <search-bar-filter-tag type="Establishment"/>
          </div>
        </div>

        <div class="Group Timing" :class="{'Selected': selected === 'Timing'}">
          <h2>Timing</h2>
          <search-bar-filter-timing/>
        </div>
      </div>

      <div class="BottomBar mt-16 flex-end index-elevation">
        <button class="Cancel" @click="onClear">Clear</button>
        <button class="Apply secondary" @click="onApply" v-if="applyText"
                :class="{'bg-s500 white weight-600': isApplicable, 'bg-s050 b-a85 weight-600': !isApplicable}">
          {{applyText}}
        </button>
        <beat-loader v-else class="Apply border-3 bg-s050 flex-center" color="#084E69" size="8px"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import {disableBodyScroll, clearAllBodyScrollLocks} from 'body-scroll-lock';

  import SearchBarFilterTag from "./SearchBarFilterTag";
  import SearchBarFilterTiming from "./SearchBarFilterTiming";
  import SearchBarFilterLocation from "./SearchBarFilterLocation";
  import SearchBarFilterPrice from "./SearchBarFilterPrice";

  export default {
    name: "SearchBarFilterList",
    components: {SearchBarFilterPrice, SearchBarFilterLocation, SearchBarFilterTiming, SearchBarFilterTag},
    computed: {
      ...mapGetters('filter', ['count', 'selected', 'isSelectedLocationType', 'applyText', 'isApplicable']),
    },
    mounted() {
      disableBodyScroll(this.$refs.scrollable)
    },
    methods: {
      onClear() {
        this.$store.dispatch('filter/clear')
      },
      onApply() {
        if (this.isApplicable) {
          this.$store.commit('filter/selected', null)
          this.$store.commit('unfocus', 'Filter')
          this.$store.dispatch('search/start')

          this.$track.search(`Search - Filter`, this.$store.getters['search/locationType'])
        }
      },
    }
  }
</script>

<style scoped lang="less">
  h2 {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    padding-top: 24px;
    margin-bottom: 8px;
  }

  @media (max-width: 767.98px) {
    .FilterList {
      background: white;
      position: fixed;
      top: 104px;
      left: 0;
      right: 0;
      height: calc(100vh - 72px - 72px/*Header72px*/ - 48px);

      padding: 0 24px 24px 24px;

      overflow-x: visible;
      overflow-y: auto;
      -webkit-overflow-scrolling: touch;

      @media (min-width: 576px) {
        max-width: 540px;
        margin-right: auto;
        margin-left: auto;
      }
    }

    .Group.Location {
      display: none;
    }

    .FilterList.Selected-Location {
      .Group {
        display: none;
      }

      .Group.Selected {
        display: block;
      }
    }

    .BottomBar {
      height: 72px;
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;

      background: white;
      box-shadow: 0 -1px 2px 0 rgba(0, 0, 0, 0.2), 0 -1px 3px 0 rgba(0, 0, 0, 0.1);
    }

    .Cancel {
      display: none;
    }

    .Apply {
      height: 48px;
      margin: 12px 24px;
      flex-grow: 1;
    }
  }

  @media (min-width: 768px) {
    .FilterListContainer {
      position: fixed;

      box-shadow: 0 6px 6px 0 rgba(0, 0, 0, 0.26), 0 10px 20px 0 rgba(0, 0, 0, 0.19);
      border: 1px solid rgba(0, 0, 0, 0.1);
      padding: 16px 24px;
      border-radius: 4px;

      background: white;
      margin-top: 8px;
    }

    .ScrollList {
      overflow-y: auto;
      max-height: calc(90vh - 104px - 64px);
    }

    h2,
    .Group {
      display: none;
    }

    .Group.Selected {
      display: block;
    }

    .Cancel {
      padding: 6px 24px;
    }

    .Apply {
      height: 40px;
      min-width: 224px;
      margin-left: 8px;
    }
  }
</style>
