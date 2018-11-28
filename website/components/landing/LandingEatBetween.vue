<template>
  <div class="LandingEatBetween">
    <div class="BetweenContainer container">
      <div class="Input">
        <h1>EatBetween</h1>
        <div class="Interaction">
          <search-bar-filter-between/>

          <div class="Action">
            <div class="Button Cancel" @click="onClear">
              Clear
            </div>
            <div class="Button Apply" @click="onApply"
                 :class="{'bg-s500 white weight-600': isApplicable, 'bg-s050 b-a85 weight-600': !isApplicable}">
              {{applyText}}
            </div>
          </div>
        </div>
      </div>

      <div class="Design flex-center">
        <img src="~/assets/img/home/4-guys.png">
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import SearchBarFilterBetween from "../search/SearchBarFilterBetween";
  import AppleMap from "../core/AppleMap";

  export default {
    name: "LandingEatBetween",
    components: {AppleMap, SearchBarFilterBetween},
    computed: {
      ...mapGetters('filter', ['applyText', 'isApplicable']),
    },
    mounted() {
      // This interfere with SearchQuery
      this.$store.commit('filter/selected', 'location')
      this.$store.dispatch('filter/location', {type: 'Between', areas: []})
    },
    destroyed() {
      this.$store.commit('filter/selected', null)
      this.$store.commit('unfocus', 'Filter')
    },
    methods: {
      onClear() {
        this.$store.dispatch('filter/clear', {tags: []})
      },
      onApply() {
        if (this.isApplicable) {
          this.$store.dispatch('search/start')
          this.$track.search(`Search - Landing: EatBetween`, this.$store.getters['search/locationType'])
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .BetweenContainer {
    display: flex;
    padding-top: 56px;
    padding-bottom: 56px;

    .Input {
      h1 {
        margin-top: 24px;
        margin-bottom: 16px;
      }

      h2 {
        margin-bottom: 24px;
      }

      .Action {
        display: flex;
        justify-content: flex-end;
        padding-top: 16px;

        .Button {
          line-height: 1.5;
          border-radius: 3px;
          font-size: 16px;

          &:hover {
            cursor: pointer;
          }
        }

        .Cancel {
          padding: 6px 18px 6px 0;
        }

        .Apply {
          text-align: center;
          padding: 6px 0;
          width: 208px;
          margin-left: 8px;
        }
      }
    }

    .Design {
      flex: 50%;

      img {
        width: 100%;
        padding-left: 64px;
        padding-right: 64px;
      }

      @media (max-width: 767.98px) {
        display: none;
      }
    }
  }

  @media (max-width: 767.98px) {
    .BetweenContainer {
      min-height: 100vh;
    }

    .Input {
      width: calc(100vw - 48px);
    }
  }

  @media (min-width: 768px) {
    .BetweenContainer {
      min-height: 100vh;
      align-items: center;

      max-width: 1200px;
    }

    .Input {
      width: 480px;
    }

    .Interaction {
      min-height: 50vh;
    }
  }
</style>
