<template>
  <div>
    <div ref="annotation">
      <div class="PlaceAnnotation hover-pointer" :class="{Hover: focused, Selected: selected}" @click="onClick">
        <div class="AnnotationDialog">
          <nuxt-link class="DialogArea Selected initial" :to="'/places/' + place.placeId" v-if="selected" @click.capture="$track.view(`RIP`, 'Map - Annotation')">
            <image-size v-if="image" class="Image index-content border-3-top" :image="image" :alt="place.name"/>
            <div v-else class="Image bg-whisper100"/>

            <div class="Content">
              <div class="Name Title large weight-600 b-a80">{{place.name}}</div>
              <div class="Tags">
                <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
                     :class="{'bg-peach100 weight-600 b-a80': tag.type === 'price', 'bg-whisper100 weight-400': tag.type !== 'price'}">
                  {{tag.name}}
                </div>
              </div>
            </div>
          </nuxt-link>
          <div class="DialogArea Label" v-else>{{place.name}}</div>
        </div>
        <div class="AnnotationTriangle"/>
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
      focused: {
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
      focused(focused) {
        this.$annotation.selected = focused
        this.selected = false
      }
    },
    methods: {
      onClick() {
        this.selected = true
        this.$annotation.selected = true

        this.$parent.$map.showItems([this.$annotation], {
          animate: true,
          padding: new mapkit.Padding(64, 64, 64, 64)
        })
      },
    },
    mounted() {
      const coordinate = AppleMap.Coordinate(this.place.location.latLng)
      this.$annotation = new mapkit.Annotation(coordinate, (coordinate, options) => {
        return this.$refs.annotation
      }, {data: this.place})

      const map = this.$parent.$map
      if (map) {
        map.addAnnotation(this.$annotation)
      }
    },
    beforeDestroy() {
      const map = this.$parent.$map
      if (map) {
        map.removeAnnotation(this.$annotation)
      }
    }
  }
</script>

<style lang="less" scoped>
  @Primary500: #F05F3B;

  .PlaceAnnotation {
    &.Hover, &:not(.Selected):hover {
      .Label {
        color: white;
        border: 1px solid @Primary500;
        background-color: @Primary500;
      }

      .AnnotationTriangle {
        &:after {
          border-top: 9px solid @Primary500;
        }
      }
    }
  }

  .AnnotationDialog {
    position: absolute;
    bottom: 30px;

    width: 200px;
    min-height: 30px;
    max-height: 250px;

    display: flex;
    justify-items: flex-end;
    align-content: center;

    > .DialogArea {
      position: relative;
      margin: auto auto 0;

      border-radius: 3px;
      background-color: rgb(249, 249, 253);

      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    }
  }

  .AnnotationDialog {
    .Label {
      min-width: 32px;
      max-width: 128px;
      height: 30px;
      padding: 2px 8px;

      border: 1px solid rgba(0, 0, 0, 0.08);

      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;

      color: rgba(0, 0, 0, 0.75);
      font-size: 15px;
      font-weight: 600;
      line-height: 24px;
      text-align: center;
    }
  }

  .AnnotationDialog {
    .Selected {
      width: 200px;

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

  .AnnotationTriangle {
    position: relative;
    margin-left: auto;
    margin-right: auto;

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
