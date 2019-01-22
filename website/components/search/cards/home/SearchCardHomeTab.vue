<template>
  <div>
    <section class="HomeTab container-remove-gutter" :class="current.id">
      <div class="Overlay wh-100">
        <div class="container">
          <h1>{{salutation}}, {{name}}.</h1>
          <p v-if="!isLoggedIn">(Not Samantha? Create an account <span @click="onCreateAccount"
                                                                       class="text-underline hover-pointer">here</span>.)
          </p>
          <div class="FeatureList TopSpacing flex-row container-remove-gutter">
            <div>
              <div class="gutter"/>
            </div>
            <div class="mr-24 hover-pointer" v-for="(feature, i) in features" :key="feature.id" @click="index = i">
              <h4 class="Feature">{{feature.name}}</h4>
              <div :class="{Selected: feature.id === current.id}"></div>
            </div>
            <div>
              <div class="gutter"/>
            </div>
          </div>

          <div class="FeatureLine"/>
          <h5 class="TopSpacing">{{current.message}}</h5>

          <div class="ActionBar TopSpacing bg-white border-3 w-100 flex-align-center hover-pointer"
               @click.capture.stop.prevent="onClick()">
            <simple-svg class="wh-24px mlr-16" fill="black" :filepath="current.icon"/>
            <h4 class="text-ellipsis-1l">{{current.hint}}</h4>
          </div>
        </div>
      </div>
    </section>

    <section class="Feed border-3 mt-48 relative">
      <div class="wh-100 flex-center">
        <div class="Padded p-24 flex-column flex-align-center">
          <h2 class="white">The sexiest thing you’ll see on the Internet - ever.</h2>
          <h5 class="white mt-8 mb-32">(Warning! Not Safe For Waist. NSFW)</h5>
          <button @click="onFeed()" class="secondary-outline">Explore Feed</button>
        </div>
      </div>
    </section>


    <search-filter-between-dialog v-if="isBetween" @cancel="isBetween = false"/>
    <search-filter-area-dialog v-if="isLocation" @cancel="isLocation = false"/>
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
      current() {
        return this.features[this.index]
      },
    },
    data() {
      return {
        index: 0,
        isBetween: false,
        isLocation: false,
        features: [
          {
            name: 'EatBetween',
            id: 'EatBetween',
            message: 'Enter everyone’s location and we’ll find the most ideal spot for a meal together.',
            hint: 'Enter Locations',
            icon: require('~/assets/icon/search/between.svg'),
          },
          {
            name: 'Search',
            id: 'Search',
            message: 'Search anything on Munch and we’ll give you the best recommendations.',
            hint: 'Search e.g. Italian in Orchard',
            icon: require('~/assets/icon/search/search.svg'),
          },
          {
            name: 'Neighbourhoods',
            id: 'Location',
            message: 'Enter a location and we’ll tell you what’s delicious around.',
            hint: 'Search Location',
            icon: require('~/assets/icon/search/search.svg'),
          },
        ]
      }
    },
    methods: {
      onClick() {
        switch (this.current.id) {
          case "EatBetween":
            this.isBetween = true
            break

          case "Location":
            this.isLocation = true
            break

          case "Search":
            this.$store.dispatch('filter/location', {type: 'Anywhere'})
            this.$store.dispatch('search/start')

            document.getElementById('globalSearchBar').focus()
            break
        }
      },
      onCreateAccount() {
        this.$store.commit('focus', 'Login')
      },
      onFeed() {
        this.$router.push({path: '/feed/images'})
      }
    }
  }
</script>

<!--suppress CssUnknownTarget -->
<style scoped lang="less">
  .HomeTab {
    margin-top: -86px;
    background: no-repeat center/cover;

    * > {
      color: white;
    }
  }

  .EatBetween {
    @media (max-width: 992px) {
      background-image: url('~/assets/img/search/home_eb.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~/assets/img/search/home_eb.jpg');
    }
  }

  .Search {
    @media (max-width: 992px) {
      background-image: url('~/assets/img/search/home_search.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~/assets/img/search/home_search.jpg');
    }
  }

  .Location {
    @media (max-width: 992px) {
      background-image: url('~/assets/img/search/home_location.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~/assets/img/search/home_location.jpg');
    }
  }

  .Feed {
    background: no-repeat center/cover;

    @media (max-width: 992px) {
      background-image: url('~/assets/img/search/home_feed.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~/assets/img/search/home_feed.jpg');
    }
  }

  .Padded {
    @media (min-width: 768px) {
      padding: 32px 24px;
    }

    @media (min-width: 1200px) {
      padding: 48px 24px;
    }
  }

  .TopSpacing {
    margin-top: 16px;

    @media (min-width: 768px) {
      margin-top: 24px;
    }
  }

  .Overlay {
    background: rgba(0, 0, 0, 0.5);
    padding: 80px 0 32px 0;

    @media (min-width: 768px) {
      padding: 80px;
    }

    @media (min-width: 992px) {
      padding: 120px;
    }

    @media (min-width: 1200px) {
      padding: 180px;
    }

    @media (min-width: 1600px) {
      padding: 180px 320px;
    }
  }

  .FeatureList {
    overflow-x: auto;
  }

  .Feature {
    height: 36px;
  }

  .Selected {
    height: 2px;
    background: white;
  }

  .FeatureLine {
    height: 1px;
    background: rgba(255, 255, 255, 0.85);
  }

  .ActionBar {
    height: 40px;

    @media (min-width: 768px) {
      height: 48px;
    }

    * {
      color: rgba(0, 0, 0, 0.75);
    }
  }
</style>
