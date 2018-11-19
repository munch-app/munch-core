<template>
  <div class="Navigation flex index-top-elevation">
    <div @click="onNavigationSearch" class="NavigationItem" :class="{Selected: route.startsWith('search')}">
      SEARCH
    </div>
    <div @click="onNavigationFeedImage" class="NavigationItem" :class="{Selected: route === 'feed-images'}">
      FEED
    </div>
    <div v-if="isStaging" @click="onNavigationFeedArticle" class="NavigationItem"
         :class="{Selected: route === 'feed-articles'}">ARTICLE FEED
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarNavigation",
    data() {
      return {items: ['SEARCH', 'FEED', 'ARTICLE FEED']}
    },
    methods: {
      onNavigationSearch() {
        if (!this.$store.state.search.query) {
          this.$store.dispatch('filter/location', {type: 'Anywhere'})
          this.$store.dispatch('search/start')

          this.$track.search(`Search - Navigation`, this.$store.getters['search/locationType'])
        } else if (this.$route.path !== '/search') {
          this.$router.push({path: '/search'})
        }
        this.$emit('on-blur')
      },
      onNavigationFeedImage() {
        this.$router.push({path: '/feed/images'})
        this.$emit('on-blur')
      },
      onNavigationFeedArticle() {
        this.$router.push({path: '/feed/articles'})
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
    margin: 16px 10px 16px 16px;
    min-width: 355px;

    @media (max-width: 767.98px) {
      margin-left: 24px;
    }
  }

  @Secondary400: #227190;
  .NavigationItem {
    text-decoration: initial !important;
    padding: 10px 16px;
    margin-right: 14px;

    border-radius: 3px;
    border: 1px solid rgba(0, 0, 0, 0.15);

    font-weight: 600;
    font-size: 12px;
    color: rgba(0, 0, 0, .75);

    line-height: 1.5;
    height: 38px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: clip;

    &:hover, &.Selected {
      cursor: pointer;
      color: white;
      background: @Secondary400;
      border: 1px solid @Secondary400;
    }
  }
</style>
