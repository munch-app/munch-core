<template>
  <nuxt-link class="CollectionCard hover-pointer" :to="`/collections/${collection.collectionId}`">
    <image-size class="Image" :image="collection.image">
      <div class="flex-center ImageContainer">
        <h3 class="White TextCenter">{{collection.name}}</h3>
        <div class="Username"></div>
      </div>
    </image-size>
  </nuxt-link>
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
    color: initial;
    text-decoration: initial;
  }

  .Image {
    border-radius: 4px;
    overflow: hidden;
  }

  .ImageContainer {
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
    background-color: rgba(0,0,0,0.55);
    width: 200px;
    height: 200px;
    padding: 24px;

    &:hover {
      background-color: rgba(0,0,0,0.33);
    }

    @media (max-width: 575.98px) {
      width: 160px;
      height: 160px;
      padding: 16px;
    }

    .Username {
      position: absolute;
      bottom: 8px;
      right: 8px;
      color: white;

      font-weight: 600;
      font-size: 14px;
      line-height: 20px;
    }
  }
</style>
