<template>
  <nuxt-link :to="'/places/' + place.placeId">
    <div class="Elevation1 ElevationHover2 Card Border48">
      <image-size class="Image Border48Top" :image="images" :alt="place.name"/>

      <div class="Content">
        <div class="Name Title Large Weight600 BlackA80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag Border24" v-for="tag in tags" :key="tag.tagId"
               :class="{'Peach100Bg': tag.type === 'price', 'Whisper100Bg': tag.type !== 'price'}">
            {{tag.name}}
          </div>
        </div>
        <div class="LocationDistanceTiming Small">
          <span v-if="distance">{{distance}}, </span>
          <span class="Weight600 BlackA80">{{location}}</span>
          <span v-if="timing" class="BlackA75 BulletDivider">•</span>
          <span :class="timing.class">{{timing.text}}</span>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import ImageSize from "../../core/ImageSize";
  import {Day, days, Hour, HourGroup} from '../../places/hour-group'

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
        const group = new HourGroup(this.place.hours.map((h) => new Hour(h.day, h.open, h.close)))
        switch (group.isOpen()) {
          case 'open':
            return {class: 'Open', text: 'Open Now'}
          case 'closed':
            return {class: 'Close', text: 'Closed Now'}
          case 'opening':
            return {class: 'Open', text: 'Opening Soon'}
          case 'closing':
            return {class: 'Close', text: 'Closing Soon'}
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

        height: 22px;
        margin-top: 3px;

        .Tag {
          font-size: 11px;
          padding: 3px 7px;
          margin-right: 8px;
        }
      }

      .LocationDistanceTiming {
        font-size: 14px;
        margin-top: 8px;
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
