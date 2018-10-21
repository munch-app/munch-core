<template>
  <portal to="dialog-blank">
    <div class="InstagramDialog" v-on-clickaway="onClose">
      <div class="Navigation">
        <div class="Close hover-pointer" @click="onClose">
          <simple-svg class="Icon" fill="black" filepath="/img/feed/close.svg"/>
        </div>
      </div>

      <image-size class="border-3 index-content" :image="item.image" grow="height">
        <div class="ImageContainer hover-pointer" @click="onClickPlace">
          <a class="Author" target="_blank" rel="noreferrer nofollow noopener"
             :href="`https://instagram.com/${item.instagram.username}`">
            <simple-svg class="Icon" fill="white" filepath="/img/feed/instagram.svg"/>
            <div class="Name">{{item.author}}</div>
          </a>
        </div>
      </image-size>

      <div class="Place hover-pointer" v-if="place" @click="onClickPlace">
        <h2 class="Name secondary-500-hover">{{place.name}}</h2>
        <h6 class="Location">{{location}}</h6>

        <div class="Tags">
          <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
               :class="{'peach-100-bg weight-600 black-a-80': tag.type === 'price', 'whisper-100-bg weight-400': tag.type !== 'price'}">
            {{tag.name}}
          </div>
        </div>
      </div>
    </div>
  </portal>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "FeedSelectedInstagramDialog",
    components: {ImageSize},
    props: {
      item: {
        type: Object,
        required: true
      },
    },
    computed: {
      place() {
        const placeId = this.item.places[0] && this.item.places[0].placeId
        if (placeId) {
          return this.$store.getters['feed/images/getPlace'](placeId)
        }
      },
      location() {
        return this.place.location.neighbourhood ||
          this.place.location.street ||
          this.place.location.address
      },
      tags() {
        const perPax = this.place.price && this.place.price.perPax
        const priceTax = perPax && [{type: 'price', name: `$${perPax.toFixed(0)}`}] || []

        return [
          ...priceTax,
          ...this.place.tags
        ]
      },
    },
    methods: {
      onClickPlace() {
        this.$router.push({path: '/places/' + this.place.placeId})
      },
      onClose() {
        this.$emit('close')
      },
    }
  }
</script>

<style scoped lang="less">
  .InstagramDialog {
    background: white;
    padding: 16px 24px 24px;
    border-radius: 3px;

    max-height: 100vh;
    max-width: 400px;

    @media (max-width: 575.98px) {
      max-width: 576px;
    }
  }

  .Navigation {
    margin-bottom: 16px;

    .Close {
      .Icon {
        width: 24px;
        height: 24px;

        margin-left: auto;
        margin-right: 0;
      }
    }
  }

  .ImageContainer {
    height: 100%;
    width: 100%;
    padding: 10px;

    display: flex;
    flex-direction: column;
    justify-content: flex-end;

    .Author {
      display: flex;

      .Icon {
        width: 24px;
        height: 24px;
      }

      .Name {
        margin-left: 3px;

        height: 24px;
        line-height: 22px;
        font-size: 15px;
        font-weight: 600;
        color: white;
      }
    }
  }

  .Place {
    margin-top: 12px;

    .Name {

    }

    .Location {
      margin-top: 4px;
    }

    .Tags {
      margin-top: 16px;

      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      overflow: hidden;

      min-height: 24px;
      max-height: 64px;
      margin-bottom: -8px;

      .Tag {
        font-size: 12px;
        line-height: 24px;
        padding: 0 8px;
        margin-right: 8px;
        margin-bottom: 8px;
      }
    }
  }
</style>
