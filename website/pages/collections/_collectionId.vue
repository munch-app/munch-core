<template>
  <div class="zero-spacing CollectionPage">
    <div class="Header container">
      <h1>{{collection.name}}</h1>
    </div>

    <div class="container">
      <div class="ItemList">
        <div class="Card" v-for="item in items" :key="item.placeId">
          <user-place-collection-item-card :place="item.place"/>
        </div>
      </div>
    </div>

    <div class="LoadingIndicator container" v-if="more" v-observe-visibility="{
          callback: visibilityChanged,
          throttle: 300,
        }">
      <beat-loader color="#084E69" size="14px"/>
    </div>
  </div>
</template>

<script>
  import BeatLoader from "vue-spinner/src/BeatLoader";
  import UserPlaceCollectionItemCard from "../../components/collections/UserPlaceCollectionItemCard";

  if (process.browser) {
    require('intersection-observer')
  }

  export default {
    components: {UserPlaceCollectionItemCard, BeatLoader},
    layout: 'search',
    data() {
      return {
        more: false,
        next: {},
        items: [],
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
      }
    }
  }
</script>

<style scoped lang="less">
  .Header {
    margin-top: 24px;
    margin-bottom: 0;
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
    padding-top: 24px;
    padding-bottom: 48px;
  }
</style>
