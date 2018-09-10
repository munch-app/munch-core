<template>
  <div class="ZeroSpacing">
    <section class="Banner">
      <place-banner-image :images="place.images"/>
    </section>

    <section class="PlaceNavigation NavBg Container">
      <div class="Text" :class="{'Primary300Bg White': tab === 'information'}"
           @click="tab = 'information'">
        Information
      </div>
      <div class="Text" :class="{'Primary300Bg White': tab === 'partner'}"
           v-if="hasPartner" @click="tab = 'partner'">
        Partner's Content
      </div>
    </section>

    <section class="Information PlaceNavigationTab" :class="{'SelectedTab': tab === 'information'}">
      <div class="Container">
        <section class="Name ContentBody">
          <h1>{{place.name}}</h1>
          <div class="Regular Street">{{place.location.street || place.location.address}}</div>
          <place-tag-list class="Tag" :tags="place.tags" :max="6"/>
        </section>

        <section class="MainDetail ContentBody">
          <!--<place-hour-list :hours="hours"/>-->
        </section>

        <section class="About ContentBody">
          <place-about :place="place" :awards="data.awards"/>
        </section>

        <section class="Location ContentBody">
          <place-location :location="place.location"/>
        </section>
      </div>
    </section>

    <section class="Partner PlaceNavigationTab" v-if="hasPartner" :class="{'SelectedTab': tab === 'partner'}">
      <div class="Container">
        <h2 class="Secondary500 Header">Partner's Content</h2>
      </div>

      <section class="Article" v-if="data.articles.length > 0">
        <div class="Container">
          <h2>Articles</h2>
        </div>
        <partner-article :place-id="placeId" :preload="data.articles"/>
      </section>

      <section class="Instagram" v-if="data.instagram.medias.length > 0">
        <div class="Container">
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
  import PlaceHourList from "../../components/places/PlaceHourList";
  import MunchButton from "../../components/core/MunchButton";
  import PlaceMenuList from "../../components/places/PlaceMenuList";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PartnerInstagramMedia from "../../components/places/PartnerInstagramMedia";
  import PartnerArticle from "../../components/places/PartnerArticle";
  import PlaceBannerImage from "../../components/places/PlaceBannerImage";
  import PlaceAwardList from "../../components/places/PlaceAwardList";
  import PlaceLocation from "../../components/places/PlaceLocation";
  import PlaceAbout from "../../components/places/PlaceAbout";

  export default {
    layout: 'search',
    components: {
      PlaceAbout,
      PlaceLocation,
      PlaceAwardList,
      PlaceBannerImage,
      PartnerArticle,
      PartnerInstagramMedia,
      GoogleEmbedMap,
      PlaceMenuList, MunchButton, PlaceHourList, ImageSize, PlaceTagList
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
    data() {
      return {
        tab: 'information'
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
</script>


<style lang="less" scoped>
  section.Banner {
    @media (min-width: 576px) {
      margin-top: 16px;
    }
  }

  /**
   Feature is only enable if < 576
   */
  section.PlaceNavigation {
    display: flex;

    @media (min-width: 576px) {
      display: none;
    }

    & > div {
      font-size: 14px;
      margin: 10px 10px 10px 0;
      padding: 7px 10px;
      border-radius: 3px;

      &:hover {
        cursor: pointer;
      }
    }
  }

  /**
   Feature is only enable if < 576
   */
  section.PlaceNavigationTab {
    display: none;

    &.SelectedTab {
      display: block;
    }

    @media (min-width: 576px) {
      &.Information, &.Partner {
        display: block;
      }
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
    margin-top: 24px;
  }

  section.About {
    margin-top: 24px;
  }

  section.Location {
    margin-top: 24px;
  }

  section.Partner {
    margin: 24px 0;

    .Header {
      display: none;
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

    @media (min-width: 576px) {
      margin: 48px 0;

      .Header {
        display: block;
        margin-bottom: 16px;
      }

      .Article {
        margin: 8px 0;
      }

      .Instagram {
        margin: 8px 0;
      }
    }
  }

  section.End {
    margin-top: 48px;
  }
</style>
