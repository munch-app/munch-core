<template>
<span class="time-picker">
  <input @click.stop="toggleDropdown" class="display-time" type="text" v-model="displayTime"/>
  <div @click.stop="toggleDropdown" class="time-picker-overlay" v-if="showDropdown"></div>
  <div class="dropdown" v-show="showDropdown">
    <div class="select-list">
      <ul class="hours">
        <li class="hint" v-text="hourType"></li>
        <li :class="{active: hour === hr}" @click.stop="select('hour', hr)" v-for="hr in hours" v-text="hr"></li>
      </ul>
      <ul class="minutes">
        <li class="hint" v-text="minuteType"></li>
        <li :class="{active: minute === m}" @click.stop="select('minute', m)" v-for="m in minutes" v-text="m"></li>
      </ul>
      <ul class="apms" v-if="apmType">
        <li class="hint" v-text="apmType"></li>
        <li :class="{active: apm === a}" @click.stop="select('apm', a)" v-for="a in apms" v-text="a"></li>
      </ul>
    </div>
  </div>
</span>
</template>
<script>
  const CONFIG = {
    HOUR_TOKENS: ['HH', 'H', 'hh', 'h'],
    MINUTE_TOKENS: ['mm', 'm'],
    APM_TOKENS: ['A', 'a']
  }

  export default {
    name: 'VueTimepicker',
    props: {
      value: {type: Object},
      hideClearButton: {type: Boolean},
      format: {type: String},
      minuteInterval: {type: Number}
    },

    data() {
      return {
        hours: [],
        minutes: [],
        apms: [],
        showDropdown: false,
        muteWatch: false,
        hourType: 'H',
        minuteType: 'mm',
        apmType: '',
        hour: '',
        minute: '',
        apm: '',
        fullValues: undefined
      }
    },

    computed: {
      displayTime: {
        set: function (val) {
          if (/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/.test(val)) {
            const hm = val.split(':')
            const hour = parseInt(hm[0])
            const min = parseInt(hm[1])
            const minS = min < 10 ? '0' + min : min

            this.hour = hour
            this.minute = minS
          } else {

          }
        },
        get: function () {
          let formatString = String((this.format || 'H:mm'))
          if (this.hour) {
            formatString = formatString.replace(new RegExp(this.hourType, 'g'), this.hour)
          }
          if (this.minute) {
            formatString = formatString.replace(new RegExp(this.minuteType, 'g'), this.minute)
          }
          if (this.apm && this.apmType) {
            formatString = formatString.replace(new RegExp(this.apmType, 'g'), this.apm)
          }
          return formatString
        }
      },
    },

    watch: {
      'format': 'renderFormat',
      minuteInterval(newInteval) {
        this.renderList('minute', newInteval)
      },
      'value': 'readValues',
      'displayTime': 'fillValues'
    },

    methods: {
      formatValue(type, i) {
        switch (type) {
          case 'H':
          case 'm':
            return String(i)
          case 'HH':
          case 'mm':
            return i < 10 ? `0${i}` : String(i)
          case 'h':
            return String(i + 1)
          case 'hh':
            return (i + 1) < 10 ? `0${i + 1}` : String(i + 1)
          default:
            return ''
        }
      },

      checkAcceptingType(validValues, formatString, fallbackValue) {
        if (!validValues || !formatString || !formatString.length) {
          return ''
        }
        for (let i = 0; i < validValues.length; i++) {
          if (formatString.indexOf(validValues[i]) > -1) {
            return validValues[i]
          }
        }
        return fallbackValue || ''
      },

      renderFormat(newFormat) {
        newFormat = newFormat || this.format
        if (!newFormat || !newFormat.length) {
          newFormat = 'HH:mm'
        }

        this.hourType = this.checkAcceptingType(CONFIG.HOUR_TOKENS, newFormat, 'HH')
        this.minuteType = this.checkAcceptingType(CONFIG.MINUTE_TOKENS, newFormat, 'mm')
        this.apmType = this.checkAcceptingType(CONFIG.APM_TOKENS, newFormat)

        this.renderHoursList()
        this.renderList('minute')

        if (this.apmType) {
          this.renderApmList()
        }

        const self = this
        this.$nextTick(() => {
          self.readValues()
        })
      },

      renderHoursList() {
        let hoursCount = (this.hourType === 'h' || this.hourType === 'hh') ? 12 : 24

        if (this.apm === "pm") {
          hoursCount = 11
        }

        this.hours = []
        for (let i = 0; i < hoursCount; i++) {
          this.hours.push(this.formatValue(this.hourType, i))
        }
      },

      renderList(listType, interval) {
        if (listType === 'minute') {
          interval = interval || this.minuteInterval
        } else {
          return
        }

        if (interval === 0) {
          interval = 60
        } else if (interval > 60) {
          window.console.warn('`' + listType + '-interval` should be less than 60. Current value is', interval)
          interval = 1
        } else if (interval < 1) {
          window.console.warn('`' + listType + '-interval` should be NO less than 1. Current value is', interval)
          interval = 1
        } else if (!interval) {
          interval = 1
        }

        if (listType === 'minute') {
          this.minutes = []
        }

        for (let i = 0; i < 60; i += interval) {
          if (listType === 'minute') {
            this.minutes.push(this.formatValue(this.minuteType, i))
          }
        }
      },

      renderApmList() {
        this.apms = []
        if (!this.apmType) {
          return
        }
        this.apms = this.apmType === 'A' ? ['AM', 'PM'] : ['am', 'pm']
      },

      readValues() {
        if (!this.value || this.muteWatch) {
          return
        }

        const timeValue = JSON.parse(JSON.stringify(this.value || {}))

        const values = Object.keys(timeValue)
        if (values.length === 0) {
          return
        }

        if (values.indexOf(this.hourType) > -1) {
          this.hour = timeValue[this.hourType]
        }

        if (values.indexOf(this.minuteType) > -1) {
          this.minute = timeValue[this.minuteType]
        }

        if (values.indexOf(this.apmType) > -1) {
          this.apm = timeValue[this.apmType]
        }
        this.renderHoursList()
        this.fillValues()
      },

      fillValues() {
        let fullValues = {}

        const baseHour = this.hour
        const baseHourType = this.hourType

        const hourValue = baseHour || baseHour === 0 ? Number(baseHour) : ''
        const baseOnTwelveHours = this.isTwelveHours(baseHourType)
        const apmValue = (baseOnTwelveHours && this.apm) ? String(this.apm).toLowerCase() : false

        CONFIG.HOUR_TOKENS.forEach((token) => {
          if (token === baseHourType) {
            fullValues[token] = baseHour
            return
          }

          let value
          let apm
          switch (token) {
            case 'H':
            case 'HH':
              if (!String(hourValue).length) {
                fullValues[token] = ''
                return
              } else if (baseOnTwelveHours) {
                if (apmValue === 'pm') {
                  value = hourValue < 12 ? hourValue + 12 : hourValue
                } else {
                  value = hourValue % 12
                }
              } else {
                value = hourValue % 24
              }
              fullValues[token] = (token === 'HH' && value < 10) ? `0${value}` : String(value)
              break
            case 'h':
            case 'hh':
              if (apmValue) {
                value = hourValue
                apm = apmValue || 'am'
              } else {
                if (!String(hourValue).length) {
                  fullValues[token] = ''
                  fullValues.a = ''
                  fullValues.A = ''
                  return
                } else if (hourValue > 11) {
                  apm = 'pm'
                  value = hourValue === 12 ? 12 : hourValue % 12
                } else {
                  if (baseOnTwelveHours) {
                    apm = ''
                  } else {
                    apm = 'am'
                  }
                  value = hourValue % 12 === 0 ? 12 : hourValue
                }
              }
              fullValues[token] = (token === 'hh' && value < 10) ? `0${value}` : String(value)
              fullValues.a = apm
              fullValues.A = apm.toUpperCase()
              break
          }
        })

        if (this.minute || this.minute === 0) {
          const minuteValue = Number(this.minute)
          fullValues.m = String(minuteValue)
          fullValues.mm = minuteValue < 10 ? `0${minuteValue}` : String(minuteValue)
        } else {
          fullValues.m = ''
          fullValues.mm = ''
        }

        this.fullValues = fullValues
        this.updateTimeValue(fullValues)
        this.$emit('change', {data: fullValues})
      },

      updateTimeValue(fullValues) {
        this.muteWatch = true

        const self = this

        const baseTimeValue = JSON.parse(JSON.stringify(this.value || {}))
        let timeValue = {}

        Object.keys(baseTimeValue).forEach((key) => {
          timeValue[key] = fullValues[key]
        })

        this.$emit('input', timeValue)

        this.$nextTick(() => {
          self.muteWatch = false
        })
      },

      isTwelveHours(token) {
        return token === 'h' || token === 'hh'
      },

      toggleDropdown() {
        this.showDropdown = !this.showDropdown
      },

      select(type, value) {
        if (type === 'hour') {
          this.hour = value
        } else if (type === 'minute') {
          this.minute = value
        } else if (type === 'apm') {
          this.apm = value
          this.renderHoursList()
        }
      },
      parseTimeForInput(time) {
        const hm = time.split(':')
        const hour = parseInt(hm[0])
        const min = parseInt(hm[1])
        const minS = min < 10 ? '0' + min : min
        if (hour < 13) {
          return {h: hour, mm: minS, a: "am"}
        } else {
          return {h: hour % 12, mm: minS, a: "pm"}
        }
      },
    },

    mounted() {
      this.renderFormat()
    },
  }
</script>

<style scoped>
  .time-picker {
    display: inline-block;
    position: relative;
    font-size: 1em;
    width: 10em;
    font-family: sans-serif;
    vertical-align: middle;
  }

  .time-picker * {
    box-sizing: border-box;
  }

  .time-picker input.display-time {
    border: 1px solid #d2d2d2;
    width: 10em;
    height: 2.2em;
    padding: 0.3em 0.5em;
    font-size: 1em;
  }

  .time-picker .time-picker-overlay {
    z-index: 2;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .time-picker .dropdown {
    position: absolute;
    z-index: 5;
    top: calc(2.2em + 2px);
    left: 0;
    background: #fff;
    box-shadow: 0 1px 6px rgba(0, 0, 0, 0.15);
    width: 10em;
    height: 10em;
    font-weight: normal;
  }

  .time-picker .dropdown .select-list {
    width: 10em;
    height: 10em;
    overflow: hidden;
    display: flex;
    flex-flow: row nowrap;
    align-items: stretch;
    justify-content: space-between;
  }

  .time-picker .dropdown ul {
    padding: 0;
    margin: 0;
    list-style: none;

    flex: 1;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .time-picker .dropdown ul.minutes,
  .time-picker .dropdown ul.apms {
    border-left: 1px solid #fff;
  }

  .time-picker .dropdown ul li {
    text-align: center;
    padding: 0.3em 0;
    color: #161616;
  }

  .time-picker .dropdown ul li:not(.hint):hover {
    background: rgba(0, 0, 0, .08);
    color: #161616;
    cursor: pointer;
  }

  .time-picker .dropdown ul li.active,
  .time-picker .dropdown ul li.active:hover {
    background: #F05F3B;
    color: #fff;
  }

  .time-picker .dropdown .hint {
    color: #a5a5a5;
    cursor: default;
    font-size: 0.8em;
  }

</style>
