<template>
  <div>
    <h2>Discover Locations</h2>

    <horizontal-scroll-view class="ClusterList container-remove-gutter" :items="areas" :map-key="a => a.areaId"
                            :padding="18">
      <template slot-scope="{item}">
        <div class="ClusterCard elevation-hover-1 hover-pointer" @click="onClick(item)">
          <image-size class="Image" :image="item.images && item.images[0]"/>
          <div class="Name">{{item.name}}</div>
        </div>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import ImageSize from "../../core/ImageSize";
  import HorizontalScrollView from "../../core/HorizontalScrollView";

  export default {
    name: "SearchCardAreaClusterList",
    components: {HorizontalScrollView, ImageSize},
    props: {
      areas: {
        type: Array,
        required: true
      }
    },
    methods: {
      onClick(area) {
        this.$store.dispatch('filter/location', {areas: [area], type: 'Where'})
        this.$store.dispatch('search/start')

        this.$track.search(`Search - Card: Area Cluster`, this.$store.getters['search/locationType'])
      }
    }
  }
</script>

<style scoped lang="less">
  .ClusterList {
    margin-top: 18px;
    height: 112px !important;
  }

  .ClusterCard {
    border: 1px solid #dddddd;
    border-radius: 3px;
    width: 120px;
    height: 110px;

    .Image {
      width: 100%;
      height: 64px;
      overflow: hidden;
    }

    .Name {
      height: 38px;
      line-height: 1.2;
      padding: 6px 6px 0;
      font-size: 13px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.75);

      overflow: hidden;
      -webkit-line-clamp: 2;
      display: -webkit-box;
      -webkit-box-orient: vertical;
    }
  }
</style>
