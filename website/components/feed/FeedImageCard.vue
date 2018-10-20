<template>
  <div class="ImageFeedCard hover-pointer">
    <image-size class="border-3" :image="image" grow="height">
      <div class="ImageFeedContainer">
        <div class="Restaurant">
          <div class="Name" v-for="place in places" :key="place.placeId">
            {{place.name}}
          </div>
        </div>
        <div class="Author">
          <simple-svg class="Icon" fill="white" filepath="/img/feed/instagram.svg"/>
          <div class="Name">{{item.author}}</div>
        </div>
      </div>
    </image-size>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

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
      places() {
        return this.item.places.map(({placeId}) => {
          return this.$store.getters['feed/images/getPlace'](placeId)
        }).filter(place => place)
      }
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .ImageFeedCard {
  }

  .ImageFeedContainer {
    width: 100%;
    height: 100%;
    padding: 10px;

    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .Restaurant {
      .Name {
        text-align: right;
        margin-left: 4px;
        margin-right: 4px;

        height: 24px;
        line-height: 24px;
        font-size: 16px;
        font-weight: 600;

        color: white;
      }
    }

    .Author {
      display: flex;

      .Icon {
        width: 16px;
        height: 16px;
      }

      .Name {
        margin-left: 3px;

        height: 16px;
        line-height: 15px;
        font-size: 13px;
        font-weight: 600;
        color: white;
      }
    }
  }

  .ImageFeedContainer {
    opacity: 0;
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);

    &:hover {
      opacity: 1;
      background-color: rgba(0,0,0,0.4);
    }
  }
</style>
