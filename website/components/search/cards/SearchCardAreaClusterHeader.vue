<template>
  <div>
    <div class="SearchCard Secondary400Bg Border4 ZeroSpacing">
      <h2 class="White Text Weight600">{{area.name}}</h2>
      <div class="Content">
        <image-size v-if="image" class="Image Border4" :image="image"/>
        <div class="Text White">
          <div>{{area.description}}</div>
          <div class="LastLine">
            <div class="Count">
              <span class="Weight600">{{area.counts.total}} </span>
              <span>food spots</span>
            </div>
            <div class="Address">{{area.location.address}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ImageSize from "../../core/ImageSize";

  export default {
    name: "SearchCardAreaClusterHeader",
    components: {ImageSize},
    props: {
      area: {
        type: Object,
        required: true
      }
    },
    computed: {
      image() {
        return this.area.images && this.area.images[0]
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchCard {
    padding: 24px 24px;
    overflow: hidden;
  }

  .Content {
    margin-top: 8px;
    display: flex;
  }

  .Image {
    z-index: 1;
    padding-left: 150px;
    padding-top: 100px;
    margin-right: 24px;
  }

  .LastLine {
    display: flex;
    margin-top: 8px;

    .Count {
      margin-right: 32px;
    }
  }

  @media (max-width: 575.98px) {
    .Content {
      flex-direction: column;
    }

    .Image {
      width: 100%;
      margin-bottom: 16px;
    }
  }
</style>
