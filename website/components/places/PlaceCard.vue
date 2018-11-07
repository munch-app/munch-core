<template>
  <nuxt-link :to="'/places/' + place.placeId" @click.native="$track.view(`RIP`, 'PlaceCard')">
    <div class="Card relative" :class="{'Small': small}">
      <no-ssr>
        <place-card-add-collection class="absolute" :place="place"/>
      </no-ssr>

      <div class="aspect r-5-3 border-3 overflow-hidden" v-if="image">
        <image-sizes v-if="placeImage" :sizes="placeImage.sizes" :alt="place.name"/>
        <div v-else class="bg-whisper100"/>
      </div>

      <div class="Content">
        <div class="Name Title text-ellipsis-1l large weight-600 b-a80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag border-3" v-for="tag in placeTags" :key="tag.tagId" :class="{
               'bg-peach100 weight-600 b-a80': tag.type === 'price',
               'bg-whisper100 weight-400': tag.type !== 'price'}"
          >
            {{tag.name}}
          </div>
        </div>

        <div class="LocationDistanceTiming small text-ellipsis-1l">
          <span v-if="placeDistance">{{placeDistance}}, </span>
          <span class="weight-600 b-a80">{{placeLocation}}</span>
          <span v-if="placeTiming" class="b-a75 BulletDivider">•</span>
          <span v-if="placeTiming" :class="placeTiming.class">{{placeTiming.text}}</span>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import {Hour, HourGroup} from './hour-group'
  import ImageSizes from "../core/ImageSizes";
  import PlaceCardAddCollection from "./PlaceCardAddCollection";

  function abc1428240170610139() {

  }

  export default {
    name: "PlaceCard",
    components: {PlaceCardAddCollection, ImageSizes},
    props: {
      place: {
        type: Object,
        required: true,
      },
      small: {
        type: Boolean,
        default: false
      },
      image: {
        type: Boolean,
        default: true
      }
    },
    computed: {
      placeLocation() {
        return this.place.location.neighbourhood || this.place.location.street
      },
      placeImage() {
        if (this.place.images && this.place.images[0]) {
          return this.place.images[0]
        }
      },
      placeTags() {
        const perPax = this.place.price && this.place.price.perPax
        const priceTax = perPax && [{type: 'price', name: `$${perPax.toFixed(0)}`}] || []

        return [
          ...priceTax,
          ...this.place.tags
        ]
      },
      placeDistance() {
        return null
      },
      placeTiming() {
        if (this.place.hours.length === 0) return

        const group = new HourGroup(this.place.hours.map((h) => new Hour(h.day, h.open, h.close)))
        switch (group.isOpen()) {
          case 'open':
            return {class: 'time-open', text: 'Open Now'}
          case 'closed':
            return {class: 'time-close', text: 'Closed Now'}
          case 'opening':
            return {class: 'time-open', text: 'Opening Soon'}
          case 'closing':
            return {class: 'time-close', text: 'Closing Soon'}
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .Content {
    .Name {
      height: 26px;
      line-height: 26px;
      margin-top: 6px;
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

    .LocationDistanceTiming {
      margin-top: 8px;

      font-weight: 600;
      font-size: 13px;

      .BulletDivider {
        vertical-align: middle;
        font-size: 12px;
        margin: 0 3px;
      }
    }
  }

  .Small {
    .Name {
      font-size: 15px;
    }

    .Tags {
      min-height: 22px;
      max-height: 56px;
      margin-bottom: -6px;

      .Tag {
        font-size: 10px;
        line-height: 22px;
        padding: 0 7px;
        margin-right: 6px;
        margin-bottom: 6px;
      }
    }

    .LocationDistanceTiming {
      font-size: 11px;
    }
  }
</style>
