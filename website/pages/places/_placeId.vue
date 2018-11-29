<template>
  <div class="zero">
    <section class="Banner">
      <place-image-banner :images="images" @onClickImage="(index) => $refs.imageWall.onClickImage(index)"/>
    </section>

    <section class="Information container flex-justify-between relative">
      <div class="ContentBody flex-grow">
        <section class="Max720 Name">
          <div v-if="place.status.type === 'closed'">
            <h3 class="error">Permanently Closed</h3>
          </div>

          <h1>{{place.name}}</h1>
          <div class="regular">
            {{place.location.neighbourhood || place.location.street || place.location.address}}
          </div>

          <place-tag-list class="mt-8" :tags="place.tags" :max="10"/>
        </section>

        <section class="Max720">
          <place-detail :place="place"/>
        </section>

        <section>
          <place-about :place="place" :awards="data.awards"/>
        </section>

        <section v-if="menu" class="Max720">
          <h2>Menu</h2>
          <place-menu :menu="menu"/>
        </section>

        <section class="Max720">
          <h2>Location</h2>
          <place-location :place-id="place.placeId" :location="place.location"/>
        </section>

        <section v-if="articles">
          <div>
            <h2>{{place.name}} Articles</h2>
          </div>
        </section>
      </div>

      <aside class="mt-24">
        <place-floating-panel class="FloatingPanel" :place="place"/>
      </aside>
    </section>

    <section class="Images mb-64" v-if="images">
      <div class="container">
        <h2>{{place.name}} Images</h2>
      </div>

      <place-image-wall ref="imageWall" :place-id="place.placeId" :preload="images"/>
    </section>
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
  import PlaceFloatingPanel from "../../components/places/PlaceFloatingPanel";

  export default {
    components: {
      PlaceFloatingPanel,
      PlaceAction,
      PlaceImageBanner,
      PlaceArticleList,
      PlaceImageWall,
      PlaceDetail, PlaceAbout, PlaceLocation, PlaceAwardList,
      GoogleEmbedMap, PlaceMenu, ImageSizes, PlaceTagList
    },
    head() {
      const {placeId, name, description, images, taste} = this.place

      return this.$head({
        robots: {follow: true, index: taste.group >= 2},
        graph: {
          title: `${name} · Munch Singapore`,
          description: description,
          type: 'place',
          image: images && images[0] && ImageSizes.$$findUrl(images[0].sizes, 300, 300),
          url: `https://www.munch.app/places/${placeId}`,
        },
        breadcrumbs: [
          {
            name: 'Places',
            item: 'https://www.munch.app/places'
          },
          {
            name: name,
            item: `https://www.munch.app/places/${placeId}`
          },
        ]
      })
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    asyncData({$api, $axios, params: {placeId}, error}) {
      return $api.get(`/places/${placeId}`)
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
  section {
    margin-top: 48px;

    h2 {
      margin-bottom: 16px;
    }
  }

  .Name {
    margin-top: 24px;
  }

  .Information,
  .Banner {
    margin-top: 0;
  }

  .Information {
    @media (max-width: 991.98px) {
      flex-direction: column;
    }
  }

  .Max720 {
    max-width: 720px;
  }

  .ContentBody {
    @media (min-width: 992px) {
      margin-right: 48px;
    }
  }

  aside {
    width: 360px;
    flex-grow: 1;
  }

  .FloatingPanel {
    @media (max-width: 991.98px) {
      border-radius: 0;
      border-style: solid none none none;

      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
    }

    @media (min-width: 992px) {
      position: sticky;
      top: 80px;
    }
  }

</style>
