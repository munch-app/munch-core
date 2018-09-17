<template>
  <div class="CollectionCard HoverPointer">
    <image-size class="Image" :image="collection.image">
      <div class="FlexCenter ImageContainer">
        <h3 class="White TextCenter">{{collection.name}}</h3>
      </div>
    </image-size>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "UserPlaceCollectionCard",
    components: {ImageSize},
    props: {
      collection: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .CollectionCard {

  }

  .Image {
    border-radius: 4px;
    overflow: hidden;
  }

  .ImageContainer {
    background-color: rgba(0,0,0,0.6);
    width: 200px;
    height: 200px;
    padding: 24px;

    @media (max-width: 575.98px) {
      width: 160px;
      height: 160px;
      padding: 16px;
    }
  }
</style>
