<template>
  <div class="MasonryWall" :style="style.wall">
    <div class="MasonryWallLane" v-for="(lane, index) in lanes" :key="index" :style="style.lane">
      <div class="MasonryWallItem" v-for="i in lane.indexes" :key="i" :style="style.item">
        <slot :item="items[i]" :index="i">{{items[i]}}</slot>
      </div>

      <no-ssr v-if="!lane.requested" style="flex-grow:1">
        <div v-observe-visibility="{callback: (v) => visibilityChanged(v,index),throttle:2}"/>
      </no-ssr>
    </div>
  </div>
</template>

<script>
  export default {
    name: "MasonryWall",
    props: {
      items: {
        type: Array,
        required: true
      },
      min: {
        type: Number,
        default: 1
      },
      width: {
        type: Number,
        default: 300
      },
      padding: {
        type: Number,
        default: 12,
      }
    },
    data() {
      return {
        lanes: [],
        cursor: 0
      }
    },
    mounted() {
      this.$nextTick(() => {
        let index = Math.round(window.innerWidth / this.width)
        if (index < this.min) index = this.min

        this.lanes.splice(0, this.lanes.length)
        for (let i = 0; i < index; i++) {
          this.lanes.push({
            indexes: [],
            requested: true
          })
        }

        this.fill()
      })
    },
    computed: {
      style() {
        return {
          wall: {
            margin: `-${this.padding}px`
          },
          lane: {
            paddingLeft: `${this.padding}px`,
            paddingRight: `${this.padding}px`,
          },
          item: {
            paddingTop: `${this.padding}px`,
            paddingBottom: `${this.padding}px`,
          }
        }
      }
    },
    methods: {
      visibilityChanged(isVisible, index) {
        if (!isVisible) return
        this.lanes[index].requested = true

        this.fill()

        if (this.cursor < this.items.length) return

        this.$emit('append', () => {
          this.$nextTick(() => {
            this.fill()
          })
        })
      },
      fill() {
        this.lanes.forEach(lane => {
          const index = this.cursor
          if (lane.requested && this.items[index]) {
            lane.indexes.push(index)
            lane.requested = false
            this.cursor = index + 1
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .MasonryWall {
    display: flex;

    .MasonryWallLane {
      flex-grow: 1;
      flex-basis: 0;

      display: flex;
      flex-direction: column;
    }
  }
</style>
