<template>
  <div class="LandingEatBetween">
    <div class="BetweenContainer container">
      <div class="Input">
        <h1>EatBetween</h1>
        <h2>Getting the gang together just got a whole lot easier</h2>
        <search-bar-filter-between/>

        <div class="Action">
          <div class="Button Cancel" @click="onClear">
            Clear
          </div>
          <div class="Button Apply" @click="onApply"
               :class="{'secondary-500-bg white weight-600': isApplicable, 'secondary-050-bg black-a-85 weight-600': !isApplicable}">
            {{applyText}}
          </div>
        </div>
      </div>

      <div class="Design flex-center">
        <img src="/img/landing/4-guys@2x.png">
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
      this.$store.commit('filter/selected', 'location')
      this.$store.dispatch('filter/location', {type: 'Between', areas: []})
    },
    methods: {
      onClear() {
        this.$store.dispatch('filter/clear', {tags: []})
      },
      onApply() {
        if (this.isApplicable) {
          this.$store.commit('filter/selected', null)
          this.$store.commit('unfocus', 'Filter')
          this.$store.dispatch('search/start', this.$store.state.filter.query)
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
      height: 60vh;

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
        margin-top: 16px;

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
      height: 100vh;
    }

    .Input {
      width: 100vw;
    }
  }

  @media (min-width: 768px) {
    .BetweenContainer {
      height: 100vh;
      align-items: center;

      max-width: 1200px;
    }

    .Input {
      width: 480px;
    }
  }
</style>
