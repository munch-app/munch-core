<template>
  <div class="FeedArticleCard">
    <h2 class="Title">{{article.title}}</h2>
    <p class="Content">{{article.content}}</p>

    <h5 class="DomainName secondary-500">{{article.domain.name}}</h5>

    <div class="Places">
      <h6 class="text-uppercase weight-700 black-a-75">Places</h6>
      <div class="PlaceList">
        <div class="PlaceItem" v-for="place in places" :key="place.placeId">
          <image-size class="Image" :image="place.images[0]"/>
          <h6>
            {{place.name}}
          </h6>
        </div>
      </div>
    </div>
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
    }
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none;
    color: black;
  }

  .FeedArticleCard {
    padding-top: 24px;
    padding-bottom: 24px;

    max-width: 800px;
  }

  .FeedArticleCard {
    .Title {
      margin-bottom: 8px;
    }

    .Content {
      margin-bottom: 16px;
    }

    .DomainName {
      margin-bottom: 16px;
    }
  }

  .Places {
    margin-top: 16px;

    .PlaceList {
      display: flex;

      .PlaceItem {
        margin-right: 16px;
      }
    }

    .Image {
      width: 100px;
      height: 100px;
    }
  }
</style>
