<template>
  <div class="zero">
    <section class="Banner">
      <place-image-banner :images="images" @onClickImage="(index) => $refs.imageWall.onClickImage(index)"/>
    </section>

    <section class="Information container flex-justify-between relative">
      <div class="ContentBody flex-grow">
        <section class="Max720 mt-32">
          <place-status :place="place"/>

          <h1>{{place.name}}</h1>
          <div class="regular">
            {{place.location.neighbourhood || place.location.street || place.location.address}}
          </div>

          <place-tag-list class="mt-8" :tags="place.tags"/>
        </section>

        <section class="Max720 mtb-32">
          <place-detail :place="place"/>
        </section>

        <section v-if="data.awards.length > 0 || place.description" class="Max720">
          <place-about :place="place" :awards="data.awards"/>
        </section>

        <section v-if="menu" class="Max720 mtb-48">
          <h2 class="mb-16">Menu</h2>
          <place-menu :menu="menu"/>
        </section>

        <section class="Max720 mtb-48">
          <h2 class="mb-16">Location</h2>
          <place-location :place-id="place.placeId" :location="place.location"/>
        </section>

        <section class="mb-32 mt-64" v-if="articles">
          <h2 class="mb-32">{{place.name}} Articles</h2>
          <place-article-wall :place-id="place.placeId" :preload="articles"/>
        </section>
      </div>

      <aside class="mt-24 flex-grow">
        <place-floating-panel class="FloatingPanel" :place="place" :user="user"/>
      </aside>
    </section>

    <section class="mb-64" v-if="images">
      <div class="container">
        <h2 class="mb-32">{{place.name}} Images</h2>
      </div>

      <place-image-wall ref="imageWall" :place-id="place.placeId" :preload="images"/>
    </section>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  import PlaceTagList from "../../components/places/PlaceTagList";
  import ImageSizes from "../../components/core/ImageSizes";
  import PlaceMenu from "../../components/places/PlaceMenu";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PlaceAwardList from "../../components/places/PlaceAwardList";
  import PlaceLocation from "../../components/places/PlaceLocation";
  import PlaceAbout from "../../components/places/PlaceAbout";
  import PlaceDetail from "../../components/places/PlaceDetail";
  import PlaceImageWall from "../../components/places/PlaceImageWall";
  import PlaceImageBanner from "../../components/places/PlaceImageBanner";
  import PlaceFloatingPanel from "../../components/places/PlaceFloatingPanel";
  import PlaceArticleWall from "../../components/places/PlaceArticleWall";
  import PlaceStatus from "../../components/places/PlaceStatus";

  export default {
    components: {
      PlaceStatus,
      PlaceArticleWall,
      PlaceFloatingPanel,
      PlaceImageBanner,
      PlaceImageWall,
      PlaceDetail, PlaceAbout, PlaceLocation, PlaceAwardList,
      GoogleEmbedMap, PlaceMenu, ImageSizes, PlaceTagList
    },
    head() {
      const {placeId, name, description, images} = this.place

      return this.$head({
        robots: {follow: true, index: true},
        graph: {
          title: `${name} · Munch - Food Discovery App`,
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
    mounted() {
      if (!this.isLoggedIn) return

      return this.$api.put(`/users/recent/places/${this.place.placeId}`)
        .catch(error => {
          this.$store.dispatch('addError', error)
        })
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
      place() {
        return this.data.place
      },
      user() {
        return this.data.user
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
      },
    }
  }
</script>

<style scoped lang="less">
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
    max-width: 360px;
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
      top: calc(24px + 72px /*Header72px*/);
    }
  }

</style>
