<template>
  <div class="InstagramDialog">
    <div class="InstagramHeader">
      <h3 class="text-ellipsis-1l">{{item.instagram.caption}}</h3>
      <a class="Author" :href="`https://instagram.com/${item.instagram.username}`" target="_blank" rel="noreferrer nofollow noopener">
        <h6>by <span class="s700">{{item.author}}</span> on {{format(item.createdMillis, 'mmm dd, yyyy')}}</h6>
      </a>
    </div>

    <image-size class="index-content" :image="item.image" grow="height">
      <div class="ImageContainer">
        <a class="Author" :href="`https://instagram.com/${item.instagram.username}`" target="_blank" rel="noreferrer nofollow noopener">
          <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/instagram.svg')"/>
          <div class="Name">{{item.author}}</div>
        </a>
      </div>
    </image-size>

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

  export default {
    name: "FeedSelectedInstagramDialog",
    components: {PlaceCard, ImageSize},
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
      display: block;
    }
  }

  .ImageContainer {
    height: 100%;
    width: 100%;
    padding: 16px 24px;

    display: flex;
    flex-direction: column;
    justify-content: flex-end;

    .Author {
      display: flex;

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
