<template>
  <div class="zero PlacePage">
    <section class="Banner">
      <place-image-banner :images="images" @onClickImage="(index) => $refs.imageWall.onClickImage(index)"/>
    </section>

    <section class="Information">
      <div class="container">
        <section class="Name ContentBody w-100">
          <div v-if="place.status.type === 'closed'">
            <h3 class="error">
              Permanently Closed
            </h3>
          </div>

          <h1>{{place.name}}</h1>
          <div class="regular text">{{place.location.neighbourhood || place.location.street ||
            place.location.address}}
          </div>
          <place-tag-list class="mt-8" :tags="place.tags" :max="10"/>
        </section>

        <section class="MainDetail ContentBody">
          <place-detail :place="place"/>
        </section>

        <section class="About ContentBody">
          <place-about :place="place" :awards="data.awards"/>
        </section>

        <section class="Menu ContentBody" v-if="menu">
          <h2>Menu</h2>
          <place-menu :menu="menu"/>
        </section>

        <section class="Location ContentBody">
          <h2>Location</h2>
          <place-location :place-id="place.placeId" :location="place.location"/>
        </section>
      </div>
    </section>

    <section class="Article" v-if="articles">
      <place-article-list :place-id="place.placeId" :preload="articles"/>
    </section>

    <section class="Images" v-if="images">
      <div class="container">
        <h2>{{place.name}} Images</h2>
      </div>

      <place-image-wall ref="imageWall" :place-id="place.placeId" :preload="images"/>
    </section>

    <no-ssr>
      <place-action :place="place"/>
    </no-ssr>
  </div>
</template>

<script>
  import PlaceTagList from "../../components/places/PlaceTagList";
  import ImageSize from "../../components/core/ImageSize";
  import PlaceMenu from "../../components/places/PlaceMenu";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PlaceAwardList from "../../components/places/PlaceAwardList";
  import PlaceLocation from "../../components/places/PlaceLocation";
  import PlaceAbout from "../../components/places/PlaceAbout";
  import PlaceDetail from "../../components/places/PlaceDetail";
  import PlaceImageWall from "../../components/places/PlaceImageWall";
  import PlaceArticleList from "../../components/places/PlaceArticleList";
  import PlaceImageBanner from "../../components/places/PlaceImageBanner";
  import PlaceAction from "../../components/places/PlaceAction";

  export default {
    components: {
      PlaceAction,
      PlaceImageBanner,
      PlaceArticleList,
      PlaceImageWall,
      PlaceDetail, PlaceAbout, PlaceLocation, PlaceAwardList,
      GoogleEmbedMap, PlaceMenu, ImageSize, PlaceTagList
    },
    head() {
      const meta = []

      const index = this.place.taste.group >= 2 ? 'index' : 'noindex'
      meta.push({name: 'robots', content: `follow,${index}`})

      if (this.place.description) {
        meta.push({hid: 'description', name: 'description', content: this.place.description})
      }

      const tags = [this.place.name, ...this.place.tags.map(tag => tag.name)]
      if (this.place.location.neighbourhood) {
        tags.push(this.place.location.neighbourhood)
      }
      meta.push({name: 'keywords', content: tags.filter(tag => !!tag).join(',')})

      return {
        title: `${this.place.name} · Munch Singapore`, meta,
        __dangerouslyDisableSanitizers: ['script'],
        script: [
          {
            type: 'application/ld+json',
            innerHTML: JSON.stringify({
                "@context": "http://schema.org",
                "@type": "BreadcrumbList",
                "itemListElement": [{
                  "@type": "ListItem",
                  "position": 1,
                  "name": "Places",
                  "item": "https://www.munch.app/places"
                }, {
                  "@type": "ListItem",
                  "position": 2,
                  "name": this.place.name,
                  "item": "https://www.munch.app/places/" + this.place.placeId
                }]

              }
            )
          }
        ]
      }
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    asyncData({$axios, params, error}) {
      return $axios.$get('/api/places/' + params.placeId)
        .then(({data}) => {
          return {data: data};
        })
        .catch((err) => {
          const response = err.response
          if (response && response.status === 404) {
            error({statusCode: 404, message: 'Place Not Found'})
          } else {
            error({statusCode: 500, message: err.message})
          }
        })
    },
    computed: {
      place() {
        return this.data.place
      },
      hours() {
        if (this.data.place.hours && this.data.place.hours.length > 0) {
          return this.data.place.hours
        }
      },
      menu() {
        if (this.data.place.menu && this.data.place.menu.url) {
          return this.data.place.menu
        }
      },
      images() {
        if (this.data.images.length > 0) {
          return this.data.images
        }
      },
      articles() {
        if (this.data.articles.length > 0) {
          return this.data.articles
        }
      }
    },
    mounted() {
      const placeId = this.place.placeId
      const Activity = require('~/services/user/place-activity')
      Activity.start.call(this, placeId)

      this.$clickAction = function () {
        const action = this.dataset.placeActivity
        const data = this.dataset.placeActivityData
        Activity.click[action](placeId, data)
      }

      // Track all place activity clicks
      document.querySelectorAll('a[data-place-activity]').forEach(anchor => {
        anchor.addEventListener("click", this.$clickAction, true);
      })
    },
    beforeDestroy() {
      // Remove all place activity clicks
      document.querySelectorAll('a[data-place-activity]').forEach(anchor => {
        anchor.removeEventListener("click", this.$clickAction, true)
      })
    }
  }
</script>

<style lang="less" scoped>
  .PlacePage {
  }

  .ContentBody {
    @media (min-width: 992px) {
      max-width: 720px;
    }
  }

  section {
    margin-top: 48px;
    h2 {
      margin-bottom: 16px;
    }
  }

  .Information,
  .Name {
    margin-top: 24px;
  }

  .Banner {
    margin-top: 0;
  }
</style>
