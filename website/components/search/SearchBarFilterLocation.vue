<template>
  <div class="LocationCollection">
    <div class="LocationCell flex-center" @click="toggle('Nearby')" :class="{
         'primary-500-bg white': isSelectedLocation('Nearby'),
         'whisper-100-bg black-a-75': !isSelectedLocation('Nearby')}"
    >
      <beat-loader v-if="loadingNearby" class="flex-center" color="#0A6284" size="6px"/>
      <div v-else>Nearby</div>
    </div>
    <div class="LocationCell" v-for="location in locations" :key="location" @click="toggle(location)"
         :class="{
         'primary-500-bg white': isSelectedLocation(location),
         'whisper-100-bg black-a-75': !isSelectedLocation(location)}"
    >
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
    computed: {
      ...mapGetters('filter', ['isSelectedLocation'])
    },
    methods: {
      toggle(location) {
        if (location === 'Nearby') {
          this.loadingNearby = true
          this.$store.commit('filter/loading', true)

          return this.$getLocation({
            enableHighAccuracy: true,
            timeout: 8000,
            maximumAge: 15000
          }).then(coordinates => {
            this.$store.commit('filter/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
            this.$store.commit('filter/loading', false)
            this.$store.dispatch('filter/location', location)
          }).catch(error => {
            this.$store.dispatch('addError', error)
          }).finally(() => {
            this.$store.commit('filter/loading', false)
            this.loadingNearby = false
          })
        } else {
          this.$store.dispatch('filter/location', location)
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
