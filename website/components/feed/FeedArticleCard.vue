<template>
  <div class="FeedArticleCard">
    <a :href="article.url" target="_blank" rel="noreferrer noopener nofollow">
      <image-size class="Image index-content border-3" :image="article.thumbnail"/>
      <h3 class="Title text-ellipsis-2l">{{article.title}}</h3>
      <p class="Content text-ellipsis-4l">{{article.content}}</p>
    </a>

    <div class="Bottom">
      <a class="BrandButton" :href="article.domain.url"
         target="_blank" rel="noreferrer noopener nofollow">
        <simple-svg class="Icon" fill="black" filepath="/img/feed/article.svg"/>
        <div class="Name text text-ellipsis-1l secondary-700">{{article.domain.name}}</div>
      </a>

      <div class="PlaceButton elevation-1 border-3 border hover-pointer elevation-hover-2" @click="onClick">
        <simple-svg class="Icon" fill="rgba(0, 0, 0, 0.85)" filepath="/img/feed/place.svg"/>
        <div class="Label">Places Mentioned</div>
      </div>
    </div>
    <hr>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "FeedArticleCard",
    components: {ImageSize},
    props: {
      item: {
        required: true,
        type: Object
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
      onClick() {
        this.$emit('view')
      }
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .FeedArticleCard {
    max-width: 680px;
  }

  .FeedArticleCard {
    .Image {
      width: 100%;
      padding-top: 25%;
      margin-bottom: 16px;
    }

    .Title {
      margin-bottom: 8px;
    }

    .Content {
      margin-bottom: 16px;
    }
  }

  .Bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;

    margin-bottom: 32px;

    .BrandButton {
      display: flex;
      align-items: center;

      margin-right: 8px;

      .Icon {
        flex-shrink: 0;

        width: 18px;
        height: 18px;
      }

      .Name {
        flex-grow: 1;
        flex-shrink: 1;

        margin-left: 6px;

        font-size: 15px;
        font-weight: 600;

        line-height: 21px;
        height: 21px;
      }
    }

    .PlaceButton {
      padding: 6px 10px 6px 8px;
      display: inline-flex;
      flex-shrink: 0;

      .Icon {
        width: 20px;
        height: 20px;
      }

      .Label {
        margin-left: 4px;
        margin-right: 2px;

        height: 20px;
        line-height: 20px;

        font-weight: 600;
        font-size: 13px;

        color: rgba(0, 0, 0, 0.75);
      }
    }
  }
</style>
