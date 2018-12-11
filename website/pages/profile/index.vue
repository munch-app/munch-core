<template>
  <div class="zero">
    <section class="Profile container mt-24 flex-align-center">
      <h1>{{profile.name}}</h1>
    </section>

    <div class="container flex mt-16">
      <button v-for="tab in tabs" :key="tab" class="mr-24" :class="selected === tab ? 'secondary' : 'border'"
              @click="selected = tab">
        {{tab}}
      </button>
    </div>

    <div class="container">
      <profile-saved-place-section v-if="selected === 'Places'"/>
      <profile-search-preference-section v-if="selected === 'Preferences'"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import ProfileSavedPlaceSection from "../../components/profile/ProfileSavedPlaceSection";
  import ProfileSearchPreferenceSection from "../../components/profile/ProfileSearchPreferenceSection";

  export default {
    components: {ProfileSearchPreferenceSection, ProfileSavedPlaceSection},
    data() {
      return {
        selected: 'Places',
        tabs: ['Places', 'Preferences']
      }
    },
    computed: {
      ...mapGetters('user', ['profile'])
    },
    mounted() {
      if (!process.client) return

      const authenticator = require('~/services/authenticator').default
      if (!this.$store.state.user.profile && !authenticator.isLoggedIn()) {
        this.$router.push({path: '/authenticate'})
      }
    },
    methods: {
      onSignOut() {
        this.$store.dispatch('user/logout')
        this.$router.push({path: '/'})
      }
    }
  }
</script>

<style scoped lang="less">
  .ProfileImage {
    border-radius: 50%;

    @media (max-width: 575.98px) {
      display: none;
    }
  }
</style>
