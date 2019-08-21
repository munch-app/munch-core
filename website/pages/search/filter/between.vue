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
              <a href="/" class="flex-center">
                <img class="ImageLogo mr-8" src="~/assets/img/LOGO_TRIM.svg">
              </a>
              <h3>EatBetween</h3>
            </div>
            <simple-svg @click.native="onCancel" class="wh-24px hover-pointer" fill="black"
                        :filepath="require('~/assets/icon/close.svg')"/>
          </div>
        </div>

        <div class="index-1 BetweenDialog bg-white p-16-24 overflow-hidden">
          <div class="text mb-16">Enter everyone’s location to find the most ideal spot for a meal together.
          </div>

          <search-filter-between-search ref="search" @close="searching = false"/>

          <div v-if="!searching">
            <div class="LocationItemList">
              <div class="LocationItem PointItem flex-align-center"
                   v-if="locationPoints.length > 0" v-for="(point, index) in locationPoints">
                <simple-svg class="ml-8 wh-24px flex-no-shrink" fill="#F05F3B"
                            :filepath="require('~/assets/icon/location/Location_Pin.svg')"/>
                <div class="mlr-8 flex-grow hr-bot">
                  <p class="text-ellipsis-1l">{{point.name}}</p>
                </div>
                <div @click="onRemove(index)" class="hover-pointer flex-no-shrink">
                  <simple-svg class="wh-20px" :filepath="require('~/assets/icon/location/Location_Cancel.svg')"/>
                </div>
              </div>
            </div>

            <div class="LocationItem flex-align-center hover-pointer border border-4" @click="onSearch"
                 v-if="locationPoints.length < 10">
              <simple-svg class="EnterPin wh-24px" :filepath="require('~/assets/icon/location/Location_Pin.svg')"
                          fill="rgba(0,0,0,0.6)"/>
              <div class="mlr-8 flex-grow">
                <p class="b-a60 lh-1">Enter location</p>
              </div>
            </div>
          </div>

          <div class="mt-16 mb-8 flex">
            <button class="flex-grow" @click="onApply" v-if="applyText"
                    :class="{'bg-s500 white weight-600': isApplicable, 'b-a85 weight-600': !isApplicable}">
              {{applyText}}
            </button>
            <beat-loader v-else class="flex-grow border-3 bg-s050 flex-center p-16" color="#084E69" size="8px"/>
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
  import AppleMap from "../../../components/utils/map/AppleMap";

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

  .LocationItem {
    p {
      margin: 10px 0;
    }
  }

  .PointItem:last-child {
    div {
      border: none;
    }
  }

  .EnterPin {
    margin-left: 7px;
  }

  .ImageLogo {
    height: 36px;
    width: 36px;
  }

  .LocationItemList {
    max-height: 60vh;
    overflow-y: auto;
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

    .LocationItemList {
      max-height: 20vh;
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
