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
          <div v-if="!editing" @click="onEdit"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/edit.svg"/>
          </div>
          <div v-if="!editing" @click="onAdd"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/add.svg"/>
          </div>
          <div v-if="editing" @click="onDone"
               class="CollectionButton elevation-1 elevation-hover-2 border-3 WhiteBg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/profile/done.svg"/>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="CollectionList">
          <div class="Card" v-for="collection in list" :key="collection.collectionId">
            <div class="Editing" v-if="editing && isEditable(collection)">
              <simple-svg class="Icon" fill="white" filepath="/img/profile/edit.svg"/>
              <simple-svg class="Icon" fill="white" filepath="/img/profile/remove.svg"/>
            </div>
            <portal to="dialog-confirmation" v-if="editing">
              <h3>Delete '{{collection.name}}'?</h3>
              <button class="primary" @click="editing = !editing">Delete</button>
            </portal>
            <profile-collection-card :collection="collection"/>
          </div>

          <div class="LoadingIndicator" v-if="more" v-observe-visibility="{
            callback: visibilityChanged,
            throttle: 300,
            }">
            <beat-loader color="#084E69" size="14px"/>
          </div>
        </div>
      </div>
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
        editing: false
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
      isEditable(collection) {
        return collection.createdBy === 'User'
      },
      onEdit() {
        this.editing = true
      },
      onAdd() {

      },
      onDone() {
        this.editing = false
      },
      onRemoveCollection(collection) {
        // TODO
      },
      onEditCollection(collection) {
        // TODO
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

      .Editing {
        position: absolute;
        width: 100%;
        justify-content: flex-end;
        display: flex;
        z-index: 1;
        padding-top: 6px;
        padding-right: 26px;

        .Icon {
          width: 20px;
          height: 20px;
          margin-right: 4px;
        }
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
