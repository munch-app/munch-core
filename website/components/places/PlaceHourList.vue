<template>
  <div class="PlaceHourList" >
    <div class="HourToday" @click="popover = !popover">
      <span class="TodayShort">
        {{Day.today().text.substring(0, 3)}}:
      </span>
      <span class="TodayLong">
        {{Day.today().text}}:
      </span>
      <span :class="{'Open': isDayOpen(), 'Close': !isDayOpen() }">
        {{timeRange}}
      </span>
    </div>

    <div class="DialogContainer">
      <div class="HourDialog Elevation2" v-if="popover" v-on-clickaway="onClickaway">
        <div class="Content">
          <div class="DialogRow" v-for="day in days" :key="day.text">
            <div class="Day">{{day.text}}:</div>
            <div class="Hour"
                 :class="{'Open': day.isToday() && isDayOpen(day), 'Close': day.isToday() && !isDayOpen(day) }">
              {{group.getHour(day)}}
            </div>
          </div>
        </div>

        <div class="BottomBar">
          <div class="Primary500Bg White CloseButton Elevation1 Border3 text-center HoverPointer" @click="popover = false">
            CLOSE
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  const Day = {
    mon: {
      text: "Monday", isToday() {
        return Day.isToday(Day.mon)
      }
    },
    tue: {
      text: "Tuesday", isToday() {
        return Day.isToday(Day.tue)
      }
    },
    wed: {
      text: "Wednesday", isToday() {
        return Day.isToday(Day.wed)
      }
    },
    thu: {
      text: "Thursday", isToday() {
        return Day.isToday(Day.thu)
      }
    },
    fri: {
      text: "Friday", isToday() {
        return Day.isToday(Day.fri)
      }
    },
    sat: {
      text: "Saturday", isToday() {
        return Day.isToday(Day.sat)
      }
    },
    sun: {
      text: "Sunday", isToday() {
        return Day.isToday(Day.sun)
      }
    },
    other: {
      text: "Other", isToday() {
        return Day.isToday(Day.other)
      }
    },
    today() {
      const today = new Date();
      switch (today.getDay()) {
        case 1:
          return Day.mon
        case 2:
          return Day.tue
        case 3:
          return Day.wed
        case 4:
          return Day.thu
        case 5:
          return Day.fri
        case 6:
          return Day.sat
        case 7:
        case 0:
          return Day.sun
        default:
          return Day.other
      }
    },
    isToday(day) {
      return day === Day.today()
    },
    parse(text) {
      switch (text.toLowerCase()) {
        case "mon":
          return Day.mon
        case "tue":
          return Day.tue
        case "wed":
          return Day.wed
        case "thu":
          return Day.thu
        case "fri":
          return Day.fri
        case "sat":
          return Day.sat
        case "sun":
          return Day.sun
        default:
          return Day.other
      }
    }
  }
  const days = [Day.mon, Day.tue, Day.wed, Day.thu, Day.fri, Day.sat, Day.sun]

  function parseTimeHuman(time) {
    if (time === '24:00' || time === '23:59') {
      return "Midnight"
    }
    const hm = time.split(':')
    const hour = parseInt(hm[0])
    const min = parseInt(hm[1])
    const minS = min < 10 ? '0' + min : min
    if (hour < 13) {
      return hour + ':' + minS + 'am'
    } else {
      return hour % 12 + ':' + minS + 'pm'
    }
  }

  function parseTimeInt(time) {
    const hm = time.split(':')
    const hour = parseInt(hm[0])
    const min = parseInt(hm[1])
    return (hour * 60) + min
  }

  class Hour {
    constructor(day, open, close) {
      this.day = Day.parse(day)
      this.open = open
      this.close = close
    }

    timeRange() {
      return parseTimeHuman(this.open) + ' - ' + parseTimeHuman(this.close)
    }

    isBetween(date, opening, closing) {
      let day = new Date()
      const now = (day.getHours() * 60) + day.getMinutes()
      const open = parseTimeInt(this.open)
      const close = parseTimeInt(this.close)
      if (!opening) opening = 0
      if (!closing) closing = 0

      if (close < open) {
        return open - opening <= now && now + closing <= 1440
      }
      return open - opening <= now && now + closing <= close
    }
  }

  class HourGroup {
    constructor(hours) {
      this.hours = hours
      this.dayHours = {}

      hours.sort(function (a, b) {
        return a.open - b.open
      })
      for (const i in hours) {
        if (hours.hasOwnProperty(i)) {
          const hour = this.hours[i]
          if (this.dayHours[hour.day.text]) {
            this.dayHours[hour.day.text] = this.dayHours[hour.day.text] + ', ' + hour.timeRange()
          } else {
            this.dayHours[hour.day.text] = hour.timeRange()
          }
        }
      }
    }

    getHour(day) {
      return this.dayHours[day.text] || "Closed"
    }

    isOpen(today) {
      if (!this.hours) {
        return 'none'
      }

      const date = new Date()
      if (!today) {
        today = Day.today()
      }

      for (const i in this.hours) {
        if (this.hours.hasOwnProperty(i) && this.hours[i].day === today) {
          const hour = this.hours[i]
          if (hour.isBetween(date)) {
            if (!hour.isBetween(date, 0, 30)) {
              return 'closing'
            }
            return 'open'
          } else if (hour.isBetween(date, 30)) {
            return 'opening'
          }
        }
      }
      return 'closed'
    }

    todayTimeRange() {
      let today = Day.today()
      return this.getHour(today)
    }
  }

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
      onClickaway() {
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
    z-index: 1000;
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
      margin-top: 64px;
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
      z-index: 1000;
    }

    .HourDialog {
      border-radius: 4px;
      z-index: 1000;
      background: white;
      padding: 10px 15px;
    }

    .CloseButton {
      margin-top: 8px;
      padding: 6px 12px;

      width: 100px;
    }

    .BottomBar {
      flex-direction: row-reverse;
    }
  }
</style>
