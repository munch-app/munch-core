<template>
  <div class="container flex-justify-between">
    <div class="ContentBody flex-grow">
      <section class="Max720 mt-32">
        <div>
          <place-images v-if="place.images && place.images.length" :images="place.images"/>
        </div>

        <h1>{{place.name}}</h1>
        <!-- TODO: <place-status/> -->

        <div class="mt-16">
          <div class="m--6 flex-wrap" v-if="place.tags.length > 0">
            <div class="p-4" v-for="tag in place.tags" :key="tag.id">
              <div class="bg-steam border-3 p-4-12">{{tag.name}}</div>
            </div>
          </div>
        </div>
      </section>

      <!-- TODO: <place-about/> -->
      <!-- TODO: <place-detail/> -->

      <section v-if="place.description" class="Max720">
        <!--        <div v-if="place.description" @click="expanded = !expanded" class="hover-pointer">-->
        <!--          <p class="Description" :class="{'text-ellipsis-4l': !expanded}">{{place.description}}</p>-->
        <!--        </div>-->
      </section>

      <section class="Max720 mtb-48">
        <h2>Location</h2>

        <p class="mt-16">{{place.location.address}}</p>
      </section>

    </div>

    <section class="mt-24">
      <aside>
        <place-aside class="PlaceAside" :place="place"/>
        <!-- TODO: <place-affiliates/> -->
        <apple-map ref="map" class="Map border-3 overflow-hidden mt-24">
          <apple-map-pin-annotation :lat-lng="place.location.latLng"/>
        </apple-map>
        <div class="mt-24">
          <Adsense data-ad-client="ca-pub-7144155418390858" data-ad-slot="4206143635"/>
        </div>
      </aside>
    </section>
  </div>
</template>

<script>
  import PlaceAside from "../components/places/PlaceAside";
  import GoogleEmbedMap from "../components/core/GoogleEmbedMap";
  import AppleMap from "../components/utils/map/AppleMap";
  import AppleMapPinAnnotation from "../components/utils/map/AppleMapPinAnnotation";
  import PlaceImages from "../components/places/PlaceImages";

  export default {
    components: {PlaceImages, AppleMapPinAnnotation, AppleMap, GoogleEmbedMap, PlaceAside},
    head() {
      const {image, name, description, slug, id} = this.place
      return this.$head({
        robots: {follow: true, index: false},
        canonical: `https://www.munch.app/${slug}-${id}`,
        title: `${name} · Munch`,
        description: description,
        type: 'place',
        image: image,
        url: `https://www.munch.app/${slug}-${id}`,
        breadcrumbs: [
          {
            name: 'Places',
            item: `https://www.munch.app/places`
          },
          {
            name: name,
            item: `https://www.munch.app/${slug}-${id}`
          },
        ]
      })
    },
    asyncData({$api, params: {id}}) {
      return $api.get(`/places/${id}`, {params: {fields: 'articles,affiliates,images'}})
        .then(({data: place}) => {
          return {place}
        })
    },
    mounted() {
      this.$nextTick(() => {
        this.$refs.map.centerAnnotations({
          minimumSpan: new mapkit.CoordinateSpan(0.015, 0.015)
        })
      })
    }
  }
</script>

<style scoped lang="less">
  .Max720 {
    max-width: 720px;
  }

  .ContentBody {
    @media (min-width: 992px) {
      margin-right: 48px;
    }
  }

  aside {
    @media (min-width: 992px) {
      min-width: 320px;

      position: sticky;
      top: calc(24px + 72px /*Header72px*/);
    }
  }

  .PlaceAside {
    @media (max-width: 991.98px) {
      border-radius: 0;
      border-style: solid none none none;

      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }

  .Map {
    height: 216px;
  }
</style>
