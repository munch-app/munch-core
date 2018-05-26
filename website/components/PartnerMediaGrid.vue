<template>
  <div>
    <b-container class="InstagramImage">
      <b-row no-gutters>
        <b-col cols="4" md="3" v-for="media in parsed" :key="media.mediaId">
          <a target="_blank" :href="media.url" rel="noopener noreferrer">
            <b-img :src="media.image" :title="media.caption" fluid/>
          </a>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
  import axios from 'axios';

  function parseImage(media) {
    for (const imageKey in media.images) {
      if (imageKey.startsWith('320x')) {
        return media.images[imageKey];
      }
    }
    return media.images.values.first();
  }

  export default {
    data: function () {
      return {
        parsed: []
      }
    },
    async mounted() {
      let instagramReq = axios.get('https://api.partner.munchapp.co/v1/instagram/public/medias/12')
      let instagramRes = await instagramReq
      this.parsed = instagramRes.data.data.map(function (media) {
        return {
          url: "https://www.instagram.com/" + media.username + "/",
          image: parseImage(media),
          caption: media.caption
        }
      });

    }
  }
</script>

<style>
  .InstagramImage a img {
    object-fit: cover;
    width: 320px;
    height: 230px;
    padding: 8px;
  }

  @media screen and (max-width: 992px) {
    .InstagramImage a img {
      object-fit: cover;
      width: 200px;
      height: 130px;
    }
  }

  @media screen and (max-width: 500px) {
    .InstagramImage a img {
      object-fit: cover;
      width: 160px;
      height: 100px;
    }
  }
</style>
