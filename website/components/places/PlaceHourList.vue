<template>
  <div class="PlaceHourList">
    <div class="HourToday" @click="popover = !popover">
      <span class="TodayShort">
        {{Day.today().text.substring(0, 3)}}:
      </span>
      <span class="TodayLong">
        {{Day.today().text}}:
      </span>
      <span :class="{'timing-open': isDayOpen(), 'timing-close': !isDayOpen() }">
        {{timeRange}}
      </span>
    </div>

    <div class="DialogContainer index-elevation">
      <div class="HourDialog elevation-2 index-elevation" v-if="popover" v-on-clickaway="onClickAway">
        <div class="Content">
          <div class="DialogRow" v-for="day in days" :key="day.text">
            <div class="Day">{{day.text}}:</div>
            <div class="Hour"
                 :class="{'timing-open': day.isToday() && isDayOpen(day), 'timing-close': day.isToday() && !isDayOpen(day) }">
              {{group.getHour(day)}}
            </div>
          </div>
        </div>

        <div class="BottomBar">
          <div class="secondary-500-bg white CloseButton elevation-1 border-3 text-center hover-pointer"
               @click="popover = false">
            CLOSE
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {Day, days, Hour, HourGroup} from './hour-group'

  export default {
    name: "PlaceHourList",
    props: {
      hours: {
        required: true,
        twoWay: false,
        type: Array
      }
    },
    data() {
      const hours = this.hours.map((h) => new Hour(h.day, h.open, h.close))
      const group = new HourGroup(hours)
      let isOpen = false
      let text = ''
      switch (group.isOpen()) {
        case 'open':
          isOpen = true
          text = "Open Now"
          break
        case 'closed':
          isOpen = false
          text = "Closed Now"
          break
        case 'opening':
          isOpen = true
          text = "Opening Soon"
          break
        case 'closing':
          isOpen = false
          text = "Closing Soon"
          break
      }

      return {
        Day: Day,
        days: days,
        group: group,
        labelText: text,
        isOpen: isOpen,
        timeRange: group.todayTimeRange(),
        popover: false
      }
    },
    methods: {
      isDayOpen(day) {
        if (!day) {
          day = Day.today()
        }

        switch (this.group.isOpen(day)) {
          case 'open':
          case 'opening':
            return true
        }
        return false
      },
      onClickAway() {
        if (this.popover) {
          this.popover = false
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceHourList:hover {
    cursor: pointer;
  }

  .HourToday {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }

  .HourDialog {
    padding: 0 15px;
    text-align: left;
    background: white;

    width: 100%;
    left: 0;
    right: 0;

    .Content {
      margin-bottom: -6px;

      .DialogRow {
        margin-bottom: 6px;
      }
    }

    .Day {
      font-size: 15px;
      font-weight: 600;
      line-height: 20px;
    }

    .Hour {
      line-height: 20px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
  }

  .BottomBar {
    display: flex;
  }

  @media (max-width: 575.98px) {
    .TodayLong {
      display: none;
    }

    .HourDialog {
      background: white;
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      margin-top: 56px;
      padding: 10px 15px;
    }

    .CloseButton {
      margin-top: 8px;
      padding: 6px 12px;
      flex-grow: 1;
    }

    .BottomBar {
      flex-direction: row-reverse;
    }
  }

  @media (min-width: 576px) {
    .TodayShort {
      display: none;
    }

    .DialogContainer {
      margin-top: -30px;
      position: absolute;
    }

    .HourDialog {
      min-width: 264px;
      border-radius: 4px;
      background: white;
      padding: 16px 20px;
    }

    .CloseButton {
      margin-top: 12px;
      padding: 6px 12px;

      width: 100px;
    }

    .BottomBar {
      flex-direction: row-reverse;
    }
  }
</style>
