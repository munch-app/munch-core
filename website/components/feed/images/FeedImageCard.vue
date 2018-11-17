<template>
  <div class="hover-pointer">
    <image-size class="border-3" :image="image" grow="height" :alt="alt">
      <div class="ImageFeedContainer wh-100 flex-column-justify-between hover-opacity hover-bg-a40">

        <div class="flex-end">
          <div class="RestaurantName" v-for="place in places" :key="place.placeId">
            {{place.name}}
          </div>
        </div>

        <div class="flex">
          <simple-svg class="wh-16px" fill="white" :filepath="require('~/assets/icon/feed/instagram.svg')"/>
          <div class="AuthorName">{{item.author}}</div>
        </div>
      </div>
    </image-size>
  </div>
</template>

<script>
  import ImageSize from "../../core/ImageSize";

  export default {
    name: "FeedImageCard",
    components: {ImageSize},
    props: {
      item: {
        required: true,
        type: Object
      },
    },
    computed: {
      image() {
        return this.item.image
      },
      alt() {
        const place = this.places[0]
        return place && place.name
      },
      places() {
        return this.item.places.map(({placeId}) => {
          return this.$store.getters['feed/images/getPlace'](placeId)
        }).filter(place => place)
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageFeedContainer {
    padding: 10px;
  }

  .RestaurantName,
  .AuthorName {
    font-weight: 600;
    color: white;
  }

  .RestaurantName {
    margin-left: 4px;
    margin-right: 4px;

    height: 24px;
    line-height: 24px;
    font-size: 16px;
  }

  .AuthorName {
    margin-left: 3px;

    height: 16px;
    line-height: 15px;
    font-size: 13px;
  }
</style>
