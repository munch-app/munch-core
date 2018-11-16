<template>
  <div class="zero">
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
  import ImageSizes from "../../components/core/ImageSizes";
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
      GoogleEmbedMap, PlaceMenu, ImageSizes, PlaceTagList
    },
    head() {
      const place = this.place
      const image = place.images && place.images[0] && ImageSizes.$$findUrl(place.images[0].sizes, 300, 300)

      const tags = [place.name, ...place.tags.map(t => t.name)]
      if (place.location.neighbourhood) tags.push(place.location.neighbourhood)
      const keywords = tags.filter(tag => !!tag).join(',')

      return this.$head({
        robots: {follow: true, index: place.taste.group >= 2},
        graph: {
          title: `${place.name} · Munch Singapore`,
          description: place.description,
          type: 'place',
          image: image,
          url: `https://www.munch.app/places/${place.placeId}`,
          keywords,
        },
        breadcrumbs: [
          {
            name: 'Places',
            item: 'https://www.munch.app/places'
          },
          {
            name: place.name,
            item: `https://www.munch.app/places/${place.placeId}`
          },
        ]
      })
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    asyncData({$api, $axios, params, error}) {
      return $api.get(`/places/${params.placeId}`)
        .then(({data}) => {
          return {data}
        }).catch((err) => {
          if (err.statusCode === 404) error({statusCode: 404, message: 'Place Not Found'})
          else if (err.statusCode) error(err)
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

<style scoped lang="less">
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
