<template>
  <div>
    <h2>Discover Locations</h2>

    <div class="ClusterList">
      <div class="ClusterContainer" v-for="area in areas" :key="area.areaId" @click="onClick(area)">
        <div class="ClusterCard">
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
    display: flex;
    overflow-x: scroll;
    overflow-y: hidden;
    -webkit-overflow-scrolling: touch;

    .ClusterContainer {
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
      height: 70px;
      overflow: hidden;
    }

    .Name {
      height: 40px;
      line-height: 1.2;
      padding: 4px 6px 0;
      font-size: 13px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.75);

      text-overflow: ellipsis;
      overflow: hidden;
      -webkit-line-clamp: 2;
    }
  }
</style>
