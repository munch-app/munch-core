<template>
  <nuxt-link :to="'/places/' + place.placeId">
    <div class="Card">
      <image-size class="Image border-3" :image="images" :alt="place.name"/>

      <div class="Content">
        <div class="Name Title large weight-600 black-a-80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
               :class="{'peach-100-bg weight-600 black-a-80': tag.type === 'price', 'whisper-100-bg weight-400': tag.type !== 'price'}">
            {{tag.name}}
          </div>
        </div>
        <div class="LocationDistanceTiming small">
          <span v-if="distance">{{distance}}, </span>
          <span class="weight-600 black-a-80">{{location}}</span>
          <span v-if="timing" class="black-a-75 BulletDivider">•</span>
          <span v-if="timing" :class="timing.class">{{timing.text}}</span>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import ImageSize from "../../core/ImageSize";
  import {Hour, HourGroup} from '../../places/hour-group'

  export default {
    name: "SearchCardPlace",
    components: {ImageSize},
    props: {
      place: {
        required: true,
        type: Object
      }
    },
    computed: {
      location() {
        return this.place.location.neighbourhood || this.place.location.street
      },
      images() {
        if (this.place.images && this.place.images[0]) {
          return this.place.images[0]
        }
      },
      tags() {
        const perPax = this.place.price && this.place.price.perPax
        const priceTax = perPax && [{type: 'price', name: `$${perPax.toFixed(0)}`}] || []

        return [
          ...priceTax,
          ...this.place.tags.slice(0, 3)
        ]
      },
      distance() {
        return null
      },
      timing() {
        if (this.place.hours.length === 0) return

        const group = new HourGroup(this.place.hours.map((h) => new Hour(h.day, h.open, h.close)))
        switch (group.isOpen()) {
          case 'open':
            return {class: 'timing-open', text: 'Open Now'}
          case 'closed':
            return {class: 'timing-close', text: 'Closed Now'}
          case 'opening':
            return {class: 'timing-open', text: 'Opening Soon'}
          case 'closing':
            return {class: 'timing-close', text: 'Closing Soon'}
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .Card {
    .Image {
      width: 100%;
      padding-top: 60%;
    }

    .Content {
      .Name {
        height: 26px;
        line-height: 26px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;

        margin-top: 6px;
      }

      .Tags {
        display: flex;
        flex-wrap: wrap;
        align-items: flex-start;
        overflow: hidden;

        height: 24px;
        margin-top: 4px;

        .Tag {
          font-size: 12px;
          line-height: 24px;
          padding: 0 8px;
          margin-right: 8px;
        }
      }

      .LocationDistanceTiming {
        margin-top: 8px;

        font-weight: 600;
        font-size: 13px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;

        .BulletDivider {
          vertical-align: middle;
          font-size: 12px;
          margin: 0 3px;
        }
      }
    }
  }
</style>
