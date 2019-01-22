<template>
  <div>
    <div class="LocationCollectionContainer">
      <div class="LocationCollection">
        <div class="LocationCell" @click="toggleBetween"
             :class="{
         'bg-p500 white': isSelectedLocationType('Between'),
         'bg-whisper100 b-a75': !isSelectedLocationType('Between')}">
          EatBetween
        </div>

        <div class="LocationCell flex-center" @click="toggleNearby"
             :class="{
         'bg-p500 white': isSelectedLocationType('Nearby'),
         'bg-whisper100 b-a75': !isSelectedLocationType('Nearby')}">
          <beat-loader v-if="nearby.loading" class="flex-center" color="#0A6284" size="6px"/>
          <div v-else>Nearby</div>
        </div>

        <div class="LocationCell" @click="toggleAnywhere"
             :class="{
         'bg-p500 white': isSelectedLocationType('Anywhere'),
         'bg-whisper100 b-a75': !isSelectedLocationType('Anywhere')}">
          Anywhere
        </div>

        <div class="LocationCellRight"></div>
      </div>
    </div>

    <search-bar-filter-between class="LocationBetween" v-if="isSelectedLocationType('Between')"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import SearchBarFilterBetween from "./SearchBarFilterBetween";

  export default {
    name: "SearchBarFilterLocation",
    components: {SearchBarFilterBetween},
    data() {
      return {
        nearby: {
          loading: false
        },
        where: {},
        between: {},
      }
    },
    computed: {
      ...mapGetters('filter', ['isSelectedLocationType', 'isSelectedLocationArea'])
    },
    methods: {
      toggleNearby() {
        this.nearby.loading = true
        this.$store.commit('filter/loading', true)

        return this.$getLocation({
          enableHighAccuracy: true,
          timeout: 8000,
          maximumAge: 15000
        }).then(coordinates => {
          this.$store.commit('filter/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
          return this.$store.commit('filter/loading', false)
        }).then(() => {
          return this.$store.dispatch('filter/location', {type: 'Nearby'})
        }).catch(error => {
          return this.$store.dispatch('addMessage', {
            type: 'error',
            title: 'Location Not Available',
            message: 'Is location service enabled?'
          })
        }).finally(() => {
          this.nearby.loading = false
          return this.$store.commit('filter/loading', false)
        })
      },
      toggleAnywhere() {
        return this.$store.dispatch('filter/location', {type: 'Anywhere'})
      },
      toggleBetween() {
        return this.$store.dispatch('filter/location', {type: 'Between'})
      }
    }
  }
</script>

<style scoped lang="less">
  .LocationCollectionContainer {
    margin-left: -24px;
    margin-right: -24px;
  }

  .LocationCollection {
    display: flex;
    flex-flow: row nowrap;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;
    padding: 8px 24px;
  }

  .LocationCell {
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;

    line-height: 1.5;
    border-radius: 3px;
    padding: 12px 20px;
    margin-right: 16px;

    &:hover {
      cursor: pointer;
    }
  }

  .LocationCellRight {
    padding-left: 8px;
    min-height: 1px;
  }

  .LocationBetween {
    margin-top: 16px;

    max-width: 480px;

    @media (min-width: 768px) {
      width: 480px;
    }
  }
</style>
