<template>
  <div class="Page container-768 zero">
    <div class="Header">
      <h2 v-if="placeId">Suggest Edits: <span class="s700">{{data.place.name}}</span></h2>
      <h2 v-else>Suggest a new edit</h2>
    </div>

    <div class="Navigation hr-bot flex">
      <div class="Tab border border-4-top weight-600 hover-pointer" v-for="tab in tabs" :key="tab.component"
           :class="{'bg-s300 white': selected === tab.component}" @click="selected = tab.component">
        {{tab.name}}
      </div>
    </div>

    <div class="Content">
      <keep-alive>
        <component :data="data" :is="selected"/>
      </keep-alive>
    </div>

    <div class="Changes">
      <h2>Changes</h2>
      <div class="List">

      </div>
      <div class="flex-row-end">
        <button class="primary">Submit</button>
      </div>
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
              data: {
                place: data.place,
                images: data.images,
                articles: data.articles,
              },
            }
          })
      } else {
        return {
          placeId: null, // To determine it's a new entry.
          data: {
            place: {location: {}, price: {}, menu: {}},
            images: [],
            articles: [],
          },
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
        ],
        changes: {}
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
    overflow-x: scroll;

    .Tab {
      font-size: 15px;
      line-height: 20px;
      padding: 8px 16px;
      margin-right: 16px;

      border-style: solid solid none solid;
    }
  }

  .Content {
    margin-top: 24px;
  }

  .Changes {
    margin-top: 48px;

    .List {
      margin-top: 24px;
      margin-bottom: 24px;
    }
  }
</style>
