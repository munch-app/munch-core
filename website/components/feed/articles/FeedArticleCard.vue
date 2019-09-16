<template>
  <div class="FeedArticleCard">
    <a :href="article.url" class="Article" target="_blank" rel="noreferrer noopener nofollow">
      <image-size class="Image index-content border-3" :image="article.thumbnail"/>

      <h3 class="Title text-ellipsis-1l">{{article.title}}</h3>

      <div class="Author">
        <h6>by <span class="s700">{{item.author}}</span> on {{format(article.createdMillis, 'mmm dd, yyyy')}}</h6>
      </div>

      <p class="Content text-ellipsis-3l">{{article.description}}</p>
    </a>

    <div class="Places">
      <h4 class="s700" v-if="places.length > 1">Places Mentioned</h4>
      <h4 class="s700" v-else>Place Mentioned</h4>

      <horizontal-scroll-view class="PlaceList container-remove-gutter" :items="places" :map-key="i => i.placeId"
                              :padding="24">
        <template slot-scope="{item}">
          <div class="Card">
            <place-card :place="item" small/>
          </div>
        </template>
      </horizontal-scroll-view>
    </div>
    <hr>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import ImageSize from "../../core/ImageSize";
  import PlaceCard from "../../places/PlaceCard";
  import HorizontalScrollView from "../../core/HorizontalScrollView";

  export default {
    name: "FeedArticleCard",
    components: {HorizontalScrollView, PlaceCard, ImageSize},
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
      format: dateformat,
      onClick() {
        this.$emit('view')
      }
    }
  }
</script>

<style scoped lang="less">
  .FeedArticleCard {
    max-width: 700px;
  }

  .Article {
    .Image {
      width: 100%;
      padding-top: 22%;
    }

    .Title {
      margin-top: 16px;
    }

    .Author {
      margin-top: 4px;
      display: block;
    }

    .Content {
      margin-top: 16px;
    }
  }

  .Places {
    margin-top: 24px;
    margin-bottom: 16px;

    .PlaceList {
      margin-top: 16px;
      height: 204px;

      .Card {
        width: 140px;
        height: 204px;
      }
    }
  }
</style>
