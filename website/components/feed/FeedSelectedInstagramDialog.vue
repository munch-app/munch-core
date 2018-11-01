<template>
  <div class="InstagramDialog flex-column">
    <div class="InstagramHeader">
      <h3 class="text-ellipsis-1l">{{item.instagram.caption}}</h3>
      <a class="Author block" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
         rel="noreferrer nofollow noopener">
        <h6>by <span class="s700">{{item.author}}</span> on {{format(item.createdMillis, 'mmm dd, yyyy')}}</h6>
      </a>
    </div>

    <image-sizes class="InstagramImage" :sizes="item.image.sizes"
                 width="800" height="1000" object-fit="contain"
                 max-height="calc(100vh - 80px - 40px - 64px)">
      <div class="ImageContainer flex-column-end">
        <a class="flex" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
           rel="noreferrer nofollow noopener">
          <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/instagram.svg')"/>
          <div class="Name">{{item.author}}</div>
        </a>
      </div>
    </image-sizes>

    <div class="Places" v-if="place">
      <h2>Place Mentioned</h2>

      <div class="Card">
        <place-card :place="place"/>
      </div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import ImageSize from "../core/ImageSize";
  import PlaceCard from "../places/PlaceCard";
  import ImageSizes from "../core/ImageSizes";

  export default {
    name: "FeedSelectedInstagramDialog",
    components: {ImageSizes, PlaceCard, ImageSize},
    props: {
      item: {
        type: Object,
        required: true
      },
    },
    computed: {
      place() {
        const placeId = this.item.places[0] && this.item.places[0].placeId
        if (placeId) {
          return this.$store.getters['feed/images/getPlace'](placeId)
        }
      },
    },
    methods: {
      format: dateformat,
    }
  }
</script>

<style scoped lang="less">
  .InstagramDialog {
    width: 100%;
  }

  .InstagramHeader {
    padding: 16px 24px;

    .Author {
      margin-top: 2px;
    }
  }

  .InstagramImage {
    background-color: rgba(0, 0, 0, 0.75);

    .ImageContainer {
      padding: 16px 24px;

      .Icon {
        width: 22px;
        height: 22px;
      }

      .Name {
        margin-left: 3px;

        height: 24px;
        line-height: 22px;
        font-size: 14px;
        font-weight: 600;
        color: white;
      }
    }
  }

  .Places {
    padding: 24px 24px;

    .Card {
      margin-top: 24px;
      max-width: 300px;
    }
  }
</style>
