<template>
  <div>
    <div class="container">
      <div class="flex m--16">
        <div class="p-16 mt-24 pb-64 flex-grow">
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
            <div class="Summary">
              <portal-target name="Place_Summary"/>
            </div>
          </section>

          <section v-if="place.description || place.website" class="mt-32 mb-48">
            <div class="p-0-16 border border-3">
              <p class="mtb-16" v-if="place.description">{{place.description}}</p>
              <div class="mtb-16" v-if="place.website">
                <h5 class="text-ellipsis-1l break-all">Website: <a target="_blank" rel="noreferrer noopener nofollow"
                                                                   :href="place.website">{{place.website}}</a></h5>
              </div>
            </div>
          </section>

          <section>
            <advert class="Place_InArticle_Middle"
                    :google="{slot: 1941106200, layout: 'in-article', format: 'fluid'}"
            />
          </section>

          <section v-if="place.articles && place.articles.length" class="mt-48">
            <div>
              <h3 class="mb-16 text-ellipsis-1l break-all">Articles about {{place.name}}</h3>
              <div class="mt-24">
                <horizontal-list :items="place.articles" :options="{size: 3}">
                  <template v-slot:default="{item}">
                    <article-card :article="item"/>
                  </template>
                </horizontal-list>
              </div>
            </div>
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
            <advert class="Place_Bottom"
                    :google="{slot: 7508826332, format: 'auto'}"
            />
          </section>
        </div>

        <aside class="p-16 mt-24 relative">
          <div class="Summary">
            <portal-target name="Place_Summary"/>
          </div>
        </aside>
      </div>
    </div>

    <div class="container">
      <div class="mt-24 mb-64">
        <place-mentions :place="place"/>
      </div>
    </div>

    <portal to="Place_Summary">
      <place-aside class="PlaceAside" :place="place"/>
      <place-affiliates v-if="place.affiliates && place.affiliates.length" class="mtb-24" :place="place"/>
      <apple-map ref="map" class="Map border-3 overflow-hidden mt-24">
        <apple-map-pin-annotation :lat-lng="place.location.latLng"/>
      </apple-map>
    </portal>
  </div>
</template>

<script>
  import PlaceAside from "../components/places/PlaceAside";
  import AppleMap from "../components/utils/map/AppleMap";
  import AppleMapPinAnnotation from "../components/utils/map/AppleMapPinAnnotation";
  import PlaceImages from "../components/places/PlaceImages";
  import PlaceStatus from "../components/places/PlaceStatus";
  import PlaceArticles from "../components/places/PlaceArticles";
  import PlaceCreatedBy from "../components/places/PlaceCreatedBy";
  import PlaceAffiliates from "../components/places/PlaceAffiliates";
  import HorizontalList from "../components/utils/HorizontalList";
  import ArticleCard from "../components/article/ArticleCard";
  import Advert from "../components/utils/ads/Advert";
  import PlaceMentions from "../components/places/PlaceMentions";

  export default {
    components: {
      PlaceMentions,
      Advert,
      ArticleCard,
      HorizontalList,
      PlaceAffiliates,
      PlaceCreatedBy,
      PlaceArticles,
      PlaceStatus,
      PlaceImages,
      AppleMapPinAnnotation,
      AppleMap,
      PlaceAside
    },
    head() {
      const {image, name, description, slug, id} = this.place
      return this.$head({
        robots: {follow: true, index: true},
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
    asyncData({$api, error, params: {id}}) {
      return $api.get(`/places/${id}`, {params: {fields: 'articles,affiliates,images'}})
        .then(({data: place}) => {
          return {place}
        })
        .catch(err => {
          if (err.response.status === 404) {
            return error({statusCode: 404, message: 'Place Not Found'})
          }
          throw err
        })
    },
    data() {
      return {
        state: null
      }
    },
    mounted() {
      const {slug, id} = this.place
      this.$path.replace({path: `/${slug}-${id}`})

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
  .Map {
    height: 216px;
  }

  @media (max-width: 992px) {
    aside {
      display: none;
    }
  }

  @media (min-width: 992px) {
    section > .Summary {
      display: none;
    }

    aside {
      display: initial;
      flex: 0 0 300px;
      max-width: 300px;

      .Summary {
        position: sticky;
        top: calc(24px + 72px /*Header72px*/);
      }
    }
  }

  /*section {*/
  /*  @media (min-width: 992px) {*/
  /*    max-width: calc(100% - 300px - 24px);*/
  /*  }*/
  /*}*/

  /*aside {*/
  /*  @media (min-width: 992px) {*/
  /*    width: 300px;*/

  /*    position: sticky;*/
  /*    top: calc(24px + 72px !*Header72px*!);*/
  /*    !*left: calc(100vw - 300px - 24px);*!*/
  /*  }*/

  /*  @media (min-width: 1200px) {*/
  /*    !*left: calc(100vw - 300px - 80px);*!*/
  /*  }*/

  /*  @media (min-width: 1400px) {*/
  /*    !*left: initial;*!*/
  /*    !*right: calc((100vw - 1400px) / 2 + 80px);*!*/
  /*  }*/
  /*}*/
</style>
