<template>
  <div class="border-4 overflow-hidden elevation-1 flex-column">
    <apple-map ref="map" class="Map flex-grow w-100" :options="map.options">
      <article-context-map-annotation v-for="item in items" :key="item.id" :item="item"/>
    </apple-map>

    <div v-if="map.focused" class="Focused p-16 flex-between">
      <div>
        <h5 class="text-ellipsis-1l m-0 overflow-hidden">{{map.focused.name}}</h5>
        <h6 class="b-a75 text-ellipsis-2l m-0 overflow-hidden">
          {{map.focused.address}}
        </h6>
      </div>

      <div class="flex-no-shrink ml-16">
        <nuxt-link :to="map.focused.path">
          <button class="blue">Open</button>
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from "../utils/map/AppleMap";
  import AppleMapPlaceMarkerAnnotation from "../core/AppleMapPlaceMarkerAnnotation";
  import ArticleContextMapAnnotation from "./ArticleContextMapAnnotation";

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

  function getElementDistanceFromCenter(el) {
    const height = (window.innerHeight || document.documentElement.clientHeight)
    // Window - 72 (Top Bar) = Center - 100 (Offset since below eyes look at the top)
    const windowCenter = ((height - 72) / 2) - 100

    const rect = el.getBoundingClientRect()
    const rectCenter = rect.top + (rect.height / 2)

    return Math.abs(windowCenter - rectCenter);
  }

  export default {
    name: "ArticleContextMap",
    components: {ArticleContextMapAnnotation, AppleMapPlaceMarkerAnnotation, AppleMap},
    props: {
      article: {
        type: Object,
        required: true
      },
      getContexts: Function
    },
    computed: {
      items() {
        return this.article?.content?.map(node => this.mapData({place: node.attrs?.place}))
          .filter(node => node)
      },
    },
    data() {
      return {
        map: {options: {}, focused: null}
      }
    },
    mounted() {
      window.addEventListener("scroll", this.onScroll)
      this.onMapUpdate()
    },
    beforeDestroy() {
      window.removeEventListener("scroll", this.onScroll)
    },
    methods: {
      mapData({place}) {
        if (place) {
          return {
            type: 'place',
            id: place.id,
            name: place.name,
            latLng: place.location?.latLng,
            address: place.location?.address,
            path: `/places/${place.id}`
          }
        }
      },
      getData(id) {
        return this.items.filter(n => n.id === id)[0]
      },
      getVisibleContextItems() {
        return this.getContexts().map(com => {
          return {
            id: com?.id,
            height: getElementVisibleHeight(com.$el),
            fromCenter: getElementDistanceFromCenter(com.$el)
          }
        })
          .filter(({height, id}) => height !== 0 && id)
          .sort((a, b) => a.fromCenter - b.fromCenter)

      },
      onScroll() {
        if (this.$refs.map) {

          // On scroll, remove timeout for next map update
          clearTimeout(this.$mapUpdate)
          this.$mapUpdate = setTimeout(() => {
            this.onMapUpdate()
          }, 500);
        }
      },
      onMapUpdate() {
        this.$nextTick(() => {
          if (!this.$refs.map) return
          const items = this.getVisibleContextItems()

          if (items.length > 0) {
            this.map.focused = this.getData(items[0].id)
            this.$refs.map.centerAnnotations(this.$refs.map.getAnnotations((p) => {
              return p.id === items[0].id
            }))
          }
        })
      },
    }
  }
</script>

<style scoped lang="less">
  .Map {
    height: 260px;
  }
</style>
