<template>
  <div class="zero-spacing">
    <section class="Banner" v-if="place.images.length > 0">
      <place-banner-image :images="place.images"/>
    </section>

    <section class="Information">
      <div class="container">
        <section class="Name ContentBody">
          <h1>{{place.name}}</h1>
          <div class="regular Street">{{place.location.neighbourhood || place.location.street || place.location.address}}</div>
          <place-tag-list class="Tag" :tags="place.tags" :max="6"/>
        </section>

        <section class="MainDetail ContentBody">
          <place-detail :place="place"/>
        </section>

        <section class="About ContentBody">
          <place-about :place="place" :awards="data.awards"/>
        </section>

        <section class="Location ContentBody">
          <place-location :location="place.location"/>
        </section>
      </div>
    </section>

    <section class="Partner" v-if="hasPartner">
      <div class="container">
        <h2 class="Secondary500 Header">Partner's Content</h2>
      </div>

      <section class="Article" v-if="data.articles.length > 0">
        <div class="container">
          <h2>Articles</h2>
        </div>
        <partner-article :place-id="placeId" :preload="data.articles"/>
      </section>

      <section class="Instagram" v-if="data.instagram.medias.length > 0">
        <div class="container">
          <h2>Instagram</h2>
        </div>
        <partner-instagram-media :place-id="placeId" :preload="data.instagram.medias"/>
      </section>
    </section>

    <section class="End">
    </section>
  </div>
</template>

<script>
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

  export default {
    layout: 'search',
    components: {
      PlaceDetail,
      PlaceAbout,
      PlaceLocation,
      PlaceAwardList,
      PlaceBannerImage,
      PartnerArticle,
      PartnerInstagramMedia,
      GoogleEmbedMap,
      PlaceMenuList, MunchButton, ImageSize, PlaceTagList
    },
    head() {
      const description = this.data.place.description
      if (description) {
        return {
          title: this.data.place.name + ' | Munch',
          meta: [{hid: 'description', name: 'description', content: description}]
        }
      }
      return {
        title: this.data.place.name + ' | Munch',
      }
    },
    asyncData({$axios, params}) {
      return $axios.$get('/api/places/' + params.placeId)
        .then(({data}) => {
          return {data: data};
        });
    },
    computed: {
      placeId() {
        return this.data.place.placeId
      },
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
    }
  }

  // TODO RIP Activity Store
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
    margin-top: 16px;

    .Street {
      margin-top: 8px;
    }

    .Tag {
      margin-top: 8px;
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
