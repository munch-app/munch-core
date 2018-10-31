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
        lanes: [],
        cursor: 0,
      }
    },
    mounted() {
      this.redraw()
      window.addEventListener('resize', this.redraw)
    },
    beforeDestroy() {
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
            const spacer = _.max(spacers, (spacer) => spacer.clientHeight || 0)
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
      flex-grow:1;
      margin-top: -300px;
      padding-top: 300px;
      min-height: 100px;
    }
  }
</style>
