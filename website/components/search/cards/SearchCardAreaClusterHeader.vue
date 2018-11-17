<template>
  <div>
    <div class="SearchCard overflow-hidden p-24 bg-saltpan200 border-4 zero">
      <h2 class="text weight-600">{{area.name}}</h2>
      <div class="Content flex mt-8">
        <image-size v-if="image" class="Image border-4" :image="image"/>


        <div class="text">
          <div>{{area.description}}</div>

          <div class="mt-8 flex">
            <div class="Count">
              <span class="weight-600">{{area.counts.total}} </span>
              <span>food spots</span>
            </div>

            <div>{{area.location.address}}</div>
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
  .Image {
    z-index: 1;
    padding-left: 150px;
    padding-top: 100px;
    margin-right: 24px;

    max-width: 280px;
  }

  .Count {
    margin-right: 32px;
  }

  @media (max-width: 575.98px) {
    .SearchCard {
      margin-left: -24px;
      margin-right: -24px;
      border-radius: 0;
    }

    .Content {
      flex-direction: column;
    }

    .Image {
      width: 100%;
      margin-bottom: 16px;
    }
  }
</style>
