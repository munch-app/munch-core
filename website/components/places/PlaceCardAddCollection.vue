<template>
  <div class="CollectionBtn w-100 index-content-overlay flex-row-end">
    <div @click.prevent.stop="onClickAdd">
      <simple-svg class="AddIcon" fill="white" :filepath="require('~/assets/icon/place/add.svg')"/>
    </div>

    <profile-collection-add-place :place="place" v-if="showAddToCollection" @on-close="showAddToCollection = false"/>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import ProfileCollectionAddPlace from "../profile/ProfileCollectionAddPlace";

  export default {
    name: "PlaceCardAddCollection",
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
  .CollectionBtn {
    .AddIcon {
      width: 30px;
      height: 30px;
      padding: 6px;
    }
  }
</style>
