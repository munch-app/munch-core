<template>
  <div>
    <portal to="dialog-full">
      <div class="BetweenScreen flex-column wh-100">
        <div class="flex flex-grow">
          <apple-map ref="map" class="flex-grow">
            <apple-map-marker-annotation v-for="(point, index) in locationPoints" :name="point.name"
                                         :lat-lng="point.latLng" :key="index"
                                         :icon="require('~/assets/icon/search/place.svg')"/>

            <apple-map-marker-annotation v-if="centroid" :lat-lng="centroid"
                                         :icon="require('~/assets/icon/search/flag.svg')"
                                         color="s500"/>
          </apple-map>

          <div class="index-0 Header container absolute h-48px flex-align-center flex-justify-between"
               :class="{Searching: searching}">
            <div class="flex-align-center">
              <img class="ImageLogo mr-8" src="~/assets/img/MunchLogo.svg">
              <h3>EatBetween</h3>
            </div>
            <simple-svg @click.native="onCancel" class="wh-24px hover-pointer" fill="black"
                        :filepath="require('~/assets/icon/close.svg')"/>
          </div>
        </div>

        <div class="index-1 BetweenDialog bg-white p-16-24 overflow-hidden">
          <div class="text">Enter everyone’s location and we’ll find the most ideal spot for a meal together.</div>

          <search-filter-between-search ref="search" @close="searching = false"/>

          <horizontal-scroll-view v-if="locationPoints.length > 0" :items="locationPoints"
                                  class="PointList mt-8 container-remove-gutter"
                                  :map-key="(_, i) => i" :padding="16">
            <template slot-scope="{item, index}">
              <div class="hover-pointer" @click="onRemove(index)">
                <div class="Point flex-row flex-align-center bg-whisper100 border-3">
                  <div class="flex-no-shrink text-nowrap text">
                    {{item.name}}
                  </div>

                  <div class="wh-16px Cancel flex-no-shrink">
                    <simple-svg fill="black" :filepath="require('~/assets/icon/close.svg')"/>
                  </div>
                </div>
              </div>
            </template>
          </horizontal-scroll-view>

          <div class="mt-16 flex">
            <button class="secondary-outline mr-16" @click.capture="onSearch">
              + Location
            </button>
            <button class="flex-grow" @click="onApply" v-if="applyText"
                    :class="{'bg-s500 white weight-600': isApplicable, 'bg-s050 b-a85 weight-600': !isApplicable}">
              {{applyText}}
            </button>
            <beat-loader v-else class="flex-grow border-3 bg-s050 flex-center" color="#084E69" size="8px"/>
          </div>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"
  import AppleMapMarkerAnnotation from "../../../components/core/AppleMapMarkerAnnotation";
  import HorizontalScrollView from "../../../components/core/HorizontalScrollView";
  import SearchFilterBetweenSearch from "../../../components/search/dialog/SearchFilterBetweenSearch";
  import AppleMap from "../../../components/core/AppleMap";

  function getCentroid(points) {
    if (points.length < 2) return null

    let cLat = 0.0, cLng = 0.0;
    points.forEach((point) => {
      const ll = point.latLng.split(',')
      cLat += parseFloat(ll[0])
      cLng += parseFloat(ll[1])
    });

    return `${cLat / points.length},${cLng / points.length}`
  }

  export default {
    name: "",
    components: {AppleMapMarkerAnnotation, HorizontalScrollView, SearchFilterBetweenSearch, AppleMap},
    computed: {
      ...mapGetters('filter', ['locationPoints', 'applyText', 'isApplicable']),
    },
    data() {
      return {
        centroid: null,
        searching: false,
      }
    },
    beforeRouteEnter(to, from, next) {
      next((vm) => {
        vm.hasPrevious = true
      });
    },
    mounted() {
      this.$store.commit('filter/updateLocation', {type: 'Between'})
      this.centroid = getCentroid(this.locationPoints)


      setTimeout(() => {
        if (this.$refs.map)
          this.$refs.map.centerAnnotations()
      }, 500);
    },
    watch: {
      locationPoints(points) {
        this.centroid = getCentroid(points)

        setTimeout(() => {
          if (this.$refs.map)
            this.$refs.map.centerAnnotations()
        }, 500);
      }
    },
    methods: {
      onSearch() {
        this.searching = true
        this.$refs.search.start()
      },
      onRemove(index) {
        this.$store.commit('filter/updateBetweenLocation', {index})
        this.$store.dispatch('filter/location', {type: 'Between'})
      },
      onApply() {
        if (this.isApplicable) {
          this.$store.dispatch('search/start')
          this.$track.search(`Search - Home: EatBetween`, this.$store.getters['search/locationType'])
        }
      },
      onCancel() {
        if (this.hasPrevious) {
          this.$router.go(-1)
        } else {
          this.$router.push({path: '/'})
        }
      }
    },
  }
</script>

<style scoped lang="less">
  button {
    padding-left: 12px !important;
    padding-right: 12px !important;
  }

  .PointList, .Point {
    height: 28px;
  }

  .Point {
    padding-left: 10px;
    padding-right: 8px;
  }

  .Cancel {
    margin-left: 6px;
  }

  .ImageLogo {
    height: 36px;
    width: 36px;
  }

  @media (max-width: 768px) {
    .Header {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;

      &.Searching {
        display: none;
      }
    }

    .BetweenScreen {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }

  .BetweenDialog {
    @media (min-width: 768px) {
      position: absolute;
      border-radius: 4px;
      width: 400px;

      top: 64px;
      left: 24px;
    }

    @media (min-width: 1200px) {
      left: 80px;
    }
  }
</style>
