<template>
  <div class="hover-pointer text">
    <div class="text-ellipsis-1l" @click="onPopover">
      <span class="subtext">
        {{Day.today().text.substring(0, 3).toUpperCase()}}:
      </span>
      <span :class="{'success': todayOpen, 'error': !todayOpen}">
        {{timeRange}}
      </span>
    </div>

    <no-ssr>
      <portal to="dialog-styled" v-if="popover">
        <div class="DayList flex-column" v-on-clickaway="onCancel">
          <div class="text" v-for="day in days" :key="day.text">
            <div class="weight-600">{{day.text}}:</div>
            <div :class="{'success': isDayOpen(day), 'error': day.isToday() && !isDayOpen(day)}">
              {{group.getHour(day)}}
            </div>
          </div>
        </div>
        <button class="secondary mt-8" @click="onCancel">Close</button>
      </portal>
    </no-ssr>
  </div>
</template>

<script>
  import {Day, days, Hour, HourGroup} from './hour-group'

  export default {
    name: "PlaceHourList",
    props: {
      hours: {
        type: Array,
        required: true,
      }
    },
    data() {
      const group = new HourGroup(this.hours.map((h) => new Hour(h.day, h.open, h.close)))

      return {
        Day: Day,
        days: days,
        group: group,
        timeRange: group.todayTimeRange(),
        popover: false,

        todayOpen: false
      }
    },
    mounted() {
      this.todayOpen = this.isDayOpen(Day.today())
    },
    methods: {
      isDayOpen(day) {
        if (!day.isToday()) return false

        switch (this.group.isOpen(day)) {
          case 'open':
          case 'opening':
            return true
        }
        return false
      },
      onPopover() {
        this.popover = !this.popover
      },
      onCancel() {
        if (this.popover) {
          this.popover = false
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .DayList {
    > div {
      margin-bottom: 8px;
      line-height: 1.3;
    }
  }

  button {
    margin-top: 0 !important;
  }
</style>
