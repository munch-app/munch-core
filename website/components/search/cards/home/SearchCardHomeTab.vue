<template>
  <div>
    <h2>{{salutation}}, {{name}}. Find the perfect spot on Munch.</h2>
    <p class="b-a75" v-if="!isLoggedIn">(Not Samantha? Create an account <span class="p500">here</span>.)</p>

    <horizontal-scroll-view class="FeatureList container-remove-gutter mt-16" :items="features" :map-key="a => a.id"
                            :padding="24">
      <template slot-scope="{item}">
        <div class="FeatureCard bg-whisper100 border-4 hover-pointer flex-center text-center" @click="onClick(item)">
          <h5>{{item.name}}</h5>
        </div>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HorizontalScrollView from "../../../core/HorizontalScrollView";

  export default {
    name: "SearchCardHomeTab",
    components: {HorizontalScrollView},
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
        features: [
          {name: 'Dining with Friends', id: 'EatBetween', image: ''},
          {name: 'Food Inspiration', id: 'Feed', image: ''},
          {name: 'Discover by Neighborhood', id: 'Location', image: ''},
        ]
      }
    },
    methods: {
      onClick({id}) {
        switch (id) {
          case "EatBetween":
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

    padding: 24px;
  }
</style>
