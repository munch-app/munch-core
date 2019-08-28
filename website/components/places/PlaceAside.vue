<template>
  <div class="border-3 border bg-white index-elevation">
    <div class="p-16-24 hr-bot desktop">
      <h4>{{place.name}}</h4>
    </div>

    <div class="p-16-24 Info desktop">
      <div v-if="place.price && place.price.perPax">
        <h5>Price Per Person</h5>
        <p>~${{place.price.perPax.toFixed(1)}}</p>
      </div>

      <div v-if="place.phone">
        <h5>Phone</h5>
        <p>{{place.phone}}</p>
      </div>

      <div v-if="place.hours.length > 0">
        <h5>Opening Hours</h5>
        <place-hour-list :hours="place.hours"/>
      </div>

      <div>
        <h5>Address</h5>
        <div class="text">{{place.location.address}}</div>
      </div>
    </div>

    <div class="BottomBar flex-end">
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import PlaceHourList from "./PlaceHourList";

  export default {
    name: "PlaceAside",
    components: {PlaceHourList},
    props: {
      place: {
        type: Object,
        required: true
      },
    },
    data() {
      return {
        show: {
          more: false
        }
      }
    },
    computed: {
      ...mapGetters('account', ['isLoggedIn']),
    },
    methods: {
      onShare() {
        this.show.more = false

        const url = window.location.href
        this.$copyText(url).then(() => {
          this.$store.dispatch('addMessage', {title: 'Copied URL!'})
          this.$track.share('RIP', 'Copied URL')
        }, (e) => {
          this.$store.dispatch('addError', e)
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .Info {
    margin-top: -24px;

    > div {
      margin-top: 24px;
    }
  }

  .BottomBar {
    padding: 16px 16px;

    @media (max-width: 992px) {
      padding-top: 6px;
      padding-bottom: 6px;
    }
  }
</style>
