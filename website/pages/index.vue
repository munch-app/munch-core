<template>
  <div class="zero-spacing LandingPage">
    <section class="EatBetween">
      <landing-eat-between/>
    </section>

    <section v-if="false" class="Profile">
      <div class="container">
        <div class="Greeting">
          <h1>{{salutation}}
            <no-ssr><span class="Name">, {{displayName || 'Samantha'}}</span></no-ssr>
          </h1>
          <no-ssr>
            <div class="text Login" v-if="!isLoggedIn">(not your name?
              <a @click="$store.commit('focus', 'Login')" class="LoginButton"><span
                class="primary-500 weight-600">Log In</span></a>
              now!)
            </div>
          </no-ssr>
        </div>
        <p>Discover the best wherever & whenever</p>
      </div>
    </section>

    <section class="Timing">
      <div class="container">
        <h2>Here & Now</h2>
        <p>The best Breakfast spots near you.</p>
      </div>

      <horizontal-scroll-view class="TimingList" :items="[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18]" :map-key="i => i">
        <template slot-scope="{item}">
          <div class="SimpleCard"></div>
        </template>
      </horizontal-scroll-view>
    </section>

    <section class="Search">
      <div class="container">
        <h2>Search</h2>
      </div>
      <div class="container" @click="onFocus">
        <search-bar class="SearchBar"/>
        <div class="SearchBarContainer"></div>
      </div>
    </section>

    <section class="Collection">
      <div class="container">
        <h2>In the know</h2>
        <p>What’s hot and edible</p>
      </div>

      <landing-collection class="LandingCollection" :collections="collections"/>
    </section>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import LandingLocationList from "../components/landing/LandingLocationList";
  import HorizontalScrollView from "../components/core/HorizontalScrollView";
  import LandingCollection from "../components/landing/LandingCollection";
  import LandingEatBetween from "../components/landing/LandingEatBetween";
  import SearchBar from "../components/search/SearchBar";

  export default {
    components: {SearchBar, LandingEatBetween, LandingCollection, HorizontalScrollView, LandingLocationList},
    asyncData({$axios}) {
      return $axios.$post('/api/landing')
        .then(({data}) => {
          return {
            collections: data.collections
          }
        })
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn', 'displayName']),
      salutation() {
        const date = new Date()
        const totalMinutes = (date.getHours() * 60) + date.getMinutes()
        if (totalMinutes >= 300 && totalMinutes < 720) return 'Good Morning'
        if (totalMinutes >= 720 && totalMinutes < 1020) return 'Good Afternoon'
        else return 'Good Evening'
      }
    },
    methods: {
      onFocus() {
        this.$router.push({path: '/search'})
        this.$store.commit('focus', 'Suggest')
      }
    }
  }
</script>

<style scoped lang="less">
  .LandingPage {
    padding-bottom: 24px;
  }

  section {
    margin-bottom: 40px;
  }

  section.Profile {
    .Greeting {
      display: flex;
      align-items: flex-end;
      justify-content: flex-start;

      @media (max-width: 575.98px) {
        .Login, .Name {
          display: none;
        }
      }

      h1 {
        margin-right: 24px;
      }

      .Login {
        line-height: 30px;
        .LoginButton {
          &:hover {
            cursor: pointer;
            text-decoration: underline;
          }
        }
      }
    }
  }

  section.Timing {
    .TimingList {
      margin-top: 16px;
      height: 72px;
    }

    .SimpleCard {
      background: black;
      width: 160px;
      height: 72px;
      border-radius: 4px;
    }
  }

  section.Search {
    .SearchBarContainer {
      position: absolute;

      left: 0;
      right: 0;
      height: 40px;
      margin-top: -40px;
    }

    .SearchBar {
      margin-top: 16px;
      max-width: 500px;
    }
  }

  section.Collection {
    .LandingCollection {
      margin-top: 16px;
    }
  }
</style>
