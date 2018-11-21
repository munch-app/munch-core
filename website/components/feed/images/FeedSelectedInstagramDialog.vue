<template>
  <div class="w-100 flex-align-stretch border-3 overflow-hidden">
    <section class="flex-center">
      <nuxt-link :to="`/places/${place.placeId}`" class="lh-0" @click.native.capture="$track.view(`RIP`, 'Feed - Instagram')">
        <image-sizes class="InstagramImage" :sizes="item.image.sizes"
                     width="800" height="1000" object-fit="contain"
                     max-height="calc(100vh - 96px)">
        </image-sizes>
      </nuxt-link>
    </section>
    <aside class="p-24">
      <feed-image-action :place="place"/>

      <div class="mt-24">
        <a class="block" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
           rel="noreferrer nofollow noopener">
          <p class="text-ellipsis-3l subtext">{{item.instagram.caption}}</p>

          <h5 class="text-ellipsis-2l mt-8">
            by <span class="s700">{{item.author}}</span> on {{formatMillis(item.createdMillis)}}
          </h5>
        </a>
      </div>



      <div class="mt-32">
        <h2 class="mb-24">Place Mentioned</h2>

        <div class="Place" v-if="place">
          <place-card :place="place"/>
        </div>
      </div>
    </aside>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import PlaceCard from "../../places/PlaceCard";
  import ImageSizes from "../../core/ImageSizes";
  import FeedImageAction from "./FeedImageAction";

  export default {
    name: "FeedSelectedInstagramDialog",
    components: {FeedImageAction, ImageSizes, PlaceCard},
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
  section {
    background-color: rgba(0, 0, 0, 0.75);
  }

  aside {
    min-width: 260px;
    max-width: 400px;
    flex: 0 0 33.333%;
  }

  .Place {
    max-width: 280px;
  }
</style>
