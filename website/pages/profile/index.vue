<template>
  <div class="zero">
    <section class="Profile container mt-24 flex-align-center">
      <h1>{{profile.name}}</h1>
    </section>

    <div class="container flex mt-16">
      <button v-for="tab in tabs" :key="tab.id" class="mr-24" :class="selected === tab.id ? 'secondary' : 'border'"
              @click="selected = tab.id">
        {{tab.name}}
      </button>
    </div>

    <div class="container">
      <profile-saved-place-section v-if="selected === 'places'"/>
      <profile-search-preference-section v-if="selected === 'preferences'"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import ProfileSavedPlaceSection from "../../components/account/ProfileSavedPlaceSection";
  import ProfileSearchPreferenceSection from "../../components/account/ProfileSearchPreferenceSection";

  export default {
    components: {ProfileSearchPreferenceSection, ProfileSavedPlaceSection},
    data() {
      return {
        selected: 'places',
        tabs: [{
          id: 'places',
          name: 'Your Places'
        }, {
          id: 'preferences',
          name: 'Preferences'
        }]
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
