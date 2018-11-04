<template>
  <div class="InstagramDialog flex-column border-3 overflow-hidden">
    <div class="InstagramHeader">
      <a class="block" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
         rel="noreferrer nofollow noopener">
        <h6 class="text-ellipsis-2l b-a75">{{item.instagram.caption}}</h6>
        <h6 class="text-ellipsis-1l Author">by <span class="s700">{{item.author}}</span> on {{format(item.createdMillis,
          'mmm dd, yyyy')}}</h6>
      </a>
    </div>

    <nuxt-link :to="`/places/${place.placeId}`" class="lh-0">
      <image-sizes class="InstagramImage" :sizes="item.image.sizes"
                   width="800" height="1000" object-fit="contain"
                   max-height="calc(100vh - 94px - 48px - 128px - 24px)">
        <no-ssr>
          <place-card-add-collection class="absolute" :place="place" large/>
        </no-ssr>
      </image-sizes>
    </nuxt-link>

    <div class="Place" v-if="place">
      <place-card :place="place" :image="false"/>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import ImageSize from "../core/ImageSize";
  import PlaceCard from "../places/PlaceCard";
  import ImageSizes from "../core/ImageSizes";
  import PlaceCardAddCollection from "../places/PlaceCardAddCollection";

  export default {
    name: "FeedSelectedInstagramDialog",
    components: {PlaceCardAddCollection, ImageSizes, PlaceCard, ImageSize},
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
    @media (max-width: 768px) {
      height: 110vh;
    }
  }

  .InstagramHeader {
    padding: 16px 24px;

    .Author {
      margin-top: 8px;
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

  .Place {
    padding: 16px 24px 24px;
  }
</style>
