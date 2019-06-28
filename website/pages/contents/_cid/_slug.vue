<template>
  <div class="pb-64 bg-steam">
    <div class="Content flex-justify-between relative container-1200 mt-48">
      <div class="ItemList">
        <div v-for="item in items">
          <content-text-body v-if="item.type === 'title'" :item="item"/>
          <content-text-body v-else-if="item.type === 'h1'" :item="item"/>
          <content-text-body v-else-if="item.type === 'h2'" :item="item"/>
          <content-text-body v-else-if="item.type === 'text'" :item="item"/>

          <content-image v-else-if="item.type === 'image'" :item="item"/>
          <content-line v-else-if="item.type === 'line'"/>
          <content-avatar v-else-if="item.type === 'avatar'"
                          :images="item.body.images"
                          :line1="item.body.line1"
                          :line2="item.body.line2"/>

          <content-place v-else-if="item.type ==='place'" :place="places[item.body.placeId]"
                         :options="item.body.options || {}"
                         :place-name="item.body.placeName" :place-id="item.body.placeId" ref="places"/>
        </div>
      </div>

      <div class="MapView border-4 overflow-hidden elevation-1 flex-column">
        <apple-map ref="map" class="flex-grow w-100" :options="map.options">
          <apple-map-place-marker-annotation v-for="place in places" v-if=" place && place.placeId" :key="place.placeId"
                                             :place="place"/>
        </apple-map>
        <div v-if="map.place" class="PlaceMapInfo p-16 flex-between">
          <div class="text-capitalize text-ellipsis-1l">
            <h5 class="mb-4 overflow-hidden">{{map.place.location.street || map.place.location.address}}</h5>
            <h6 class="m-0 overflow-hidden">
              <span>{{map.place.location.neighbourhood || map.place.location.street}}</span>,
              <span class="text-capitalize">{{map.place.location.city}}</span>
            </h6>
          </div>

          <div class="flex-no-shrink ml-8">
            <nuxt-link :to="`/places/${map.place.placeId}`">
              <button class="secondary small">Open Place</button>
            </nuxt-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import base64 from 'uuid-base64'
  import ContentTextBody from "../../../components/contents/ContentTextBody";
  import ContentPlace from "../../../components/contents/ContentPlace";
  import AppleMap from "../../../components/core/AppleMap";
  import ImageSizes from "../../../components/core/ImageSizes";
  import ContentLine from "../../../components/contents/ContentLine";
  import ContentImage from "../../../components/contents/ContentImage";
  import AppleMapPlaceMarkerAnnotation from "../../../components/core/AppleMapPlaceMarkerAnnotation";
  import ContentBanner from "../../../components/contents/ContentBanner";
  import SocialShare from "../../../components/core/SocialShare";
  import ContentAvatar from "../../../components/contents/ContentAvatar";

  function appendLoad({items, places, next}) {
    if (next) {
      const options = {params: {'next.itemId': next.itemId}}
      const prev = places
      return this.$api.get(`/contents/${this.contentId}/items`, options).then(({data, places, next}) => {
        items.push(...data)
        places = Object.assign({}, prev, places)
        return appendLoad.call(this, {items, places, next})
      })
    }
    return {items, places}
  }

  function getElementVisibleHeight(el) {
    const topBar = 56 // Top Bar
    const height = (window.innerHeight || document.documentElement.clientHeight)
    const rect = el.getBoundingClientRect()

    const padding = 48 // Padding for visibility

    function isWithin(y) {
      return (topBar + padding) <= y && y <= (height - padding)
    }

    if (isWithin(rect.top) && isWithin(rect.bottom)) {
      return rect.height
    }

    if (isWithin(rect.bottom)) {
      return rect.height - ((topBar + padding) - rect.top)
    }

    return 0
  }

  export default {
    components: {
      ContentAvatar,
      SocialShare,
      ContentBanner,
      AppleMapPlaceMarkerAnnotation,
      ContentImage,
      ContentLine,
      ImageSizes,
      AppleMap,
      ContentPlace,
      ContentTextBody
    },
    head() {
      const {url, content: {title, body, image}} = this

      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          type: 'article',
          url: url,
          image: image && ImageSizes.$$findUrl(image.sizes, 480, 480),
          title: `${title} · Munch`,
          description: body,
        },
        breadcrumbs: [
          {
            name: 'Contents',
            item: 'https://www.munch.app/contents'
          },
          {
            name: title,
            item: url
          },
        ]
      })
    },
    data() {
      return {
        map: {options: {}, place: null}
      }
    },
    asyncData({store, $api, params: {cid, slug}}) {
      // cid is not using the default base64, see more: https://www.npmjs.com/package/d64
      const contentId = base64.decode(cid)
      const url = `https://www.munch.app/contents/${cid}/${slug}`

      return Promise.all([
        $api.get(`/contents/${contentId}`).then(({data}) => ({content: data})),
        $api.get(`/contents/${contentId}/items`).then(({data, places, next}) => {
          return appendLoad.call({$api, contentId}, ({items: data, places, next}))
        })
      ]).then(values => {
        return {
          cid, slug, url,
          ...values[0],
          ...values[1],
        }
      })
    },
    mounted() {
      window.addEventListener("scroll", this.onScroll)
      this.onMapUpdate()
    },
    beforeDestroy() {
      window.removeEventListener("scroll", this.onScroll)
    },
    methods: {
      getVisiblePlaces() {
        return this.$refs['places'].map(com => {
          return {
            place: com.place,
            height: getElementVisibleHeight(com.$el)
          }
        })
          .filter(({height}) => height !== 0)
          .filter(({place}) => place)
          .sort((a, b) => b.height - a.height)
          .map(({place}) => place)
      },
      onScroll() {
        if (this.$refs.map) {
          clearTimeout(this.$mapUpdate)
          this.$mapUpdate = setTimeout(() => {
            this.onMapUpdate()
          }, 500);
        }
      },
      onMapUpdate() {
        this.$nextTick(() => {
          if (!this.$refs.map) return
          const places = this.getVisiblePlaces()

          if (places.length > 0) {
            this.map.place = places[0]
            this.$refs.map.centerAnnotations(this.$refs.map.getAnnotations((p) => {
              return p.placeId === places[0].placeId
            }))
          }
        })
      },
    },
  }
</script>

<style scoped lang="less">
  .Content {
  }

  .ItemList {

  }

  .PlaceMapInfo {
    @media (max-width: 991.98px) {
      display: none;
    }
  }

  .MapView {
    @media (max-width: 991.98px) {
      position: fixed;
      right: 16px;
      bottom: 16px;

      width: 220px;
      height: 144px;
    }

    @media (max-width: 576px) {
      display: none;
    }

    @media (min-width: 992px) {
      flex: 0 0 33%;
      height: calc((100vh - 80px) * 0.7);

      position: sticky;
      top: calc(24px + 72px /*Header72px*/);
      margin-left: 128px;
    }
  }
</style>
