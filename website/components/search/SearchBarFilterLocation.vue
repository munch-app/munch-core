<template>
  <div class="LocationCollection">
    <div class="LocationCell" v-for="location in locations" :key="location" @click="toggle(location)"
         :class="{Primary400Bg: isSelectedLocation(location), Whisper100Bg: !isSelectedLocation(location)}">
      {{location}}
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarFilterLocation",
    data() {
      return {locations: ['Nearby', 'Anywhere']}
    },
    mounted() {
      // this.$watchLocation()
      //   .then(coordinates => {
      //     this.$store.commit('searchBar/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
      //   })
    },
    computed: {
      ...mapGetters('searchBar', ['isSelectedLocation'])
    },
    methods: {
      toggle(location) {
        if (location === 'Nearby') {
          return this.$getLocation()
            .then(coordinates => {
              this.$store.commit('searchBar/updateLatLng', `${coordinates.lat},${coordinates.lng}`)
              this.$store.commit('searchBar/toggleLocation', location)
            });
        }

        this.$store.commit('searchBar/toggleLocation', location)
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
  }

  .LocationCell {
    font-size: 13px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);
    white-space: nowrap;

    line-height: 1.5;
    border-radius: 3px;
    padding: 10px 20px;
    margin-left: 16px;

    &.Primary400Bg {
      color: white;
    }

    &.Whisper100Bg {
      color: rgba(0, 0, 0, 0.75);
    }

    &:hover {
      cursor: pointer;
    }
  }
</style>
