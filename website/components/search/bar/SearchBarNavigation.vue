<template>
  <div class="Navigation flex-column index-top-elevation">

  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarNavigation",
    methods: {
      onNearby() {
        this.$store.dispatch('filter/location', {type: 'Anywhere'})
        this.$store.dispatch('search/start')

        this.$track.search(`Search - Navigation`, this.$store.getters['search/locationType'])
        this.$emit('on-blur')
      },
      onAnywhere() {
        this.$store.dispatch('filter/location', {type: 'Anywhere'})
        this.$store.dispatch('search/start')

        this.$track.search(`Search - Navigation`, this.$store.getters['search/locationType'])
        this.$emit('on-blur')
      },
      onFeed() {
        this.$router.push({path: '/feed/images'})
        this.$emit('on-blur')
      },
    },
    computed: {
      ...mapGetters(['isStaging']),
      route() {
        return this.$route.name
      },
    }
  }
</script>

<style scoped lang="less">
  .Navigation {
    margin: 10px 10px 10px 16px;

    @media (max-width: 767.98px) {
      margin-left: 24px;
    }
  }

  .NavigationItem {
    padding: 10px 0;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);
    display: flex;

    &:hover {
      cursor: pointer;
    }
  }
</style>
