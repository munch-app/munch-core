<template>
  <portal to="dialog-blank" slot-scope="{scroll}">
    <div class="ArticleDialog" id="dialog-portal-scroll" v-on-clickaway="onClose">

      <div class="Navigation index-navigation white-bg elevation-1">
        <div>
          <h4 class="Title text-ellipsis-1l">{{article.title}}</h4>
        </div>
        <div class="Close hover-pointer" @click="onClose">
          <simple-svg class="Icon" fill="black" :filepath="require('~/assets/icon/close.svg')"/>
        </div>
      </div>

      <section class="Content">
        <div class="Article">
          <a :href="article.url" target="_blank" rel="noreferrer noopener nofollow">
            <image-size class="Image index-content border-3" :image="article.thumbnail"/>
            <h3 class="Title text-ellipsis-2l">{{article.title}}</h3>
            <p class="Content text-ellipsis-4l">{{article.content}}</p>
          </a>

          <a class="BrandButton" :href="article.domain.url">
            <simple-svg class="Icon" fill="black" :filepath="require('~/assets/icon/feed/article.svg')"/>
            <div class="Name text text-ellipsis-1l secondary-700">{{article.domain.name}}</div>
          </a>
        </div>
        <div class="Place">
          <h4 class="Title secondary-700 text-uppercase">Places</h4>

          <div class="PlaceList">
            <div class="PlaceItem index-content" v-for="place in places" :key="place.placeId">
              <place-card :place="place"/>
            </div>
          </div>
        </div>
      </section>
    </div>
  </portal>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  import PlaceCard from "../places/PlaceCard";

  export default {
    name: "FeedSelectedArticleDialog",
    components: {PlaceCard, ImageSize},
    props: {
      item: {
        type: Object,
        required: true
      },
    },
    computed: {
      article() {
        return this.item.article
      },
      places() {
        return this.item.places.map(({placeId}) => {
          return this.$store.getters['feed/articles/getPlace'](placeId)
        }).filter(place => place)
      }
    },
    methods: {
      onClose() {
        this.$emit('close')
      },
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .ArticleDialog {
    background: white;
    border-radius: 3px;

    overflow-y: auto;
    -webkit-overflow-scrolling: touch;

    @media (max-width: 575.98px) {
      margin-top: 56px;
      height: calc(100vh - 56px);
      width: 100vw;
    }
    @media (min-width: 576px) {
      max-height: 90vh;
      max-width: 400px;
    }

    section.Content {
      padding: 24px 24px 64px 24px;
    }
  }

  .Navigation {
    padding: 16px 24px;
    position: sticky;
    top: 0;

    display: flex;
    align-content: center;
    justify-content: space-between;

    .Title {
      font-size: 17px;

      line-height: 24px;
      height: 24px;
    }

    .Close {
      .Icon {
        width: 24px;
        height: 24px;

        margin-left: auto;
        margin-right: 0;
      }
    }
  }

  .Article {
    .Image {
      width: 100%;
      padding-top: 28%;
      margin-bottom: 16px;
    }

    .Title {
      margin-bottom: 8px;
    }

    .Content {
      margin-bottom: 16px;
      font-size: 15px;
    }

    .BrandButton {
      display: flex;
      align-items: center;

      margin-right: 8px;

      .Icon {
        flex-shrink: 0;

        width: 16px;
        height: 16px;
      }

      .Name {
        flex-grow: 1;
        flex-shrink: 1;

        margin-left: 6px;

        font-size: 13px;
        font-weight: 600;

        line-height: 21px;
        height: 21px;
      }
    }
  }

  .Place {
    .Title {
      margin-top: 24px;
      margin-bottom: 24px;

      font-weight: 700;
      font-size: 17px;
    }

    .PlaceList {
      display: flex;
      flex-direction: column;

      margin-top: -16px;
      margin-bottom: -16px;

      .PlaceItem {
        padding-top: 16px;
        padding-bottom: 16px;
      }
    }
  }
</style>
