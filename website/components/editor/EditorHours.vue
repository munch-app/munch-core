<template>
  <div class="EditorHours monospace border border-3 bg-steam p-16 relative">
    <div>
      <div class="m--8">
        <div class="p-8" v-for="type in dayTypes" :key="type">
          <div class="flex">
            <div class="p-8 bg-white border-2 flex-self-start">
              {{type}}:
            </div>

            <div class="ml-16">
              <div class="m--8 flex-wrap">
                <div class="p-8" v-for="(hour, index) in days[type]" :key="index">
                  <div class="bg-white border-2 overflow-hidden">
                    <div class="p-8-12 hover-bg-a10 hover-pointer flex-align-center" @click="onRemove(hour)">
                      <div>
                        <div class="">{{toText(hour)}}</div>
                      </div>

                      <div class="ml-4">
                        <simple-svg class="wh-16px" fill="black"
                                    :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-8 no-select" v-if="editing.length < max">
      <div class="flex m--8">

        <div class="p-8">
          <div class="relative">
            <div class="bg-white border-2 overflow-hidden">
              <div class="p-8 flex-center hover-bg-a10 hover-pointer flex-align-center" @click="onFocusDay">
                <simple-svg v-if="state === 'days'" class="wh-24px" fill="black"
                            :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
                <simple-svg v-else class="wh-24px" fill="black"
                            :filepath="require('~/assets/icon/icons8-month-view.svg')"/>
                <div v-if="state !== 'days' && input.days.length" class="ml-4">x{{input.days.length}}</div>
              </div>
            </div>

            <div class="absolute" v-if="state === 'days'" style="left: 0; bottom: 48px">
              <div class="bg-white border-3 border index-1">
                <div class="m--6 p-12">
                  <div class="flex">
                    <div class="p-6" v-for="day in dayTypes.slice(0, 5)" :key="day">
                      <div
                        @click="onInputDay(day)"
                        class="hover-pointer p-8 border-2"
                        :class="onContainsDay(day) ? 'bg-blue white' : 'bg-steam black'">
                        {{day}}
                      </div>
                    </div>
                  </div>
                  <div class="flex">
                    <div class="p-6" v-for="day in dayTypes.slice(5, 7)" :key="day">
                      <div @click="onInputDay(day)"
                           class="hover-pointer p-8 border-2"
                           :class="onContainsDay(day) ? 'bg-blue white' : 'bg-steam black'">
                        {{day}}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="flex-grow flex">
          <div class="p-8">
            <div class="relative TimeInput bg-white border-2" :class="{'Warning': input.open === null}">
              <label>OPEN:</label>
              <input v-model="input.editingOpen" @keyup="onInputChange" placeholder="HH:MM"
                     @focus="onFocusOpen">

              <div v-if="state === 'open'"
                   class="absolute hr-top border-2 bg-white" style="bottom: 40px; left: 0; right: 0">
                <div v-for="time in ['10:00', '10:30', '11:00', '11:30']" :key="time">
                  <div class="p-6 hover-bg-a10 hover-pointer" @click="onInputOpen(time)">{{time}}</div>
                </div>
              </div>
            </div>
          </div>

          <div class="p-8">
            <div class="relative TimeInput bg-white border-2" :class="{'Warning': input.close === null}">
              <label>CLOSE:</label>
              <input v-model="input.editingClose" @keyup="onInputChange" placeholder="HH:MM"
                     @focus="onFocusClose">

              <div v-if="state === 'close'"
                   class="absolute hr-top border-2 bg-white" style="bottom: 40px; left: 0; right: 0">
                <div v-for="time in ['21:30', '22:00', '22:30', '24:00']" :key="time">
                  <div class="p-6 hover-bg-a10 hover-pointer" @click="onInputClose(time)">{{time}}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="p-8">
          <div class="wh-40px flex-center border-2 bg-blue hover-pointer" @click="onAdd">
            <simple-svg class="wh-24px" fill="white" :filepath="require('~/assets/icon/icons8-add.svg')"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  function mapTimeRange(hour) {
    const open = {
      hh: `${Math.floor(hour.open / 60)}`.padStart(2, '0'),
      mm: `${hour.open % 60}`.padStart(2, '0')
    }
    const close = {
      hh: `${Math.floor(hour.close / 60)}`.padStart(2, '0'),
      mm: `${hour.close % 60}`.padStart(2, '0')
    }
    return `${open.hh}:${open.mm} - ${close.hh}:${close.mm}`
  }

  function parseNaturalText(text) {
    text = text?.trim()
    const hhmm = {}

    hhmm.h1 = parseInt(text[0])
    hhmm.h2 = parseInt(text[1])
    hhmm.m1 = parseInt(text[2])
    hhmm.m2 = parseInt(text[3])

    if (!isNaN(hhmm.m2) && isNaN(hhmm.m1)) {
      hhmm.m1 = hhmm.m2
      hhmm.m2 = undefined
    }

    if (isNaN(hhmm.m2)) {
      hhmm.m2 = parseInt(text[4])
    }

    if (isNaN(hhmm.m1) || isNaN(hhmm.m2) || isNaN(hhmm.h1) || isNaN(hhmm.h2)) {
      return {text: text, time: null}
    }

    const time = hhmm.h1 * 600 + hhmm.h2 * 60 + hhmm.m1 * 10 + hhmm.m2
    if (time > 1440) {
      return {text: text, time: null}
    }

    return {
      text: `${hhmm.h1}${hhmm.h2}:${hhmm.m1}${hhmm.m2}`,
      time: time
    }
  }

  export default {
    name: "EditorHours",
    props: {
      value: Array,
      max: {
        type: Number,
        default: 24
      }
    },
    data() {
      let editing = []
      if (this.value) {
        editing = JSON.parse(JSON.stringify(this.value))
      }

      return {
        editing: editing,
        dayTypes: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
        input: {
          days: [],
          open: undefined,
          close: undefined,
          editingOpen: '11:00',
          editingClose: '22:00',
        },
        state: null
      }
    },
    computed: {
      days() {
        const days = {'MON': [], 'TUE': [], 'WED': [], 'THU': [], 'FRI': [], 'SAT': [], 'SUN': []}
        for (const hour of this.editing) {
          days[hour.day].push(hour)
          days[hour.day].sort(h => h.open)
        }
        return days
      }
    },
    methods: {
      toText(hour) {
        return mapTimeRange(hour)
      },
      onRemove(hour) {
        const index = _.findIndex(this.editing, (h) => hour.open === h.open && hour.close === h.close && hour.day === h.day)
        if (index > -1) {
          this.editing.splice(index, 1)
        }
        this.update()
      },
      onAdd() {
        this.onInputChange()
        if (this.input.days.length === 0) {
          this.state = 'days'
        }

        if (this.input.days.length) {
          this.input.days.forEach(day => {
            this.editing.push({
              day: day,
              open: this.input.open,
              close: this.input.close,
            })
          })

          this.input.open = undefined
          this.input.close = undefined
          this.input.editingOpen = ''
          this.input.editingClose = ''
          this.input.days.splice(0)

          this.state = null

          if (this.editing.length > 24) {
            this.editing.splice(24)
            this.$store.dispatch('addMessage', {
              type: 'error',
              title: 'Limitation',
              message: 'You can only store a maximum of 24 timings.'
            })
          }

          this.update()
        }
      },
      onFocusDay() {
        if (this.state === 'days') {
          this.state = null
        } else {
          this.state = 'days'
        }
      },
      onFocusOpen() {
        this.state = 'open'
      },
      onFocusClose() {
        this.state = 'close'
      },
      update() {
        this.$emit('input', this.editing)
      },
      onInputDay(day) {
        const index = _.findIndex(this.input.days, d => d === day)
        if (index > -1) {
          this.input.days.splice(index, 1)
        } else {
          this.input.days.push(day)
        }
      },
      onInputOpen(time) {
        this.input.editingOpen = time
        this.state = null
      },
      onInputClose(time) {
        this.input.editingClose = time
        this.state = null
      },
      onInputChange() {
        const open = parseNaturalText(this.input.editingOpen)
        this.input.open = open.time
        this.input.editingOpen = open.text

        const close = parseNaturalText(this.input.editingClose)
        this.input.close = close.time
        this.input.editingClose = close.text
      },
      onContainsDay(day) {
        return _.some(this.input.days, d => d === day)
      },
    },
  }
</script>

<style scoped lang="less">
  .EditorHours {
    font-size: 15px;
  }

  .DayPopover {
    bottom: 0;
    left: 0;
  }

  .TimeInput {
    padding: 4px 8px 4px;

    height: 40px;
    display: flex;
    flex-direction: column;

    border: 1px solid #FFF;

    &.Warning {
      border: 1px solid #EC152C;
    }

    * {
      display: block;
    }

    label {
      font-size: 9px;
    }

    input {
      outline: none;
      border: none;
      width: 100%;

      flex-grow: 1;
      font-size: 15px;

      border-radius: 1px;
    }
  }
</style>
