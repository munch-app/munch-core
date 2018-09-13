<template>
  <div class="LocationCollection">
    <div class="LocationCell FlexCenter" @click="toggle('Nearby')" :class="{
         'Primary500Bg White': isSelectedLocation('Nearby'),
         'Whisper100Bg BlackA75': !isSelectedLocation('Nearby')}">
      <beat-loader v-if="loadingNearby" class="FlexCenter" color="#0A6284" size="6px"/>
      <div v-else>Nearby</div>
    </div>
    <div class="LocationCell" v-for="location in locations" :key="location" @click="toggle(location)"
         :class="{
         'Primary500Bg White': isSelectedLocation(location),
         'Whisper100Bg BlackA75': !isSelectedLocation(location)}">
      {{location}}
    </div>
  </div>
</template>

<script>
  import BeatLoader from 'vue-spinner/src/BeatLoader.vue'
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarFilterLocation",
    components: {BeatLoader},
    data() {
      return {
        locations: ['Anywhere'],
        loadingNearby: false
      }
    },
    mounted() {
      this.$watchLocation()
        .then(coordinates => {
          this.$store.commit('searchBar/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
        })
    },
    computed: {
      ...mapGetters('searchBar', ['isSelectedLocation'])
    },
    methods: {
      toggle(location) {
        if (location === 'Nearby') {
          this.loadingNearby = true
          return this.$getLocation({
            enableHighAccuracy: true,
            timeout: 8000,
            maximumAge: 15000
          })
            .then(coordinates => {
              this.$store.commit('searchBar/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
              this.$store.dispatch('searchBar/location', location)
            }).catch(error => {
              alert(error)
            }).finally(() => {
              this.loadingNearby = false
            })
        } else {
          this.$store.dispatch('searchBar/location', location)
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .LocationCollection {
    padding: 8px 0;
    display: flex;
    flex-flow: row nowrap;
    margin-left: -16px;

    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;
  }

  .LocationCell {
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;

    line-height: 1.5;
    border-radius: 3px;
    padding: 12px 20px;
    margin-left: 16px;

    &:hover {
      cursor: pointer;
    }
  }
</style>
