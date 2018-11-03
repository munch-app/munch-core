<template>
  <div class="Page container-800 zero">
    <div class="Header">
      <h2 v-if="placeId">Suggest Edits: <span class="s700">{{place.name}}</span></h2>
      <h2 v-else>Suggest a new edit</h2>
    </div>

    <div class="Navigation flex">
      <div class="Tab bg-p050 border-3 weight-600 b-a75 hover-pointer" v-for="tab in tabs" :key="tab.component"
           @click="selected = tab.component">
        {{tab.name}}
      </div>
    </div>

    <div class="Content">
      <keep-alive>
        <place-suggest-detail v-bind:is="selected"/>
        <place-suggest-menu v-bind:is="selected"/>
        <place-suggest-image v-bind:is="selected"/>
        <place-suggest-article v-bind:is="selected"/>
      </keep-alive>
    </div>

    <div class="Action flex-row-end">
      <button class="primary">Submit</button>
    </div>
  </div>
</template>

<script>
  import ImageSizes from "../../components/core/ImageSizes";
  import PlaceSuggestDetail from "../../components/places/suggest/PlaceSuggestDetail";
  import PlaceSuggestArticle from "../../components/places/suggest/PlaceSuggestArticle";
  import PlaceSuggestImage from "../../components/places/suggest/PlaceSuggestImage";
  import PlaceSuggestMenu from "../../components/places/suggest/PlaceSuggestMenu";

  export default {
    components: {PlaceSuggestMenu, PlaceSuggestImage, PlaceSuggestArticle, PlaceSuggestDetail, ImageSizes},
    asyncData({$axios, params, route}) {
      const placeId = route.query.placeId
      if (placeId) {
        return $axios.$get(`/api/places/${placeId}`)
          .then(({data}) => {
            return {
              placeId,
              place: data.place,
              images: data.images,
              articles: data.articles,
            }
          })
      } else {
        return {
          placeId: null, // To determine it's a new entry.
          place: {}
        }
      }
    },
    data() {
      return {
        selected: 'PlaceSuggestDetail',
        tabs: [
          {name: 'Detail', component: 'PlaceSuggestDetail'},
          {name: 'Menu', component: 'PlaceSuggestMenu'},
          {name: 'Image', component: 'PlaceSuggestImage'},
          {name: 'Article', component: 'PlaceSuggestArticle'},
        ]
      }
    },
    computed: {}
  }
</script>

<style scoped lang="less">
  .Page {
    margin-top: 32px;
    margin-bottom: 64px;
  }

  .Header {
    h4 {
      margin-top: 8px;
    }
  }

  .Navigation {
    margin-top: 24px;

    .Tab {
      line-height: 20px;
      padding: 10px 20px;
      margin-right: 16px;
    }
  }

  .Content {
    margin-top: 24px;
  }

  .Action {
    margin-top: 48px;
  }
</style>
