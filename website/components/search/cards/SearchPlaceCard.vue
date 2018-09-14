<template>
  <nuxt-link class="col-12 col-md-4 col-lg-3" :to="'/places/' + place.placeId">
    <div class="Elevation1 Card Border48">
      <image-size class="Image Border48Top" :image="images" :alt="place.name"/>

      <div class="Content">
        <div class="Name Title Large">{{place.name}}</div>
        <div class="LocationDistanceTiming">Location + Distance + Timing</div>
        <div class="Tags">
          <div class="Tag TagBg Border24" v-for="tag in tags" :key="tag.tagId">
            {{tag.name}}
          </div>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import ImageSize from "../../core/ImageSize";

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
      },
      tags() {
        return this.place.tags.slice(0, 3)
      }
    }
  }
</script>

<style scoped lang="less">
  .Card {
    margin-top: 12px;
    margin-bottom: 12px;

    &:hover {
      cursor: pointer;
    }

    .Image {
      width: 100%;
      padding-top: 55%;
    }

    .Content {
      padding: 8px 16px 16px 16px;

      .Name {
        height: 26px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;

        margin-top: 8px;
        font-weight: bolder !important;
      }

      .LocationDistanceTiming {
        margin-top: 4px;
        height: 24px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
      }
      .Tags {
        display: flex;
        flex-wrap: wrap;
        align-items: flex-start;
        overflow: hidden;

        height: 34px;
        margin-top: 8px;
        margin-bottom: -8px;

        .Tag {
          font-size: 12px;
          padding: 4px 8px;
          margin-right: 8px;
          margin-bottom: 8px;
        }
      }

    }
  }

</style>
