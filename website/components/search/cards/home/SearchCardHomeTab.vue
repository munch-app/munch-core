<template>
  <div>
    <h2>{{salutation}}, {{name}}. Find the perfect spot on Munch.</h2>
    <p class="b-a75" v-if="!isLoggedIn">(Not Samantha? Create an account <span class="p500">here</span>.)</p>

    <horizontal-scroll-view class="FeatureList container-remove-gutter mt-16" :items="features" :map-key="a => a.id"
                            :padding="24">
      <template slot-scope="{item}">
        <div class="FeatureCard border-3 overflow-hidden hover-pointer" @click="onClick(item)"
             :style="{backgroundImage: `url('${item.image}')`}">
          <div class="Overlay wh-100 flex-center text-center">
            <h4 class="white">{{item.name}}</h4>
          </div>
        </div>
      </template>
    </horizontal-scroll-view>

    <search-filter-between-dialog v-if="isBetween" @cancel="isBetween = false"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HorizontalScrollView from "../../../core/HorizontalScrollView";
  import SearchBarFilterBetween from "../../SearchBarFilterBetween";
  import SearchFilterBetweenDialog from "../../dialog/SearchFilterBetweenDialog";

  export default {
    name: "SearchCardHomeTab",
    components: {SearchFilterBetweenDialog, SearchBarFilterBetween, HorizontalScrollView},
    computed: {
      ...mapGetters('user', ['isLoggedIn', 'displayName']),
      name() {
        return this.displayName || 'Samantha'
      },
      salutation() {
        const date = new Date()
        const totalMinutes = (date.getHours() * 60) + date.getMinutes()
        if (totalMinutes >= 300 && totalMinutes < 720) return 'Good Morning'
        if (totalMinutes >= 720 && totalMinutes < 1020) return 'Good Afternoon'
        else return 'Good Evening'
      }
    },
    data() {
      return {
        isBetween: false,
        features: [
          {
            name: 'Dining with Friends',
            id: 'EatBetween',
            image: require('~/assets/img/search/home-tab-between.jpg')
          },
          {
            name: 'Food Inspiration',
            id: 'Feed',
            image: require('~/assets/img/search/home-tab-feed.jpg')
          },
          {
            name: 'Discover by Neighborhood',
            id: 'Location',
            image: require('~/assets/img/search/home-tab-location.jpg')
          },
        ]
      }
    },
    methods: {
      onClick({id}) {
        switch (id) {
          case "EatBetween":
            this.isBetween = true
            break

          case "Location":
            this.$router.push({path: '/locations'})
            break

          case "Feed":
            this.$router.push({path: '/feed/images'})
            break
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .FeatureList {
    height: 88px;
  }

  .FeatureCard {
    height: 88px;
    width: 180px;

    background: no-repeat center/cover;
  }

  .Overlay {
    background: rgba(0, 0, 0, 0.4);
    padding: 24px;
  }
</style>
