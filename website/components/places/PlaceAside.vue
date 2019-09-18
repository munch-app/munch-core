<template>
  <div class="border-3 border bg-white index-elevation">
    <div class="p-16 hr-bot none desktop-b">
      <h4>{{place.name}}</h4>
    </div>

    <div class="p-0-16">
      <div class="mtb-16" v-if="place.price && place.price.perPax">
        <h6>Price Per Person</h6>
        <p>~${{place.price.perPax.toFixed(1)}}</p>
      </div>

      <div class="mtb-16" v-if="place.phone">
        <h6>Phone</h6>
        <p>{{place.phone}}</p>
      </div>

      <div class="mtb-16" v-if="place.hours && place.hours.length > 0">
        <h6>Opening Hours</h6>
        <opening-hours :hours="place.hours"/>
      </div>

      <div class="mtb-16">
        <h6>Address</h6>
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
