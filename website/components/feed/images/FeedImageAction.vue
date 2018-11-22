<template>
  <aside>
    <button @click="onClickAdd" class="border small">Save</button>
    <nuxt-link :to="`/places/${place.placeId}`" class="lh-0" @click.native.capture="$track.view(`RIP`, 'Feed - Instagram')">
      <button class="primary ml-16 small">Open Place</button>
    </nuxt-link>

    <no-ssr>
      <profile-collection-add-place :place="place" v-if="showAddToCollection" @on-close="showAddToCollection = false"/>
    </no-ssr>
  </aside>
</template>

<script>
  import {mapGetters} from "vuex";
  import ProfileCollectionAddPlace from "../../profile/ProfileCollectionAddPlace";

  export default {
    name: "FeedImageAction",
    components: {ProfileCollectionAddPlace},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
    },
    methods: {
      onClickAdd() {
        if (this.isLoggedIn) {
          this.showAddToCollection = true
        } else {
          this.$store.commit('focus', 'Login')
        }
      }
    }
  }
</script>

<style scoped lang="less">

</style>
