<template>
  <div class="zero-spacing">
    <section class="Profile container">
      <div class="ProfileImage Secondary100Bg">
        <no-ssr>
          <img v-if="photo" :src="photo" :alt="profile.name">
        </no-ssr>
      </div>
      <no-ssr class="ProfileDetail">
        <div>
          <h2 class="Name BlackA85">{{profile.name}}</h2>
          <div class="Email Text Weight600 BlackA75">{{profile.email}}</div>
        </div>
      </no-ssr>
      <div class="ProfileAction">
        <div class="SettingButton">Setting</div>
      </div>
    </section>
    <hr class="container">

    <section class="Collection">
      <div class="Header container">
        <div>
          <h2>My Collections</h2>
        </div>
        <div class="CollectionControl">
          <div class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="#0A6284" filepath="/img/profile/edit.svg"/>
          </div>
          <div class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="#0A6284" filepath="/img/profile/add.svg"/>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="CollectionList">
          <div class="Card" v-for="collection in collections" :key="collection.collectionId">
            <profile-collection-card :collection="collection"/>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import ProfileCollectionCard from "../../components/profile/ProfileCollectionCard";

  export default {
    components: {ProfileCollectionCard},
    layout: 'search',
    data() {
      return {
        collections: [],
        next: {}
      }
    },
    asyncData({$axios}) {
      return $axios.$get('/api/users/places/collections')
        .then(({data, next}) => {
          return {
            collections: data,
            next: next
          }
        })
    },
    computed: {
      profile() {
        return this.$store.state.user.profile || {}
      },
      photo() {
        return this.profile && this.profile.photoUrl
      }
    },
    mounted() {
      if (process.client) {
        const authenticator = require('~/services/authenticator').default
        if (!this.$store.state.user.profile && !authenticator.isLoggedIn()) {
          this.$router.push({path: '/authenticate'})
        }
      }
    }
  }
</script>

<style scoped lang="less">
  section {
    margin-top: 24px;
  }

  section.Profile {
    display: flex;
    align-items: flex-end;

    .ProfileImage {
      overflow: hidden;
      height: 64px;
      width: 64px;
      border-radius: 50%;
      margin-right: 24px;

      margin-bottom: 16px;

      img {
        height: 64px;
        width: 64px;
      }
    }

    .ProfileDetail {
      .Name {
      }

      .Email {
        margin-bottom: 16px;
      }
    }

    .ProfileAction {
      .SettingButton {
        line-height: 24px;
        height: 24px;
        font-size: 17px;
      }
    }
  }

  section.Collection {
    padding-bottom: 64px;

    .Header {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;

      margin-bottom: 24px;
    }

    .CollectionControl {
      display: flex;

      .CollectionButton {
        margin-left: 16px;
        padding: 8px;
        width: 40px;
        height: 40px;
      }
    }

    .CollectionList {
      display: flex;
      flex-wrap: wrap;
      margin-top: -12px;
      margin-right: -12px;
      margin-left: -12px;

      .Card {
        position: relative;
        width: 100%;
        min-height: 1px;
      }
    }

    @media (max-width: 767.98px) {
      .Card {
        flex: 0 0 174px;
        width: 150px;
        height: 150px;
        flex-shrink: 0;
        padding-left: 12px;
        padding-right: 12px;
      }

      .CollectionList {
        flex-wrap: nowrap;
        overflow-x: scroll;
        -webkit-overflow-scrolling: touch;
      }
    }

    @media (min-width: 768px) {
      .Card {
        flex: 0 0 25%;
        height: 200px;
        padding: 12px 12px;
      }
    }

    @media (min-width: 1200px) {
      .Card {
        flex: 0 0 20%;
      }
    }
  }
</style>
