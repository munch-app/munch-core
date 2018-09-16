<template>
  <div>
    <h2>Discover Locations</h2>

    <div class="ClusterList">
      <div class="ClusterContainer" v-for="area in areas" :key="area.areaId" @click="onClick(area)">
        <div class="ClusterCard ElevationHover1">
          <image-size class="Image" :image="area.images && area.images[0]"></image-size>
          <div class="Name">
            {{area.name}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSize from "../../core/ImageSize";

  export default {
    name: "SearchCardAreaClusterList",
    components: {ImageSize},
    props: {
      areas: {
        type: Array,
        required: true
      }
    },
    methods: {
      onClick(area) {
        this.$store.dispatch('filter/location', area)
        this.$store.dispatch('filter/start')
        this.$store.dispatch('search/start', this.$store.state.filter.query)
      }
    }
  }
</script>

<style scoped lang="less">
  h2 {
    margin-bottom: 18px;
  }

  .ClusterList {
    height: 112px;
    display: flex;
    overflow-x: scroll;
    overflow-y: visible;
    -webkit-overflow-scrolling: touch;

    .ClusterContainer {
      overflow: visible;
      padding-right: 18px;

      &:hover {
        cursor: pointer;
      }
    }
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
