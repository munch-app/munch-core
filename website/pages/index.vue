<template>
  <div class="ZeroSpacing LandingPage">
    <section class="Profile">
      <div class="Container">
        <div class="Name">
          <h1>Good morning, Samantha</h1>
          <div class="Text Login">(not your name? <span class="LoginButton Primary500 Weight600">Log In</span> now!)</div>
        </div>
        <p>Discover the best wherever & whenever</p>
      </div>
    </section>

    <section class="Timing">
      <div class="Container">
        <h2>Here & Now</h2>
        <p>The best Breakfast spots near you.</p>
      </div>
    </section>

    <section class="Location">
      <div class="Container">
        <h2>Day in, day out</h2>
        <p>Your favourite places - rediscovered.</p>
      </div>
      <landing-location-list/>
    </section>

    <section class="Collection">
      <div class="Container">
        <h2>In the know</h2>
        <p>What’s hot and edible</p>
      </div>

      <div class="CollectionList Container">
        <div class="Card" v-for="collection in collections" :key="collection.collectionId">
          <user-place-collection-card :collection="collection"/>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import LandingLocationList from "../components/landing/LandingLocationList";
  import UserPlaceCollectionCard from "../components/collections/UserPlaceCollectionCard";

  export default {
    components: {UserPlaceCollectionCard, LandingLocationList},
    layout: 'search',
    asyncData({$axios}) {
      return $axios.$post('/api/landing')
        .then(({data}) => {
          return {
            collections: data.collections
          }
        })
    }
  }
</script>

<style scoped lang="less">
  .LandingPage {
    margin-top: 24px;
    margin-bottom: 48px;
  }

  section {
    margin-top: 24px;
  }

  section.Profile {
    .Name {
      display: flex;
      align-items: flex-end;
      justify-content: flex-start;

      @media (max-width: 575.98px) {
        flex-direction: column;
        div {
          width: 100%;
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

  section.Location {

  }

  section.Collection {
    .CollectionList {
      margin-top: 16px;

      display: flex;
      overflow-x: scroll;
      -webkit-overflow-scrolling: touch;

      .Card {
        margin-right: 24px;
      }

      .Card:nth-last-of-type(1){
        padding-right: 24px;
      }
    }
  }
</style>
