<template>
  <div class="border-3 border bg-white index-elevation">
    <div class="p-16-24 hr-bot desktop">
      <h3>{{place.name}}</h3>
    </div>

    <div class="p-16-24 Info desktop">
      <div v-if="place.price && place.price.perPax">
        <h6>Price Per Person</h6>
        <p>~${{place.price.perPax.toFixed(1)}}</p>
      </div>

      <div v-if="place.phone">
        <h6>Phone</h6>
        <p>{{place.phone}}</p>
      </div>

      <div v-if="place.hours.length > 0">
        <h6>Opening Hours</h6>
        <place-hour-list :hours="place.hours"/>
      </div>

      <div>
        <h6>Address</h6>
        <div class="text">{{place.location.address}}</div>
      </div>
    </div>

    <div class="BottomBar flex-end">
      <div @click="onMore" class="p-8 mr-8">
        <simple-svg class="wh-24px" fill="black" :filepath="require('~/assets/icon/place/more.svg')"/>
      </div>
    </div>

    <div>
      <portal to="dialog-action-sheet" v-if="show.more">
        <div @click="onSuggest">
          Suggest Edits
        </div>
        <div @click="onShare">
          Share
        </div>
        <hr>
        <div @click="show.more = false">
          Close
        </div>
      </portal>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import PlaceHourList from "./PlaceHourList";

  export default {
    name: "PlaceFloatingPanel",
    components: {PlaceHourList},
    props: {
      place: {
        type: Object,
        required: true
      },
      user: {
        type: Object,
        required: true
      }
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
      onMore() {
        this.show.more = true
      },
      onSuggest() {
        if (this.isLoggedIn) {
          window.open(`/places/suggest?placeId=${this.place.placeId}`, '_blank');
        } else {
          this.show.more = false
          this.$store.commit('focus', 'Login')
        }
      },
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
