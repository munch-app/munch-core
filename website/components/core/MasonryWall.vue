<template>
  <!-- Load with Queue -->
  <!-- Redraw on resize -->
  <div class="MasonryWall" :style="style.wall">
    <div class="MasonryWallLane" v-for="(lane, index) in lanes" :key="index" :style="style.lane">
      <div class="MasonryWallItem" v-for="i in lane.indexes" :key="i" :style="style.item">
        <slot :item="items[i]" :index="i">{{items[i]}}</slot>
      </div>

      <no-ssr :ref="lane.ref" style="flex-grow:1">
        <div v-observe-visibility="{callback: (v) => visibilityChanged(v,index),throttle:2}"/>
      </no-ssr>
    </div>
  </div>
</template>

<script>
  import _ from 'underscore'

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
        lanes: [],    // e.g. {indexes: [], requested: true, ref: ''}
        cursor: 0
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.redraw()
        window.addEventListener('resize', this.redraw)
      })
    },
    destroyed() {
      window.removeEventListener('resize', this.redraw)
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
        this.lanes[index].requested = isVisible
        this.fill()

        if (this.cursor < this.items.length) return

        this.$emit('append', () => {
          this.$nextTick(() => {
            this.fill()
          })
        })
      },

      redraw() {
        let length = Math.round(window.innerWidth / this.width)
        if (length < this.min) length = this.min
        if (this.lanes.length === length) return

        this.cursor = 0
        this.lanes.splice(0, this.lanes.length)
        for (let i = 0; i < length; i++) {
          this.lanes.push({
            indexes: [],
            requested: true,
            ref: 'lane' + i
          })
        }
      },

      fill() {
        const lane = _.max(this.lanes, (l) => {
          const ref = this.$refs[l.ref]
          return ref && ref[0] && ref[0].$el.clientHeight || 0
        })

        if (lane.requested) {
            const index = this.cursor
            if (lane.requested && this.items[index]) {
              lane.indexes.push(index)
              lane.requested = false
              this.cursor = index + 1
            }
        }
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
