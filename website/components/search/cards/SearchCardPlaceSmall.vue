<template>
  <!--// Deprecated?-->
  <nuxt-link :to="'/places/' + place.placeId" @click.native="$track.view(`RIP`, 'PlaceCard')">
    <div class="elevation-1 Card border-3">
      <div class="Content">
        <div class="Name Title large weight-600 b-a80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
               :class="{'bg-peach100': tag.type === 'price', 'bg-whisper100': tag.type !== 'price'}">
            {{tag.name}}
          </div>
        </div>
        <div class="LocationDistanceTiming small">
          <span v-if="distance">{{distance}}, </span>
          <span class="weight-600 b-a80">{{location}}</span>
          <span v-if="timing" class="b-a75 BulletDivider">•</span>
          <span v-if="timing" :class="timing.class">{{timing.text}}</span>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import {Hour, HourGroup} from '../../places/hour-group'

  export default {
    name: "SearchCardPlaceSmall",
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
      tags() {
        const perPax = this.place.price && this.place.price.perPax
        const priceTax = perPax && [{type: 'price', name: `$${perPax.toFixed(1)}`}] || []

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
  .Card {
    .Content {
      padding: 8px 16px 16px 16px;

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

        margin-top: 4px;
        height: 22px;

        .Tag {
          font-size: 12px;
          line-height: 22px;
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
