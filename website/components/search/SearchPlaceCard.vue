<template>
  <a class="Card Border48" :href="'/places/' + place.placeId">
    <div class="Elevation1">
      <image-size class="Image Border48Top BorderImage" :image="images" :alt="place.name"/>

      <div class="Content">
        <p class="Title Large">{{place.name}}</p>
        <p class="Location Small">{{location}}</p>
      </div>
    </div>
  </a>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  export default {
    name: "SearchPlaceCard",
    components: {ImageSize},
    props: {
      place: {
        required: true,
        type: Object
      }
    },
    computed: {
      images() {
        if (this.place.images && this.place.images[0]) {
          return this.place.images[0]
        }
        return []
      },
      location() {
        return this.place.location.street || this.place.location.address
      }
    }
  }
</script>

<style scoped lang="less">
  .Card {
    &:hover {
      cursor: pointer;
    }

    .Image {
      width: 100%;
      padding-top: 55%;
    }

    .Content {
      padding: 8px;

      p {
        margin: 0;
        padding: 0;
      }

      .Name {
      }

      .Location {
        margin-top: -2px;
      }
    }
  }

</style>
