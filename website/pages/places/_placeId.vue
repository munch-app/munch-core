<template>
  <div class="zero-spacing PlacePage">
    <section class="Banner">
      <place-image-banner :images="images" @onClickImage="(index) => $refs.imageWall.onClickImage(index)"/>
    </section>

    <section class="Information">
      <div class="container">
        <section class="Name ContentBody">
          <h1>{{place.name}}</h1>
          <div class="regular text">{{place.location.neighbourhood || place.location.street ||
            place.location.address}}
          </div>
          <place-tag-list class="Tag" :tags="place.tags" :max="10"/>
        </section>

        <section class="Action">
          <no-ssr>
            <div v-if="isLoggedIn" @click="showAddToCollection = true"
                 class="Button elevation-1 elevation-hover-2 border-3 white-bg hover-pointer">
              <simple-svg class="Icon" fill="rgba(0,0,0,0.75)" filepath="/img/places/action_add_collection.svg"/>
            </div>
          </no-ssr>
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
      <profile-collection-add-place :place="place" v-if="showAddToCollection" @on-close="showAddToCollection = false"/>
    </no-ssr>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import PlaceTagList from "../../components/places/PlaceTagList";
  import ImageSize from "../../components/core/ImageSize";
  import MunchButton from "../../components/core/MunchButton";
  import PlaceMenu from "../../components/places/PlaceMenu";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PlaceBannerImage from "../../components/places/PlaceBannerImage";
  import PlaceAwardList from "../../components/places/PlaceAwardList";
  import PlaceLocation from "../../components/places/PlaceLocation";
  import PlaceAbout from "../../components/places/PlaceAbout";
  import PlaceDetail from "../../components/places/PlaceDetail";
  import ProfileCollectionAddPlace from "../../components/profile/ProfileCollectionAddPlace";
  import PlaceImageWall from "../../components/places/PlaceImageWall";
  import PlaceArticleList from "../../components/places/PlaceArticleList";
  import PlaceImageBanner from "../../components/places/PlaceImageBanner";

  const Activity = require('~/services/user/place-activity')

  let clickAction

  export default {
    components: {
      PlaceImageBanner,
      PlaceArticleList,
      PlaceImageWall,
      ProfileCollectionAddPlace, PlaceDetail, PlaceAbout, PlaceLocation, PlaceAwardList, PlaceBannerImage,
      GoogleEmbedMap, PlaceMenu, MunchButton, ImageSize, PlaceTagList
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
      ...mapGetters('user', ['isLoggedIn']),
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
      if (process.client) {
        const placeId = this.place.placeId
        Activity.start.call(this, placeId)

        clickAction = function () {
          const action = this.dataset.placeActivity
          const data = this.dataset.placeActivityData
          Activity.click[action](placeId, data)
        }

        // Track all place activity clicks
        document.querySelectorAll('a[data-place-activity]').forEach(anchor => {
          anchor.addEventListener("click", clickAction, true);
        })
      }
    },
    beforeDestroy() {
      if (process.client) {
        // Remove all place activity clicks
        document.querySelectorAll('a[data-place-activity]').forEach(anchor => {
          anchor.removeEventListener("click", clickAction, true)
        })
      }
    }
  }
</script>

<style lang="less" scoped>
  .PlacePage {
    margin-bottom: 64px;
  }

  section.ContentBody {
    width: 100%;

    @media (min-width: 992px) {
      width: 720px;
    }
  }

  section {
    h2 {
      margin-bottom: 16px;
    }
  }

  section.Name {
    margin-top: 24px;

    .Tag {
      margin-top: 8px;
    }
  }

  section.Action {
    .Button {
      margin-right: 16px;
      margin-top: 16px;
      padding: 8px;
      width: 40px;
      height: 40px;
    }
  }

  section.MainDetail {
    margin-top: 48px;
  }

  section.About {
    margin-top: 48px;
  }

  section.Location {
    margin-top: 48px;
  }

  section.Menu {
    margin-top: 48px;
  }

  section.Article {
    margin-top: 48px;
  }

  section.Images {
    margin-top: 48px;
  }

  section.End {
    margin-top: 64px;
  }
</style>
