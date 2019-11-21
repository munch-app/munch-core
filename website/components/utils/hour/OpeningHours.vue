<template>
  <div class="OpeningHours hover-pointer" @click="expanded = !expanded">
    <div v-if="!expanded" class="text-ellipsis-1l flex">
      <div class="text-uppercase text-nowrap">{{todayDay}}:</div>
      <div class="ml-4">{{todayText}}</div>
      <div class="ml-8">
        <simple-svg class="wh-16px" fill="black" :filepath="require('~/assets/icon/icons8-expand-arrow.svg')"/>
      </div>
    </div>

    <div v-if="expanded" class="monospace">
      <div v-for="type in dayTypes" :key="type">
        <div class="mt-4 flex">
          <div class="flex-no-shrink">{{type}}:</div>
          <div class="ml-16">{{toText(days[type])}}</div>
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

  export default {
    name: "OpeningHours",
    props: {
      hours: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        dayTypes: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
        expanded: false
      }
    },
    computed: {
      days() {
        const days = {'MON': [], 'TUE': [], 'WED': [], 'THU': [], 'FRI': [], 'SAT': [], 'SUN': []}
        for (const hour of this.hours) {
          days[hour.day].push(hour)
          days[hour.day].sort(h => h.open)
        }
        return days
      },
      todayDay() {
        const today = new Date()
        switch (today.getDay()) {
          case 1:
            return 'MON'
          case 2:
            return 'TUE'
          case 3:
            return 'WED'
          case 4:
            return 'THU'
          case 5:
            return 'FRI'
          case 6:
            return 'SAT'
          case 7:
          case 0:
            return 'SUN'
          default:
            return 'OTH'
        }
      },
      todayText() {
        const day = this.todayDay
        return this.toText(this.hours
          .filter(h => h.day === day))
      },
    },
    methods: {
      toText(hours) {
        if (!hours.length) {
          return 'Closed'
        }

        return hours.sort(h => h.open)
          .map(mapTimeRange)
          .join(", ")
      }
    }
  }
</script>

<style scoped lang="less">
  .OpeningHours {
    font-size: 15px;
    font-weight: 500;
  }
</style>
