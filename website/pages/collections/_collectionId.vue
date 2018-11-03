<template>
  <div class="zero CollectionPage">
    <div class="Header container">
      <div>
        <h1>{{collection.name}}</h1>
        <div class="flex">
          <h5 v-if="collection.userProfile"><span class="weight-400">by</span> {{collection.userProfile.name}}</h5>
          <h5>{{collection.count}} <span class="weight-400">places</span></h5>
        </div>
      </div>

      <no-ssr>
        <div v-if="isEditable">
          <div v-if="!editing" @click="editing = true"
               class="button-action elevation-1 elevation-hover-2 border-3 white-bg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/collections/edit.svg"/>
          </div>
          <div v-if="editing" @click="editing = false"
               class="button-action elevation-1 elevation-hover-2 border-3 white-bg hover-pointer">
            <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/collections/done.svg"/>
          </div>
        </div>
      </no-ssr>
    </div>

    <div class="container">
      <div class="ItemList">
        <div class="Card" v-for="item in items" :key="item.placeId">
          <user-place-collection-item-card :editing="editing" :place="item.place"
                                           :collection="collection" @on-delete="onDelete(item)"/>
        </div>
      </div>
    </div>

    <div class="LoadingIndicator container" v-if="more"
         v-observe-visibility="{callback:visibilityChanged,throttle:300}">
      <beat-loader color="#084E69" size="14px"/>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import UserPlaceCollectionItemCard from "../../components/collections/UserPlaceCollectionItemCard";
  import _ from 'lodash'

  export default {
    components: {UserPlaceCollectionItemCard},
    data() {
      return {
        more: false,
        next: {},
        items: [],
        editing: false
      }
    },
    asyncData({$axios, params}) {
      return $axios.$get(`/api/users/places/collections/${params.collectionId}`)
        .then(({data}) => {
          return {
            collection: data,
            more: true
          }
        })
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn', 'userId']),
      isEditable() {
        if (this.collection.userId === this.userId) {
          switch (this.collection.createdBy) {
            case 'User':
            case 'Default':
              return true
          }
        }
      }
    },
    methods: {
      visibilityChanged(isVisible, entry) {
        if (isVisible) {
          const id = this.collection.collectionId
          const sort = this.next.sort
          this.$axios.$get(`/api/users/places/collections/${id}/items${sort ? '?next.sort=' + sort : ''}`)
            .then(({data, next}) => {
              this.next = next
              this.more = !!next
              this.items.push(...data)
            }).catch(error => alert(error))
        }
      },
      onDelete(item) {
        const index = _.findIndex(this.items, (n) => n.placeId === item.placeId)
        if (index !== -1) {
          this.items.splice(index, 1)
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Header {
    margin-top: 24px;
    margin-bottom: 16px;

    display: flex;
    flex-wrap: wrap;
    align-content: flex-end;
    justify-content: space-between;

    h5 {
      margin-right: 24px;
    }
  }

  .ItemList {
    display: flex;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;

    .Card {
      position: relative;
      width: 100%;
      min-height: 1px;
      padding: 18px 12px;

      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 500px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 33.333333%;
        max-width: 33.333333%;
      }

      @media (min-width: 1200px) {
        flex: 0 0 25%;
        max-width: 25%;
      }

      @media (min-width: 1600px) {
        flex: 0 0 20%;
        max-width: 20%;
      }
    }

    padding-bottom: 64px;
  }

  .LoadingIndicator {
    padding-bottom: 48px;
  }
</style>
