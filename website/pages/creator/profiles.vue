<template>
  <div class="container-1200 mtb-32">
    <h1 class="mb-32">Your creator profiles</h1>

    <div v-if="profiles.length > 0" class="CardList flex-wrap">
      <div class="Card" v-for="profile in profiles" :key="profile.creatorId" @click="onSelectProfile(profile)">
        <div class="p-16-24 border border-3 elevation-hover-1 hover-pointer">
          <h4>{{profile.name}}</h4>
        </div>
      </div>
    </div>
    <div v-else>
      <h5 class="error">You don't have any creator profile.</h5>
    </div>
  </div>
</template>

<script>
  export default {
    layout: 'creator',
    head() {
      return {title: 'My Profiles · Munch Creator'}
    },
    asyncData({$api}) {
      return $api.get('/users/creators/profiles')
        .then(({data}) => {
          return {profiles: data}
        })
    },
    methods: {
      onSelectProfile(profile) {
        this.$store.commit('creator/setCreator', {profile})
        this.$router.push({path: '/creator/contents'})
      }
    }
  }
</script>

<style scoped lang="less">
  .CardList {
    margin: -18px;
  }

  .Card {
    flex: 0 0 100%;
    max-width: 100%;

    padding: 12px 18px;

    @media (min-width: 768px) {
      flex: 0 0 50%;
      max-width: 50%;
    }

    @media (min-width: 1200px) {
      flex: 0 0 33.33333%;
      max-width: 33.33333%;
    }
  }
</style>
