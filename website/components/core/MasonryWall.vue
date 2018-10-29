<template>
  <div ref="wall" class="MasonryWall" :style="style.wall">
    <div class="MasonryWallLane" v-for="(lane, index) in lanes" :key="index" :style="style.lane">
      <div class="MasonryWallItem" v-for="i in lane.indexes" :key="i" :style="style.item" :ref="`item_${i}`">
        <slot :item="items[i]" :index="i">{{items[i]}}</slot>
      </div>

      <!--<no-ssr ref="spacers" style="flex-grow:1; min-height: 1500px">-->
      <!--<div v-observe-visibility="{callback: (v) => visibilityChanged(v,index),throttle:2}"/>-->
      <!--</no-ssr>-->
      <div ref="spacers" style="flex-grow:1;" :data-lane="index">
      </div>
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
        loading: true,
        height: 1000
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.redraw()
        window.addEventListener('resize', this.redraw)
        this.$fillInterval = setInterval(this.fill, 10)
      })
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.redraw)
      clearInterval(this.$fillInterval)
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
        this.lanes.splice(0, this.lanes.length)
        for (let i = 0; i < length; i++) {
          this.lanes.push({indexes: []})
        }

        this.height = (window.innerHeight || document.documentElement.clientHeight)
      },

      fill() {
        const spacers = this.$refs.spacers
        const spacer = _.max(spacers, (spacer) => {
          return spacer.clientHeight || 0
        })

        const top = spacer.getBoundingClientRect().top
        const index = spacer.dataset.lane

        // Loading only get triggered, after 200
        if (this.height + 200 > top) {
          this.loading = true
        } else if (this.height + 2000 < top) {
          this.loading = false
        }

        // Will load util, 2000
        if (this.loading) {
          this.addItemTo(index)
        }
      },

      addItemTo(laneIndex) {
        const lane = this.lanes[laneIndex]

        if (this.items[this.cursor]) {
          lane.indexes.push(this.cursor)
          this.cursor++
        }

        // 10 Items in Advance
        if (this.cursor + 10 < this.items.length) return
        this.$emit('append')
      },
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
  }
</style>
