<template>
  <div class="LocationCollection">
    <div class="LocationCell flex-center" @click="toggleNearby"
         :class="{
         'primary-500-bg white': isSelectedLocationType('Nearby'),
         'whisper-100-bg black-a-75': !isSelectedLocationType('Nearby')}">
      <beat-loader v-if="nearby.loading" class="flex-center" color="#0A6284" size="6px"/>
      <div v-else>Nearby</div>
    </div>

    <div class="LocationCell" @click="toggleAnywhere"
         :class="{
         'primary-500-bg white': isSelectedLocationType('Anywhere'),
         'whisper-100-bg black-a-75': !isSelectedLocationType('Anywhere')}">
      Anywhere
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarFilterLocation",
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
          this.$store.commit('filter/loading', false)
          return this.$store.dispatch('filter/location', {type: 'Nearby'})
        }).catch(error => {
          return this.$store.dispatch('addError', error)
        }).finally(() => {
          this.nearby.loading = false
          return this.$store.commit('filter/loading', false)
        })
      },
      toggleAnywhere() {
        return this.$store.dispatch('filter/location', {type: 'Anywhere'})
      },
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
