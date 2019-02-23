<template>
  <div class="zero container Page">
    <section class="flex-center container-remove-gutter">
      <nuxt-link :to="`/places/${place.placeId}`" class="lh-0" @click.native.capture="$track.view(`RIP`, 'Feed - Instagram')">
        <image-sizes class="InstagramImage" :sizes="item.image.sizes"
                     width="800" height="1000" object-fit="contain"
                     max-height="calc(100vh - 96px)">
        </image-sizes>
      </nuxt-link>
    </section>

    <feed-image-action class="flex-end mtb-24" :place="place"/>

    <a class="block" :href="`https://instagram.com/${item.instagram.username}`" target="_blank"
       rel="noreferrer nofollow noopener">
      <p class="text-ellipsis-2l subtext">{{item.instagram.caption}}</p>

      <h5 class="text-ellipsis-2l mt-8">
        by <span class="s700">{{item.author}}</span> on {{formatMillis(item.createdMillis)}}
      </h5>
    </a>

    <div class="mt-32">
      <h2 class="mb-24">Place Mentioned</h2>

      <div class="Place" v-if="place">
        <place-card :place="place"/>
      </div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import ImageSizes from "../../../components/core/ImageSizes";
  import PlaceCard from "../../../components/places/PlaceCard";
  import FeedImageAction from "../../../components/feed/images/FeedImageAction";

  export default {
    components: {FeedImageAction, PlaceCard, ImageSizes},
    asyncData({$api, store, params, redirect}) {
      const item = store.getters['feed/images/getItem'](params.itemId)
      if (item) {
        return {item, places: {}, back: true}
      }

      return $api.get(`/feed/items/${params.itemId}`)
        .then(({data: {item, places}}) => {
          return {item, places, back: false}
        })
    },
    computed: {
      place() {
        const placeId = this.item.places[0] && this.item.places[0].placeId
        return this.places[placeId] ||
          this.$store.getters['feed/images/getPlace'](placeId)
      },
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">
  .Page {
    padding-bottom: 64px;
  }

  section {
    background-color: rgba(0, 0, 0, 0.75);
  }

  .Place {
    max-width: 280px;
  }
</style>
