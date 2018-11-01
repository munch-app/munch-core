<template>
  <div ref="wall" class="MasonryWall" :style="style.wall">
    <div class="MasonryWallLane" v-for="(lane, index) in lanes" :key="index" :style="style.lane">
      <div class="MasonryWallItem" v-for="i in lane.indexes" :key="i" :style="style.item" :ref="`item_${i}`">
        <slot :item="items[i]" :index="i">{{items[i]}}</slot>
      </div>

      <div ref="spacers" class="Spacers" :data-lane="index"
           v-observe-visibility="{callback: (v) => v && fill(),throttle:300}"
      />
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'

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
      /**
       * e.g.
       * {
       *     lanes: {
       *         1: {
       *             padding: 6
       *         },
       *         2: {
       *             padding: 9
       *         }
       *     }
       * }
       */
      options: {
        type: Object,
      },
      width: {
        type: Number,
        default: 300
      },
      padding: {
        type: Number,
        default: 12,
      },
      persistence: {
        type: Object
      }
    },
    data() {
      return {
        lanes: [],
        cursor: 0,
      }
    },
    mounted() {
      if (this.persistence && this.persistence.getter) {
        const {lanes, cursor} = JSON.parse(JSON.stringify(this.$store.getters[this.persistence.getter]))
        this.lanes = lanes
        this.cursor = cursor
      }

      this.redraw()
      window.addEventListener('resize', this.redraw)

      this.$nextTick(() => {
        this.fill()
      })
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.redraw)

      if (this.persistence && this.persistence.commit) {
        this.$store.commit(this.persistence.commit, {
          lanes: this.lanes, cursor: this.cursor
        })
      }
    },
    computed: {
      style() {
        const lane = this.options && this.options.lanes && this.options.lanes[this.lanes.length]
        const padding = lane && lane.padding || this.padding

        return {
          wall: {
            margin: `-${padding}px`
          },
          lane: {
            paddingLeft: `${padding}px`,
            paddingRight: `${padding}px`,
          },
          item: {
            paddingTop: `${padding}px`,
            paddingBottom: `${padding}px`,
          }
        }
      }
    },
    methods: {
      redraw() {
        let length = Math.round(this.$refs.wall.scrollWidth / this.width)
        if (length < this.min) length = this.min
        if (this.lanes.length === length) return

        this.cursor = 0
        this.lanes.splice(0)
        for (let i = 0; i < length; i++) {
          this.lanes.push({indexes: []})
        }
      },

      fill() {
        if (this.cursor >= this.items.length) {
          // Request for more items
          this.$emit('append')
          return
        }

        // Fill keep filling until no more items
        if (this.cursor < this.items.length) {
          this.$nextTick(() => {
            const spacers = this.$refs.spacers
            const spacer = _.maxBy(spacers, (spacer) => spacer.clientHeight || 0)
            this.fillItem(spacer.dataset.lane)
            this.fill()
          })
        }
      },

      fillItem(laneIndex) {
        const lane = this.lanes[laneIndex]

        if (this.items[this.cursor]) {
          lane.indexes.push(this.cursor)
          this.cursor++
        }
      },

      /**
       * @param index to scroll to
       */
      scrollTo(index) {
        const item = this.$refs['item_' + index]
        if (item && item[0]) {
          clearTimeout(this.$scrollEvent)
          this.$scrollEvent = setTimeout(() => {
            this.$scrollTo(item[0], 500)
          }, 501)
        }
      },
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

    .Spacers {
      flex-grow: 1;
      margin-top: -300px;
      padding-top: 300px;
      min-height: 100px;
    }
  }
</style>
