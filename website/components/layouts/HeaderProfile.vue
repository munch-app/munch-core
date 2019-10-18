<template>
  <div class="flex-no-shrink hover-pointer" v-if="isLoggedIn" @click="$store.commit('toggleFocus', 'HeaderMenu')">
    <profile-pic class="wh-40px" :profile="profile"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import CdnImg from "../utils/image/CdnImg";
  import ProfilePic from "../profile/ProfilePic";

  export default {
    name: "HeaderProfile",
    components: {ProfilePic, CdnImg},
    computed: {
      ...mapGetters('account', ['isLoggedIn', 'name', 'profile']),
    },
    data() {
      return {
        failed: false
      }
    }
  }
</script>
