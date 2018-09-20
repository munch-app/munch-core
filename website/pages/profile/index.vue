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
          <div class="Email text weight-600 BlackA75">{{profile.email}}</div>
        </div>
      </no-ssr>
      <div class="ProfileAction">
        <div
          class="SettingButton border-3 elevation-1 text-uppercase BlackA75 weight-600 hover-pointer elevation-hover-2">
          Settings
        </div>
      </div>
    </section>
    <hr class="container">

    <section class="Collection">
      <div class="Header container">
        <div>
          <h2>My Collections</h2>
        </div>
        <div class="CollectionControl">
          <div v-if="!showCollectionButton" @click="showCollectionButton = true"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/edit.svg"/>
          </div>
          <div v-if="!showCollectionButton" @click="creating = {access: 'Public'}"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/add.svg"/>
          </div>
          <div v-if="showCollectionButton" @click="showCollectionButton = false"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/done.svg"/>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="CollectionList">
          <div class="Card" v-for="collection in list" :key="collection.collectionId">
            <profile-collection-card :collection="collection" :show-button="showCollectionButton"/>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="LoadingIndicator" v-if="more" v-observe-visibility="{
            callback: visibilityChanged,
            throttle: 300,
            }">
          <beat-loader color="#084E69" size="14px"/>
        </div>
      </div>

      <portal to="dialog-styled" v-if="creating">
        <h3>Create New Collection</h3>
        <input v-model="creating.name" placeholder="Name">
        <input v-model="creating.description" placeholder="Description">
        <div class="right">
          <button class="clear-elevated" @click="creating = null">Cancel</button>
          <button class="secondary" @click="onCreate">Create</button>
        </div>
      </portal>
    </section>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import ProfileCollectionCard from "../../components/profile/ProfileCollectionCard";
  import BeatLoader from "vue-spinner/src/BeatLoader";

  if (process.browser) require('intersection-observer')

  export default {
    components: {BeatLoader, ProfileCollectionCard},
    data() {
      return {
        showCollectionButton: false,
        creating: null,
      }
    },
    computed: {
      ...mapGetters('user/collections', ['list', 'more']),
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
    },
    methods: {
      visibilityChanged(isVisible) {
        if (isVisible) {
          this.$store.dispatch('user/collections/load')
        }
      },
      onCreate() {
        return this.$store.dispatch('user/collections/post', this.creating)
          .then(() => {
            this.creating = null
          }).catch(error => {
            this.$store.dispatch('addError', error)
          })
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

      @media (max-width: 575.98px) {
        display: none;
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
      margin-left: auto;
      margin-right: 0;

      .SettingButton {
        line-height: 36px;
        height: 36px;
        font-size: 16px;

        padding: 0 16px;
        margin-bottom: 15px;
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
