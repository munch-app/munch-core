<template>
  <div class="border-3 border bg-white index-elevation">
    <div class="p-16 hr-bot none desktop-b">
      <h4>{{place.name}}</h4>
    </div>

    <div class="p-0-16">
      <div class="mtb-16" v-if="place.price && place.price.perPax">
        <h5>Price Per Person</h5>
        <p>~${{place.price.perPax.toFixed(1)}}</p>
      </div>

      <div class="mtb-16" v-if="place.phone">
        <h5>Phone</h5>
        <p>{{place.phone}}</p>
      </div>

      <div class="mtb-16" v-if="place.hours && place.hours.length > 0">
        <h5>Opening Hours</h5>
        <opening-hours :hours="place.hours"/>
      </div>

      <div class="mtb-16">
        <h5>Address</h5>
        <p>{{place.location.address}}</p>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import OpeningHours from "../utils/hour/OpeningHours";

  export default {
    name: "PlaceAside",
    components: {OpeningHours},
    props: {
      place: {
        type: Object,
        required: true
      },
    },
    computed: {
      ...mapGetters('account', ['isLoggedIn']),
    },
    methods: {}
  }
</script>

<style scoped lang="less">
  p {
    font-size: 16px;
  }

  h5 {
    color: #444;
  }
</style>
