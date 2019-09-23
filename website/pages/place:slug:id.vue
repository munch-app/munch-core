<template>
  <div class="container">
    <div class="flex-justify-between relative">
      <div class="mt-24 pb-64 flex-grow">
        <section v-if="place.images && place.images.length" class="mb-32">
          <place-images :images="place.images"/>
        </section>

        <section v-if="place.status && place.status.type !== 'OPEN'" class="mb-32" @click="onSuggestEdit">
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

        <section class="mt-32">
          <aside>
            <place-aside class="PlaceAside" :place="place"/>
            <place-affiliates v-if="place.affiliates && place.affiliates.length" class="mtb-24" :place="place"/>
            <apple-map ref="map" class="Map border-3 overflow-hidden mt-24">
              <apple-map-pin-annotation :lat-lng="place.location.latLng"/>
            </apple-map>
          </aside>
        </section>

        <section v-if="place.description || place.website" class="mt-32 mb-48">
          <div class="p-0-16 border border-3">
            <p class="mtb-16" v-if="place.description">{{place.description}}</p>
            <div class="mtb-16" v-if="place.website">
              <h5>Website: <a target="_blank" rel="noreferrer noopener nofollow"
                              :href="place.website">{{place.website}}</a></h5>
            </div>
          </div>
        </section>

        <section>
          <in-article-adsense class="Place_InArticle_Middle"
                              data-ad-client="ca-pub-7144155418390858" data-ad-slot="1941106200"
          />
        </section>

        <section v-if="place.articles && place.articles.length" class="mt-48">
          <h3 class="mb-16 text-ellipsis-1l">Articles about {{place.name}}</h3>
          <place-articles :articles="place.articles"/>
        </section>

        <section v-if="place.createdBy" class="mt-64">
          <h5>{{place.name}}<span class="b-a50"> is created by:</span></h5>
          <div class="mt-16">
            <place-created-by :profile="place.createdBy"/>
          </div>
        </section>

        <section class="mt-48">
          <div>
            <h5>Is this place information accurate?</h5>
            <button @click="onSuggestEdit" class="mt-4 tiny border">
              Suggest an edit
            </button>
          </div>
        </section>

        <section class="mtb-48" v-if="place.synonyms && place.synonyms.length > 1">
          <h5>{{place.name}}<span class="b-a50"> is also known as: </span></h5>
          <p class="small text-capitalize">
            {{place.synonyms.join(', ')}}
          </p>
        </section>

        <section>
          <adsense data-ad-client="ca-pub-7144155418390858" data-ad-slot="7508826332"/>
        </section>
      </div>

      <div class="mt-24 flex-no-shrink">
        <div class="AsideContainer pb-32 none desktop-b"></div>
      </div>
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
  import PlaceCreatedBy from "../components/places/PlaceCreatedBy";
  import OpeningHours from "../components/utils/hour/OpeningHours";
  import PlaceAffiliates from "../components/places/PlaceAffiliates";
  import PlaceEditorDialog from "../components/dialog/PlaceEditorDialog";

  export default {
    components: {
      PlaceEditorDialog,
      PlaceAffiliates,
      OpeningHours,
      PlaceCreatedBy,
      PlaceArticles, PlaceStatus, PlaceImages, AppleMapPinAnnotation, AppleMap, GoogleEmbedMap, PlaceAside
    },
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
    data() {
      return {
        state: null
      }
    },
    mounted() {
      const {slug, id} = this.place
      window.history.replaceState({}, document.title, `/${slug}-${id}`)

      this.$nextTick(() => {
        this.$refs.map.centerAnnotations({
          minimumSpan: new mapkit.CoordinateSpan(0.015, 0.015)
        })
      })
    },
    methods: {
      onSuggestEdit() {
        this.$store.commit('global/setDialog', {
          name: 'PlaceEditorDialog', props: {
            place: this.place,
            onSubmit: (place) => {
              this.$api.post(`/places/${place.id}/revisions`, place)
                .then(() => {
                  this.$store.dispatch('addMessage', {title: 'Added Revision', message: 'Thanks for contributing!'})
                })
                .catch(err => {
                  this.$store.dispatch('addError', err)
                })
                .finally(() => {
                  this.$store.commit('global/clearDialog')
                })
            }
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .AsideContainer {
    @media (min-width: 992px) {
      width: 300px;
      margin-left: 48px;
    }
  }

  aside {
    @media (min-width: 992px) {
      width: 300px;

      position: fixed;
      top: calc(24px + 72px /*Header72px*/);
      left: calc(100vw - 300px - 24px);
      /*right: calc(100vw - 300px);*/
    }

    @media (min-width: 1200px) {
      left: calc(100vw - 300px - 80px);
    }

    @media (min-width: 1400px) {
      left: initial;
      right: calc((100vw - 1400px) / 2 + 80px);
    }
  }

  .Map {
    height: 216px;
  }
</style>
