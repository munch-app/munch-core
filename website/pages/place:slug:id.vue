<template>
  <div class="container flex-justify-between">
    <div class="mt-24 pb-64 flex-grow">
      <section v-if="place.images && place.images.length" class="mb-32">
        <place-images :images="place.images"/>
      </section>

      <section v-if="place.status && place.status.type !== 'OPEN'" class="mb-32">
        <place-status :status="place.status"/>
      </section>

      <section>
        <h1>{{place.name}}</h1>
      </section>

      <section v-if="place.tags.length > 0" class="mt-24">
        <div class="m--6 flex-wrap">
          <div class="p-4" v-for="tag in place.tags" :key="tag.id">
            <div class="bg-steam border-3 p-4-12">{{tag.name}}</div>
          </div>
        </div>
      </section>

      <section v-if="place.description || place.website" class="mt-32">
        <div class="p-16 border border-3">
          <p v-if="place.description">{{place.description}}</p>
          <div class="mt-8" v-if="place.website">
            <h5>Website: <a target="_blank" rel="noreferrer noopener nofollow"
                            :href="place.website">{{place.website}}</a></h5>
          </div>
        </div>
      </section>

      <section>
        <!--  TODO Adsense -->
      </section>

      <section v-if="place.articles && place.articles.length" class="mt-48">
        <h3 class="mb-16 text-ellipsis-1l">Articles about {{place.name}}</h3>
        <place-articles :articles="place.articles"/>
      </section>

      <section class="mt-64" v-if="place.synonyms && place.synonyms.length > 1">
        <h5>{{place.name}}<span class="b-a50"> is also known as: </span></h5>
        <p class="small text-capitalize">
          {{place.synonyms.join(', ')}}
        </p>
      </section>
    </div>

    <div class="mt-24 flex-no-shrink">
      <aside class="pb-24">
        <place-aside class="PlaceAside" :place="place"/>
        <!-- TODO: <place-affiliates/> -->
        <apple-map ref="map" class="Map border-3 overflow-hidden mt-24">
          <apple-map-pin-annotation :lat-lng="place.location.latLng"/>
        </apple-map>
      </aside>
    </div>
  </div>
</template>

<script>
  import PlaceAside from "../components/places/PlaceAside";
  import GoogleEmbedMap from "../components/core/GoogleEmbedMap";
  import AppleMap from "../components/utils/map/AppleMap";
  import AppleMapPinAnnotation from "../components/utils/map/AppleMapPinAnnotation";
  import PlaceImages from "../components/places/PlaceImages";
  import PlaceStatus from "../components/places/PlaceStatus";
  import PlaceArticles from "../components/places/PlaceArticles";

  export default {
    components: {PlaceArticles, PlaceStatus, PlaceImages, AppleMapPinAnnotation, AppleMap, GoogleEmbedMap, PlaceAside},
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
      const {slug, id} = this.place
      window.history.replaceState({}, document.title, `/${slug}-${id}`)

      this.$nextTick(() => {
        this.$refs.map.centerAnnotations({
          minimumSpan: new mapkit.CoordinateSpan(0.015, 0.015)
        })
      })
    }
  }
</script>

<style scoped lang="less">
  aside {
    @media (min-width: 992px) {
      width: 300px;
      margin-left: 48px;

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
