<template>
  <div>
    <section class="Info">
      <b-container>
        <b-row>
          <b-col cols="12" md="8" lg="6" class="Detail">
            <h2>{{data.place.name}}</h2>
            <p class="Location Large">{{data.place.location.street || data.place.location.address}}</p>
            <place-tags class="Tags" :tags="data.place.tags" :max="5"/>

            <div class="IconList">
              <b-row class="IconRow" v-for="row in detailRows" :key="row.text">
                <b-col cols="1"><img :src="row.icon" width="24px" height="24px"></b-col>
                <b-col cols="11">{{row.text}}</b-col>
              </b-row>
            </div>
            <div class="Hour" v-if="hours">
              <opening-hours :hours="hours"/>
            </div>
          </b-col>
          <b-col class="d-none d-sm-block d-md-block" md="4" lg="6" v-if="images">
            <place-images :images="images"/>
          </b-col>
        </b-row>
      </b-container>
    </section>

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
      <b-container class="MapContainer">
        <!--<div class="GoogleMapDetail Elevation1 Border48 TagBg">-->
          <!--Level 4 Grand Park Orchard,-->
          <!--270 Orchard Road Singapore 238857-->
        <!--</div>-->

        <google-embed-map :lat-lng="data.place.location.latLng" height="224"/>
      </b-container>
    </section>

    <section class="Partner" v-if="instagramMedias || articles">
      <h2 class="text-center">Partner’s Content</h2>

      <section class="Article" v-if="articles">
        <b-container>
          <h2>Articles</h2>
          <article-collection :place-id="placeId" :articles="articles"/>
        </b-container>
      </section>

      <section class="Instagram" v-if="instagramMedias">
        <h2 class="container">Instagram</h2>
        <partner-instagram-media :place-id="placeId" :preload="instagramMedias"/>
      </section>
    </section>
    <section class="End">
    </section>
  </div>
</template>

<script>
  import ArticleCard from "../../components/places/ArticleCard";
  import InstagramMediaCard from "../../components/places/InstagramMediaCard";
  import PlaceTags from "../../components/places/PlaceTags";
  import ImageSize from "../../components/core/ImageSize";
  import PlaceImages from "../../components/places/PlaceImages";
  import OpeningHours from "../../components/places/OpeningHours";
  import MunchButton from "../../components/core/MunchButton";
  import PlaceMenus from "../../components/places/PlaceMenus";
  import InstagramMediaCollection from "../../components/places/InstagramMediaCollection";
  import ArticleCollection from "../../components/places/ArticleCollection";
  import GoogleEmbedMap from "../../components/core/GoogleEmbedMap";
  import PartnerInstagramMedia from "../../components/places/PartnerInstagramMedia";

  export default {
    layout: 'search',
    components: {
      PartnerInstagramMedia,
      GoogleEmbedMap,
      ArticleCollection,
      InstagramMediaCollection,
      PlaceMenus, MunchButton, OpeningHours, PlaceImages, ImageSize, PlaceTags, InstagramMediaCard, ArticleCard
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

  .Partner {
    margin: 48px 0;

    .Article {
      margin: 24px 0;
    }

    .Instagram {
      margin-top: 48px;
      margin-bottom: 48px;
    }
  }

  .End {
    margin-top: 48px;
  }

</style>
