<template>
  <div class="zero-spacing">
    <section class="Banner" v-if="place.images.length > 0">
      <place-banner-image :images="place.images"/>
    </section>

    <section class="Information">
      <div class="container">
        <section class="Name ContentBody">
          <h1>{{place.name}}</h1>
          <div class="regular Street">{{place.location.neighbourhood || place.location.street ||
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

        <section class="Location ContentBody">
          <place-location :place-id="place.placeId" :location="place.location"/>
        </section>
      </div>
    </section>

    <section class="Partner" v-if="hasPartner">
      <!--<div class="container">-->
        <!--<h2 class="secondary-500 Header">Partner's Content</h2>-->
      <!--</div>-->

      <section class="Article" v-if="data.articles.length > 0">
        <div class="container">
          <h2>Recent Articles</h2>
        </div>
        <partner-article :place-id="place.placeId" :preload="data.articles"/>
      </section>

      <section class="Instagram" v-if="data.instagram.medias.length > 0">
        <div class="container">
          <h2>Image Gallery</h2>
        </div>
        <partner-instagram-media :place-id="place.placeId" :preload="data.instagram.medias"/>
      </section>
    </section>

    <section class="End">
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
  import PlaceMenuList from "../../components/places/PlaceMenuList";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PartnerInstagramMedia from "../../components/places/PartnerInstagramMedia";
  import PartnerArticle from "../../components/places/PartnerArticle";
  import PlaceBannerImage from "../../components/places/PlaceBannerImage";
  import PlaceAwardList from "../../components/places/PlaceAwardList";
  import PlaceLocation from "../../components/places/PlaceLocation";
  import PlaceAbout from "../../components/places/PlaceAbout";
  import PlaceDetail from "../../components/places/PlaceDetail";
  import ProfileCollectionAddPlace from "../../components/profile/ProfileCollectionAddPlace";

  const Activity = require('~/services/user/place-activity')

  let clickAction

  export default {
    components: {
      ProfileCollectionAddPlace, PlaceDetail, PlaceAbout, PlaceLocation, PlaceAwardList, PlaceBannerImage,
      PartnerArticle, PartnerInstagramMedia, GoogleEmbedMap, PlaceMenuList, MunchButton, ImageSize, PlaceTagList
    },
    head() {
      const title = this.data.place.name + ' | Munch'
      const description = this.data.place.description
      const tags = this.data.place.tags.map(tag => tag.name).join(',')

      const meta = []
      meta.push({name: 'robots', content: `follow,${this.hasPartner ? 'index' : 'noindex'}`})
      if (description) meta.push({hid: 'description', name: 'description', content: description})
      if (tags) meta.push({name: 'keywords', content: tags})
      return {title, meta}
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    asyncData({$axios, params}) {
      return $axios.$get('/api/places/' + params.placeId)
        .then(({data}) => {
          return {data: data};
        });
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
      place() {
        return this.data.place
      },
      hasPartner() {
        return this.data.articles.length > 0 || this.data.instagram.medias.length > 0
      },
      hours() {
        if (this.data.place.hours && this.data.place.hours.length > 0) {
          return this.data.place.hours
        }
      }
    },
    methods: {},
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
    destroyed() {
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
  section.Banner {
    @media (min-width: 576px) {
      margin-top: 16px;
    }
  }

  section.ContentBody {
    width: 100%;

    @media (min-width: 992px) {
      width: 720px;
    }
  }

  section.Name {
    margin-top: 32px;

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
    margin-top: 32px;
  }

  section.About {
    margin-top: 32px;
  }

  section.Location {
    margin-top: 32px;
  }

  section.Partner {
    margin: 32px 0;

    .Header {
      margin-bottom: 16px;
    }

    .Article {
      margin: 12px 0;

      h2 {
        margin-bottom: 8px;
      }
    }

    .Instagram {
      margin: 16px 0;

      h2 {
        margin-bottom: 8px;
      }
    }
  }

  section.End {
    margin-top: 48px;
  }
</style>
