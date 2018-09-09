<template>
  <div class="ZeroSizing">
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
          <place-hour-list :hours="hours"/>
        </section>

        <section class="About ContentBody">
          <h2>About</h2>
          <p>{{place.description}}</p>
        </section>

        <section class="Menu ContentBody">
          <h2>Menu</h2>
          <place-menu-list :menus="menus"/>
        </section>

        <section class="Location ContentBody">
          <h2>Location</h2>
          <div class="Text">{{place.location.address}}</div>
          <google-embed-map :lat-lng="place.location.latLng" height="224"/>
        </section>
      </div>
    </section>

    <section class="Partner PlaceNavigationTab" v-if="hasPartner" :class="{'SelectedTab': tab === 'partner'}">
      <div class="Container">
        <h2 class="Primary">Partner's Content</h2>
      </div>

      <section class="Article" v-if="data.articles.length > 0">
        <div class="Container">
          <h3>Articles</h3>
        </div>
        <partner-article :place-id="placeId" :preload="data.articles"/>
      </section>

      <section class="Instagram" v-if="data.instagram.medias.length > 0">
        <div class="Container">
          <h3>Instagram</h3>
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

  export default {
    layout: 'search',
    components: {
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
      }
    }
  }
</script>


<style lang="less" scoped>
  h2, h3 {
    margin-bottom: 16px;
  }

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
    margin-top: 16px;
  }

  section.About {
    margin-top: 16px;
  }

  section.Menu {
    margin-top: 16px;
  }

  section.Location {
    margin-top: 16px;

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

  section.End {
    margin-top: 48px;
  }
</style>
