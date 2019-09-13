<template>
  <div class="OpeningHours">
    <div class="hover-pointer text-ellipsis-1l flex-align-center">
      <div class="text-uppercase">{{todayDay}}:</div>
      <div class="ml-4">{{todayText}}</div>
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
    computed: {
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
        return this.hours
          .filter(h => h.day === day)
          .sort(h => h.open)
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
