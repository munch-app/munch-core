<template>
  <div>
    <div class="mb-24">
      <div v-if="isLoggedIn">
        <h2>{{salutation}}, {{name}}. Meeting someone for a meal?</h2>
      </div>
      <div v-else>
        <h2>{{salutation}}, Samantha. Meeting someone for a meal?</h2>
        <p class="hover-pointer" @click="onCreateAccount">(Not Samantha? Create an account
          <span class="text-underline ">here</span>.)
        </p>
      </div>

    </div>

    <div>
      <div class="FeatureCardList flex-wrap relative">
        <div class="FeatureCard" v-for="feature in features" :key="feature.id">
          <div class="border-4 overflow-hidden h-100" :class="feature.id" @click="onClick(feature)">
            <div class="bg-overlay p-24 zero h-100">
              <h1 class="white">{{feature.name}}</h1>
              <h5 class="white mt-8">{{feature.message}}</h5>
              <button class="mt-24 secondary-outline">{{feature.button}}</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import HorizontalScrollView from "../../../core/HorizontalScrollView";
  import SearchBarFilterBetween from "../../SearchBarFilterBetween";
  import SearchFilterBetweenDialog from "../../dialog/SearchFilterBetweenDialog";
  import SearchFilterAreaDialog from "../../dialog/SearchFilterAreaDialog";

  export default {
    name: "SearchCardHomeTab",
    components: {SearchFilterAreaDialog, SearchFilterBetweenDialog, SearchBarFilterBetween, HorizontalScrollView},
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
      },
    },
    data() {
      return {
        features: [
          {
            name: 'EatBetween',
            id: 'EatBetween',
            message: 'Enter everyone’s location and we’ll find the most ideal spot for a meal together.',
            button: 'Try EatBetween',
          },
          {
            name: 'Feed',
            id: 'Feed',
            message: 'The sexiest thing you’ll see on the Internet - ever. (Warning! Not Safe For Waist. NSFW)',
            button: 'Explore Feed',
          },
        ]
      }
    },
    methods: {
      onClick(feature) {
        switch (feature.id) {
          case "EatBetween":
            this.$router.push({path: `/search/filter/between`})
            break

          case "Feed":
            this.$track.click('search_card_click', 'HomeTab_2018-11-29')
            this.$router.push({path: '/feed/images'})
            break
        }
      },
      onCreateAccount() {
        this.$store.commit('focus', 'Login')
      },
    }
  }
</script>

<!--suppress CssUnknownTarget -->
<style scoped lang="less">
  .EatBetween {
    background: no-repeat center/cover;

    @media (max-width: 992px) {
      background-image: url('~assets/img/search/home_eb.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~assets/img/search/home_eb.jpg');
    }
  }

  .Feed {
    background: no-repeat center/cover;

    @media (max-width: 992px) {
      background-image: url('~assets/img/search/home_feed.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~assets/img/search/home_feed.jpg');
    }
  }
</style>

<style scoped lang="less">
  .FeatureCardList {
    margin: -12px;

    height: 100%;
    align-items: stretch;
  }

  .FeatureCard {
    padding: 12px;

    flex: 0 0 50%;
    max-width: 50%;

    @media (max-width: 648px) {
      flex: 0 0 100%;
      max-width: 100%;
    }
  }
</style>
