<!-- @deprecated -->

<template>
  <div>
    <div ref="annotation">
      <div class="hover-pointer" @click="onClick">
        <div class="Marker flex-center relative" :class="`bg-${color}`">
          <simple-svg class="wh-24px" fill="white" :filepath="icon"/>

          <div class="Text text absolute text-nowrap" v-if="name">{{name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from '../utils/map/AppleMap'

  export default {
    name: "AppleMapMarkerAnnotation",
    components: {},
    props: {
      latLng: {
        type: String,
        required: true,
      },
      name: {
        type: String,
      },
      icon: {
        type: String,
      },
      color: {
        type: String,
        default: 'p500'
      }
    },
    computed: {},
    watch: {
      latLng(ll) {
        if (this.$annotation) {
          this.$annotation.coordinate = AppleMap.Coordinate(ll)
        }
      }
    },
    methods: {
      onClick() {

      },
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      }, {data: {name: this.name, icon: this.icon}})

      this.$parent.onMapLoaded((map) => {
        map.addAnnotation(this.$annotation)
      })
    },
    beforeDestroy() {
      const map = this.$parent.$map
      if (map && this.$annotation) {
        map.removeAnnotation(this.$annotation)
      }
    }
  }
</script>

<style scoped lang="less">
  .Marker {
    width: 32px;
    height: 32px;
    border-radius: 16px;

    margin-left: 16px;
    margin-top: 16px;
  }

  .Text {
    position: absolute;
    top: 30px;

    font-weight: 600;
    font-size: 11px;
  }
</style>
