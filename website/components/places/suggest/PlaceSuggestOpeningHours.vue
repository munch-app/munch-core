<template>
  <div>
    <div class="input-text">
      <label>Hours</label>
      <div v-if="hourGroup.hours.length > 0">
        <p style="font-size: 14px" v-for="(range, day) in hourGroup.dayHours">
          <b>{{day}}</b>{{": " + range}}
        </p>
      </div>
      <div v-else>
        <p style="font-size: 14px">
          Missing Opening Hours
        </p>
      </div>
      <div>
        <button @click="redirect" class="secondary-outline mt-16 small">Edit Hours</button>
      </div>
    </div>

    <portal to="dialog-full" v-if="show.more">
      <div class="index-1 BetweenDialog bg-white elevation-1 p-16-24 overflow-y-auto">
        <h3>Select days with incorrect hours</h3>
        <div class="mt-24">
          <div v-for="(dayName, index) in daysOfWeek">
            <div class="hover-pointer mb-16 flex-justify-between">
              <div class="flex-align-center">
                <div class="checkbox mr-16" v-bind:class="{selected: show.days[dayName]}"
                     @click="showHours($event.srcElement, dayName)"></div>
                <div class="text TimeRange-Text" v-html="dayName"></div>
              </div>
              <div class="text TimeRange-Text" v-if="!show.days[dayName]" v-html='hourGroup.dayHours[dayName]'></div>
              <div class="text TimeRange-Text Strikethrough" v-if="show.days[dayName]"
                   v-html='hourGroup.dayHours[dayName]'></div>
            </div>
            <div class="mb-48" v-if="show.days[dayName]">
              <div v-for="(hour, index) in hourGroup.hours">
                <div v-if="dayName === hour.day.text">
                  <div class="flex-align-center mb-8">
                    <input-time :value="hour.openObj"
                                @change="onTimeValueChange($event.data, index, 'open')"
                                format="H:mm" class="mr-24"></input-time>
                    &mdash;
                    <input-time :value="hour.closeObj"
                                @change="onTimeValueChange($event.data, index, 'close')"
                                format="H:mm" class="ml-24"></input-time>
                    <button class="ml-16 secondary-outline small" @click="removeHour(index)">Remove</button>
                  </div>
                  <div class="flex-align-center input-text">
                    <label v-if="hour.error" v-text="hour.error"></label>
                  </div>
                </div>
              </div>
              <div class="flex-align-center">
                <button class="secondary-outline small mr-16" @click="addHours(dayName)">Add</button>
                <button class="secondary-outline small mr-16" @click="set24Hours(dayName)">Open 24 Hrs</button>
                <button class="secondary-outline small" v-if="index !== 0" @click="setToPreviousDayHours(dayName)">Same
                  as {{daysOfWeek[index-1]}}
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="flex-justify-end mt-24">
          <button class="mr-8" @click="onCancel">Cancel</button>
          <button class="primary" v-if="noErrors" @click="onDone">Done</button>
          <button class="primary disabled" v-else>Done</button>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";
  import InputTime from "./input/InputTime";
  import {Day, days, Hour, HourGroup} from '../hour-group'
  import _ from 'lodash'

  export default {
    name: "PlaceSuggestOpeningHours",
    components: {InputText, InputTime},
    props: {
      value: {
        type: Array,
        required: true
      }
    },
    data() {
      const hourGroup = new HourGroup(this.value.map((h) => new Hour(h.day, h.open, h.close)))
      hourGroup.hours.forEach(hour => {
        hour.openObj = this.parseTimeForInput(hour.open)
        hour.closeObj = this.parseTimeForInput(hour.close)
        hour.error = ""
      })

      const originalHourGroup = JSON.parse(JSON.stringify(hourGroup))

      return {
        Day: Day,
        days: days,
        hourGroup: hourGroup,
        originalHourGroup: originalHourGroup,
        timeRange: hourGroup.todayTimeRange(),
        daysOfWeek: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        show: {
          more: false,
          days: {
            Monday: false,
            Tuesday: false,
            Wednesday: false,
            Thursday: false,
            Friday: false,
            Saturday: false,
            Sunday: false,
          }
        },
        selectedDays: [],
        noErrors: true,
      }
    },
    methods: {
      redirect() {
        this.show.more = true
      },
      showHours(event, dayName) {
        if (this.show.days[dayName]) {
          //Resets data on uncheck
          _.remove(this.hourGroup.hours, n => n.day.text === dayName)
          for (const hour of this.originalHourGroup.hours.filter(hour => hour.day.text === dayName)) {
            this.hourGroup.hours.push(hour)
          }
        }

        this.show.days[dayName] = !this.show.days[dayName]
      },
      removeHour(index) {
        this.hourGroup.hours.splice(index, 1)
      },
      getDayHours(dayName) {
        return _.filter(this.hourGroup.hours, function (hour) {
          return hour.day.text === dayName;
        })
      },
      getPreviousDayHours(dayName) {
        const days = {
          Monday: 1,
          Tuesday: 2,
          Wednesday: 3,
          Thursday: 4,
          Friday: 5,
          Saturday: 6,
          Sunday: 7,
        }

        if (dayName === "Monday") {
          return []
        }

        return _.filter(this.hourGroup.hours, function (hour) {
          return hour.day.text === Object.keys(days).find(key => days[key] === days[dayName] - 1);
        })
      },
      addHours(dayName) {
        const dayHours = this.getDayHours(dayName)
        if (dayHours.length === 1 && dayHours[0].open === "00:00" && dayHours[0].close === "23:59") {
          dayHours[0].error = dayName + " has already been set to 24 Hrs";
          return
        }

        this.hourGroup.hours.push({
          day: {text: dayName},
          open: '11:00',
          close: '21:30',
          openObj: {H: '11', mm: '00'},
          closeObj: {H: '21', mm: '30'},
          error: ""
        })
      },
      setToPreviousDayHours(dayName) {
        const previousDayHours = this.getPreviousDayHours(dayName)
        _.remove(this.hourGroup.hours, n => n.day.text === dayName)
        for (let hour of previousDayHours) {
          let newHour = JSON.parse(JSON.stringify(hour))
          newHour.day.text = dayName
          this.hourGroup.hours.push(newHour)
        }
      },
      set24Hours(dayName) {
        _.remove(this.hourGroup.hours, n => n.day.text === dayName)
        this.hourGroup.hours.push({
          day: {text: dayName},
          open: '00:00',
          close: '23:59',
          openObj: {H: '0', mm: '00'},
          closeObj: {H: '23', mm: '59'},
          error: ""
        })
      },
      onTimeValueChange(data, index, type) {
        const open = this.hourGroup.hours[index].openObj
        const close = this.hourGroup.hours[index].closeObj

        let openInt = (parseInt(open.H) * 60) + parseInt(open.mm)
        let closeInt = (parseInt(close.H) * 60) + parseInt(close.mm)
        const dataInt = (parseInt(data.H) * 60) + parseInt(data.m)

        if (openInt === dataInt || closeInt === dataInt) {
          return;
        }

        if (type === 'open') {
          openInt = dataInt
          this.hourGroup.hours[index].openObj = {H: String(data.H), mm: String(data.mm)}
        } else {
          closeInt = dataInt
          this.hourGroup.hours[index].closeObj = {H: String(data.H), mm: String(data.mm)}
        }

        if (openInt > closeInt) {
          this.hourGroup.hours[index].error = "Opening time cannot be later than closing time"
        } else if (openInt === closeInt) {
          this.hourGroup.hours[index].error = "Opening time and closing time cannot be the same"
        } else {
          this.hourGroup.hours[index].error = ""
        }

        this.noErrors = _.filter(this.hourGroup.hours, function (hour) {
          return hour.error.length > 0
        }).length === 0
      },
      onDone() {
        let newHours = []
        this.hourGroup.hours = _.sortBy(this.hourGroup.hours, [function (hour) {
          return days[hour.day.text];
        }]);
        for (const hour of this.hourGroup.hours) {
          newHours.push({
            open: this.getTimeIn24HoursStr(hour.openObj),
            close: this.getTimeIn24HoursStr(hour.closeObj),
            day: hour.day.text.toLowerCase().slice(0, 3)
          })
        }

        this.hourGroup = new HourGroup(newHours.map((h) => new Hour(h.day, h.open, h.close)))
        this.hourGroup.hours.forEach(hour => {
          hour.openObj = this.parseTimeForInput(hour.open)
          hour.closeObj = this.parseTimeForInput(hour.close)
          hour.error = ""
        })

        this.$emit('input', newHours)
        this.show.more = false
      },
      onCancel() {
        this.show.more = false
      },
      parseTimeForInput(time) {
        const hm = time.split(':')
        const hour = parseInt(hm[0])
        const min = parseInt(hm[1])
        const minS = min < 10 ? '0' + min : min
        return {H: String(hour), mm: String(minS)}
      },
      getTimeIn24HoursStr(time) {
        return `${time.H}:${time.mm}`
      }
    }
  }
</script>

<style scoped lang="less">
  .TimeRange-Text {
    font-size: 14px;
  }

  .BetweenDialog {
    margin-left: auto;
    margin-right: auto;
    margin-top: 10vh;
    max-height: 100% - 20vh;
    @media (min-width: 768px) {
      width: 600px;
    }
  }

  .Strikethrough {
    text-decoration: line-through;
  }
</style>
