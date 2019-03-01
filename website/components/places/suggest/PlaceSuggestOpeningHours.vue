<template>
  <div>
    <h3>Hours</h3>
    <div @click="redirect">
      <p v-for="(range, day) in group.dayHours">
        {{day + " : " + range}}
      </p>
    </div>

    <portal to="dialog-full" v-if="show.more">
      <div class="index-1 BetweenDialog bg-white elevation-1 p-16-24">
        <h3>Select days with incorrect hours</h3>
        <div class="mt-24">
          <div v-for="(range, day) in group.dayHours">
            <div class="hover-pointer mb-16 flex-justify-between">
              <div class="flex-align-center">
                <div class="checkbox mr-16" :class="{selected: isSelected(day)}"></div>
                <div class="text TimeRange-Text" v-html="day"></div>
              </div>
              <div class="text TimeRange-Text" v-html="range"></div>
            </div>
            <div class="flex-justify-between mb-48" v-if="">
              <div class="flex-align-center">
                <vue-time-picker format="HH:mm A" class="mr-16"></vue-time-picker>
                <vue-time-picker format="HH:mm A"></vue-time-picker>
              </div>
            </div>
          </div>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";
  import {Day, days, Hour, HourGroup} from '../hour-group'
  import VueTimePicker from "../../../node_modules/vue2-timepicker"

  export default {
    name: "PlaceSuggestOpeningHours",
    components: {InputText, VueTimePicker},
    props: {
      payload: {
        type: Object,
        twoWay: true
      }
    },
    data() {
      const group = new HourGroup(this.payload.place.hours.map((h) => new Hour(h.day, h.open, h.close)))

      return {
        Day: Day,
        days: days,
        group: group,
        timeRange: group.todayTimeRange(),
        show: {
          more: false
        },
        selectedDays: []
      }
    },
    methods: {
      redirect() {
        this.show.more = true
      },
      isSelected(day) {

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
    margin-top: 20vh;
    @media (min-width: 768px) {
      width: 700px;
    }
  }
</style>
