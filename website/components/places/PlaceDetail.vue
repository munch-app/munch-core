<template>
  <div class="Detail">
    <div class="Row Price" v-if="place.price && place.price.perPax">
      <div class="Left Secondary500">PRICE</div>
      <div class="Right">~${{place.price.perPax.toFixed(1)}}/pax</div>
    </div>
    <div class="Row Phone" v-if="place.phone">
      <div class="Left Secondary500">PHONE</div>
      <div class="Right">{{place.phone}}</div>
    </div>
    <div class="Row Website" v-if="websiteUrl">
      <div class="Left Secondary500">WEBSITE</div>
      <div class="Right">{{websiteUrl}}</div>
    </div>
    <div class="Row WebsiteMenu" v-if="menuUrl">
      <div class="Left Secondary500">MENU</div>
      <div class="Right">{{menuUrl}}</div>
    </div>
    <div class="Row Hour" v-if="hours.length > 0">
      <div class="Left Secondary500">HOURS</div>
      <place-hour-list class="Right" :hours="hours"/>
    </div>
    <div></div>
  </div>
</template>

<script>
  import PlaceHourList from "./PlaceHourList";
  function extractHostname(url) {
    let hostname;
    //find & remove protocol (http, ftp, etc.) and get hostname

    if (url.indexOf("//") > -1) {
      hostname = url.split('/')[2];
    }
    else {
      hostname = url.split('/')[0];
    }

    //find & remove port number
    hostname = hostname.split(':')[0];
    //find & remove "?"
    hostname = hostname.split('?')[0];

    return hostname.toLowerCase();
  }


  export default {
    name: "PlaceDetail",
    components: {PlaceHourList},
    props: {
      place: {
        type: Object,
        required: true
      },
    },
    computed: {
      websiteUrl() {
        const website = this.place.website
        if (website) {
          return extractHostname(website)
        }
      },
      menuUrl() {
        const url = this.place.menu && this.place.menu.url
        if (url) {
          return extractHostname(url)
        }
      },
      hours() {
        if (this.place.hours) {
          return this.place.hours
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Detail {
    margin-bottom: -8px;

    .Row {
      display: flex;
    }

    .Row .Left, .Row .Right {
      line-height: 24px;
      margin-bottom: 8px;
    }

    .Row .Left {
      font-size: 14px;
      font-weight: 700;
    }

    .Row .Right {
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }

    @media (max-width: 575.98px) {
      .Row .Right {
        padding-left: 12px;
        text-align: right;
        flex-grow: 1;
      }
    }

    @media (min-width: 576px) {
      .Row .Left {
        width: 120px;
      }
    }
  }
</style>
