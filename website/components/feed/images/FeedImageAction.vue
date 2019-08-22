<template>
  <aside>
    <nuxt-link :to="`/places/${place.placeId}`" class="lh-0"
               @click.native.capture="$track.view(`RIP`, 'Feed - Instagram')">
      <button class="primary ml-16 small">Open Place</button>
    </nuxt-link>
  </aside>
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    name: "FeedImageAction",
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    methods: {
    }
  }
</script>

<style scoped lang="less">

</style>
