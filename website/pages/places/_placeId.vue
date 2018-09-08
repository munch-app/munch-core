<template>
  <div class="ZeroSizing">
    <section class="Banner">
      <place-banner-image :images="images"/>
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
      <section class="Name Container">
        <h2>{{data.place.name}}</h2>
        <div class="Regular">{{data.place.location.street || data.place.location.address}}</div>
        <place-tags class="Tag" :tags="data.place.tags" :max="6"/>
      </section>

      <section class="Info">
        <b-container>
          <b-row>
            <b-col cols="12" md="8" lg="6" class="Detail">

            </b-col>
          </b-row>
        </b-container>
      </section>

      <!--<div class="IconList">
                <b-row class="IconRow" v-for="row in detailRows" :key="row.text">
                  <b-col cols="1"><img :src="row.icon" width="24px" height="24px"></b-col>
                  <b-col cols="11">{{row.text}}</b-col>
                </b-row>
              </div>
              <div class="Hour" v-if="hours">
                <opening-hours :hours="hours"/>
              </div>-->

      <section class="About" v-if="data.place.description || menus">
        <b-container>
          <b-row>
            <b-col cols="12" md="6" class="Description" v-if="data.place.description">
              <h4>About</h4>
              <p>{{data.place.description}}</p>
            </b-col>
            <b-col cols="12" md="6" class="Menu" v-if="menus">
              <h4>Menu</h4>
              <place-menus :menus="menus"/>
            </b-col>
          </b-row>
        </b-container>
      </section>

      <section class="Map">
        <!--<div class="GoogleMapDetail Text Elevation1 Border48 TagBg">-->
        <!--Level 4 Grand Park Orchard,-->
        <!--270 Orchard Road Singapore 238857-->
        <!--</div>-->

        <div class="Container">
          <google-embed-map :lat-lng="data.place.location.latLng" height="224"/>
        </div>
      </section>
    </section>

    <section class="Partner PlaceNavigationTab" v-if="hasPartner" :class="{'SelectedTab': tab === 'partner'}">
      <div class="Container">
        <h2 class="text-md-center">Partner's Content</h2>
      </div>

      <section class="Article" v-if="articles">
        <div class="Container">
          <h3>Articles</h3>
        </div>
        <partner-article :place-id="placeId" :preload="articles"/>
      </section>

      <section class="Instagram" v-if="instagramMedias">
        <div class="Container">
          <h3>Instagram</h3>
        </div>
        <partner-instagram-media :place-id="placeId" :preload="instagramMedias"/>
      </section>
    </section>

    <section class="End">
    </section>
  </div>
</template>

<script>
  import PlaceTags from "../../components/places/PlaceTags";
  import ImageSize from "../../components/core/ImageSize";
  import OpeningHours from "../../components/places/OpeningHours";
  import MunchButton from "../../components/core/MunchButton";
  import PlaceMenus from "../../components/places/PlaceMenus";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PartnerInstagramMedia from "../../components/places/PartnerInstagramMedia";
  import PartnerArticle from "../../components/places/PartnerArticle";
  import PlaceBannerImage from "../../components/places/PlaceBannerImage";

  export default {
    layout: 'search',
    components: {
      PlaceBannerImage,
      PartnerArticle,
      PartnerInstagramMedia,
      GoogleEmbedMap,
      PlaceMenus, MunchButton, OpeningHours, ImageSize, PlaceTags
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
      hasPartner() {
        return this.instagramMedias || this.articles
      },
      detailRows() {
        let rows = []
        // if (this.data.place.website) rows.push({icon: '/img/places/website.svg', text: this.data.place.website})
        if (this.data.place.phone) rows.push({icon: '/img/places/phone.svg', text: this.data.place.phone})
        if (this.data.place.price && this.data.place.price.perPax) rows.push({
          icon: '/img/places/price.svg',
          text: '~$' + this.data.place.price.perPax + '/pax'
        })
        return rows
      },
      hours() {
        if (this.data.place.hours && this.data.place.hours.length > 0) {
          return this.data.place.hours
        }
      },
      menus() {
        let menus = []
        if (this.data.place.menu && this.data.place.menu.url) {
          menus.push({
            data: this.data.place.menu.url,
            type: 'url'
          })
        }

        if (this.data.place.menu && this.data.place.menu.images) {
          this.data.place.menu.images.forEach(function (menu) {
            menus.push({
              data: menu,
              type: 'image'
            })
          })
        }

        if (menus.length > 0) {
          return menus
        }
      },
      images() {
        if (this.data.place.images && this.data.place.images.length > 0) {
          return this.data.place.images
        }
      },
      articles() {
        if (this.data.articles && this.data.articles.length > 0) {
          return this.data.articles
        }
      },
      instagramMedias() {
        if (this.data.instagram.medias && this.data.instagram.medias.length > 0) {
          return this.data.instagram.medias
        }
      }
    }
  }
</script>


<style lang="less" scoped>
  /**
   Feature is only enable if < 576
   */
  section.PlaceNavigation {
    display: flex;
    margin-bottom: 16px;

    @media (min-width: 576px) {
      display: none;
    }

    & > div {
      font-size: 14px;
      margin: 10px 10px 10px 0;
      padding: 7px 10px;
      border-radius: 3px;
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

  section.Name {
    .Tag {
      margin-top: 8px;
    }
  }

  section.Partner {
    h2 {
      display: none;
    }

    @media (min-width: 576px) {
      margin: 48px 0;

      h2 {
        display: block;
      }
    }

    .Article {
      margin: 12px 0;
    }

    .Instagram {
      margin: 12px 0;
    }
  }

  .Info {
    margin-top: 16px;

    .Detail {
      .Location {
        margin-top: -10px;
        margin-bottom: 8px;
      }

      .Tags {
        margin-top: 8px;
      }

      .IconList {
        margin-top: 16px;

        .IconRow {
          margin-bottom: 8px;
        }
      }

      .Hour {
        margin-top: 16px;
      }
    }
  }

  .About {
    margin: 48px 0;

    .Description {

    }

    .Menu {
    }
  }

  .Map {
    margin: 48px 0;

    .GoogleMap {
      z-index: -1;
      height: 224px;
    }

    .GoogleMapMarker {
    }

    .GoogleMapDetail {
      z-index: 0;
      padding: 10px 12px;
      width: 320px;

      margin-left: 16px;
      margin-top: 16px;
      position: absolute;
    }
  }

  .End {
    margin-top: 48px;
  }
</style>
