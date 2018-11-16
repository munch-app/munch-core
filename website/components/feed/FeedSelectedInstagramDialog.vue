<template>
  <div class="InstagramDialog flex-align-stretch border-3 overflow-hidden">
    <section class="flex-center">
      <nuxt-link :to="`/places/${place.placeId}`" class="lh-0" @click.native="$track.view(`RIP`, 'Feed - Instagram')">
        <image-sizes class="InstagramImage" :sizes="item.image.sizes"
                     width="800" height="1000" object-fit="contain"
                     max-height="calc(100vh - 96px)">
        </image-sizes>
      </nuxt-link>
    </section>
    <aside>
      <div class="Header">
        <a class="block" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
           rel="noreferrer nofollow noopener">
          <p class="text-ellipsis-3l subtext">{{item.instagram.caption}}</p>

          <h5 class="text-ellipsis-2l mt-16">
            by <span class="s700">{{item.author}}</span> on {{formatMillis(item.createdMillis)}}
          </h5>
        </a>

        <div class="mt-16">
          <button class="border">Save</button>
        </div>
      </div>

      <div class="Information">

        <div class="Place" v-if="place">
          <h3 class="mb-16 secondary">Place Mentioned</h3>
          <place-card :place="place" :image="true"/>
        </div>
      </div>
    </aside>
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
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">
  .InstagramDialog {
    width: 100%;
  }

  section {
    background-color: rgba(0, 0, 0, 0.75);
    flex: 0 0 66.666%;
  }

  aside {
    flex: 0 0 33.333%;
  }

  .Header {
    padding: 16px 24px;
  }

  .Information {
    padding: 16px 24px;
  }

  .Place {
    max-width: 320px;
  }
</style>
