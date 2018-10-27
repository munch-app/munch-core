<template>
  <div>
    <div ref="annotation">
      <div class="PlaceAnnotationLarge hover-pointer" v-if="selected">
        <nuxt-link class="AnnotationDialog border-3 initial" :to="'/places/' + place.placeId">
          <image-size v-if="image" class="Image index-content border-3-top" :image="image" :alt="place.name"/>
          <div v-else class="Image whisper-100-bg"/>

          <div class="Content">
            <div class="Name Title large weight-600 black-a-80">{{place.name}}</div>
            <div class="Tags">
              <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
                   :class="{'peach-100-bg weight-600 black-a-80': tag.type === 'price', 'whisper-100-bg weight-400': tag.type !== 'price'}">
                {{tag.name}}
              </div>
            </div>
          </div>
        </nuxt-link>
      </div>

      <div class="PlaceAnnotation hover-pointer" :class="{Hover: highlight}" v-else @click="selected = true">
        <div class="AnnotationDialog">
          {{place.name}}
        </div>
        <div class="Triangle"/>
      </div>
    </div>
  </div>
</template>

<script>
  import AppleMap from './AppleMap'
  import ImageSize from "./ImageSize";
  import PlaceCard from "../places/PlaceCard";

  export default {
    name: "AppleMapPlaceAnnotation",
    components: {PlaceCard, ImageSize},
    props: {
      place: {
        type: Object,
        required: true,
      },
      highlight: {
        type: Boolean,
        default: () => false
      }
    },
    data() {
      return {selected: false}
    },
    computed: {
      location() {
        return this.place.location.neighbourhood || this.place.location.street
      },
      image() {
        if (this.place.images && this.place.images[0]) {
          return this.place.images[0]
        }
      },
      tags() {
        const perPax = this.place.price && this.place.price.perPax
        const priceTax = perPax && [{type: 'price', name: `$${perPax.toFixed(0)}`}] || []

        return [
          ...priceTax,
          ...this.place.tags
        ]
      },
    },
    watch: {
      highlight(highlight) {
        this.$annotation.selected = highlight
        this.selected = false
      }
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.place.location.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      }, {data: this.place})

      const map = this.$parent.$map
      map.addAnnotation(this.$annotation)
    },
    beforeDestroy() {
      const map = this.$parent.$map
      map.removeAnnotation(this.$annotation)
    }
  }
</script>

<style lang="less" scoped>
  @Primary500: #F05F3B;

  .PlaceAnnotation {
    height: 38px;
    position: relative;

    .AnnotationDialog {
      position: relative;
      margin-left: auto;
      margin-right: auto;

      min-width: 32px;
      max-width: 128px;
      height: 30px;

      padding: 2px 8px;
      text-align: center;

      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;

      border: 1px solid rgba(0, 0, 0, 0.08);
      background-color: rgb(249, 249, 253);
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);

      border-radius: 3px;

      color: rgba(0, 0, 0, 0.75);
      font-size: 15px;
      font-weight: 600;
      line-height: 24px;
    }

    &.Hover, &:hover {

      .AnnotationDialog {
        color: white;
        border: 1px solid @Primary500;
        background-color: @Primary500;
      }

      .Triangle {
        &:after {
          border-top: 9px solid @Primary500;
        }
      }
    }
  }

  .PlaceAnnotationLarge {
    position: relative;
    width: 200px;

    .AnnotationDialog {
      position: absolute;
      margin-left: auto;
      margin-right: auto;

      width: 200px;

      /*padding: 2px 8px;*/

      background: rgb(249, 249, 253);
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);

      .Image {
        width: 100%;
        padding-top: 60%;
      }

      .Content {
        padding: 8px 12px 12px;

        .Name {
          height: 26px;
          line-height: 26px;
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }

        .Tags {
          display: flex;
          flex-wrap: wrap;
          align-items: flex-start;
          overflow: hidden;

          min-height: 24px;
          max-height: 64px;
          margin-top: 4px;
          margin-bottom: -8px;

          .Tag {
            font-size: 12px;
            line-height: 24px;
            padding: 0 8px;
            margin-right: 8px;
            margin-bottom: 8px;
          }
        }
      }
    }
  }

  .Triangle {
    position: relative;

    &:after, &:before {
      position: absolute;
      left: 0;
      right: 0;

      margin-top: -1px;
      margin-left: auto;
      margin-right: auto;

      content: "";
      display: block;
      width: 0;
      height: 0;
    }

    &:after {
      margin-top: -2px;
      border-left: 9px solid transparent;
      border-right: 9px solid transparent;

      border-top: 9px solid rgb(249, 249, 253);
    }

    &:before {
      border-left: 9px solid transparent;
      border-right: 9px solid transparent;

      border-top: 9px solid rgba(0, 0, 0, 0.15);
    }
  }
</style>
