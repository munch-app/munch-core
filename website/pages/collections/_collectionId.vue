<template>
  <div class="zero-spacing CollectionPage">
    <div class="Header container">
      <div>
        <h1>{{collection.name}}</h1>
        <div class="flex">
          <h5><span class="weight-400">by</span> Fuxing Loh</h5>
          <h5>{{collection.count}} <span class="weight-400">places</span></h5>
        </div>
      </div>

      <no-ssr>
        <div v-if="collection.userId === userId && collection.createdBy === 'User'">
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
  import BeatLoader from "vue-spinner/src/BeatLoader";
  import UserPlaceCollectionItemCard from "../../components/collections/UserPlaceCollectionItemCard";
  import _ from 'underscore'

  if (process.browser) require('intersection-observer')

  export default {
    components: {UserPlaceCollectionItemCard, BeatLoader},
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
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;

    .Card {
      position: relative;
      width: 100%;
      min-height: 1px;
      padding: 18px 12px;

      -ms-flex: 0 0 100%;
      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 768px) {
        -ms-flex: 0 0 33.333333%;
        flex: 0 0 33.333333%;
        max-width: 33.333333%;
      }

      @media (min-width: 1200px) {
        -ms-flex: 0 0 25%;
        flex: 0 0 25%;
        max-width: 25%;
      }
    }

    padding-bottom: 64px;
  }

  .LoadingIndicator {
    padding-bottom: 48px;
  }
</style>
