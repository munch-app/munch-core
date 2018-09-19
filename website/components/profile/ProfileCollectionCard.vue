<template>
  <nuxt-link class="CollectionCard hover-pointer" :to="`/collections/${collection.collectionId}`">
    <image-size class="Image" :image="collection.image">
      <div class="GreyscaleContainer">
        <div class="flex-center ImageContainer">
          <div class="Heading Name White text-ellipsis-1-line">{{collection.name}}</div>
        </div>
      </div>
    </image-size>
  </nuxt-link>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "ProfileCollectionCard",
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

    .GreyscaleContainer {
      position: relative;
      padding-left: 8px;
      padding-right: 8px;
      padding-top: 45%;
      transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
      background-color: rgba(0, 0, 0, 0.55);

      &:hover {
        background-color: rgba(0, 0, 0, 0.33);
      }

      .Name {
        font-size: 19px;
      }

      @media (max-width: 767.98px) {
        padding-top: 80%;

        .Name {
          font-size: 17px;
        }
      }

      @media (max-width: 991.98px) {
        padding-top: 70%;

        .Name {
          font-size: 18px;
        }
      }
    }
  }

  .ImageContainer {
    position: absolute;
    top: 0;
    left: 8px;
    right: 8px;
    bottom: 0;

    height: 100%;
  }
</style>
