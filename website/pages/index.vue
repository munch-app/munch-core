<template>
  <div class="zero-spacing LandingPage">
    <section class="Profile">
      <div class="container">
        <div class="Greeting">
          <h1>{{salutation}}<no-ssr><span class="Name">, {{displayName || 'Samantha'}}</span></no-ssr></h1>
          <no-ssr>
            <div class="textLogin" v-if="!isLoggedIn">(not your name?
              <a @click="$store.commit('focus', 'Login')" class="LoginButton"><span
                class="Primary500 weight-600">Log In</span></a>
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

      <div class="container">
        <div class="TimingList">
          <div class="Card" v-for="i in [0,1,2,4]" :key="i">
            <div></div>
          </div>
        </div>
      </div>
    </section>

    <section class="Location">
      <div class="container">
        <h2>Day in, day out</h2>
        <p>Your favourite places - rediscovered.</p>
      </div>
      <landing-location-list class="container"/>
    </section>

    <section class="Collection">
      <div class="container">
        <h2>In the know</h2>
        <p>What’s hot and edible</p>
      </div>

      <div class="container">
        <div class="CollectionList">
          <div class="Card" v-for="collection in collections" :key="collection.collectionId">
            <user-place-collection-card :collection="collection"/>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import LandingLocationList from "../components/landing/LandingLocationList";
  import UserPlaceCollectionCard from "../components/collections/UserPlaceCollectionCard";

  export default {
    components: {UserPlaceCollectionCard, LandingLocationList},
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
    }
  }
</script>

<style scoped lang="less">
  .LandingPage {
    margin-top: 24px;
    padding-bottom: 64px;
  }

  section {
    margin-top: 24px;
    margin-bottom: 32px;
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
      margin-left: -24px;
      margin-right: -24px;

      display: flex;
      overflow-x: scroll;
      -webkit-overflow-scrolling: touch;

      .Card {
        margin-right: 24px;
        flex-shrink: 0;

        > div {
          background: black;
          width: 160px;
          height: 72px;
          border-radius: 4px;
        }
      }

      .Card:nth-of-type(1) {
        margin-left: 24px;
      }

      .Card:nth-last-of-type(1) {
        padding-right: 24px;
      }
    }
  }

  section.Location {

  }

  section.Collection {
    .CollectionList {
      margin-top: 16px;
      margin-left: -24px;
      margin-right: -24px;

      display: flex;
      overflow-x: scroll;
      -webkit-overflow-scrolling: touch;

      .Card {
        margin-right: 24px;
        flex-shrink: 0;
      }

      .Card:nth-of-type(1) {
        margin-left: 24px;
      }

      .Card:nth-last-of-type(1) {
        padding-right: 24px;
      }
    }
  }
</style>
