<template>
  <div class="Detail">
    <div class="flex-align-center flex-justify-between tablet" v-if="place.price && place.price.perPax">
      <h5>Price</h5>
      <p class="text-ellipsis-1l">~${{place.price.perPax.toFixed(1)}}/pax</p>
    </div>
    <div class="flex-align-center flex-justify-between tablet" v-if="place.phone">
      <h5>Phone</h5>
      <p class="text-ellipsis-1l">{{place.phone}}</p>
    </div>
    <div class="flex-align-center flex-justify-between" v-if="websiteUrl">
      <h5>Website</h5>
      <a :href="place.website" class="text-ellipsis-1l" target="_blank" rel="noreferrer noopener nofollow">http://{{websiteUrl}}</a>
    </div>
    <div class="flex-align-center flex-justify-between tablet" v-if="place.hours.length > 0">
      <h5>Hours</h5>
      <place-hour-list :place-id="place.placeId" :hours="place.hours"/>
    </div>
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
    }
  }
</script>

<style scoped lang="less">
  .Detail {
    margin-bottom: -16px;

    > div {
      margin-bottom: 16px;
    }

    h5 {
      width: 80px;
      margin-right: 18px;
      text-transform: uppercase;
    }
  }

  @media (min-width: 768px) {
    .Detail > div {
      justify-content: flex-start;
    }

    h5 {
      margin-right: 32px;
    }
  }
</style>
