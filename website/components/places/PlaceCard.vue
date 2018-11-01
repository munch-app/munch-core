<template>
  <nuxt-link :to="'/places/' + place.placeId">
    <div class="Card" :class="{'Small': small}">
      <no-ssr>
        <div class="CollectionBtn index-content-overlay" v-if="!isProduction && isLoggedIn">
          <div @click.prevent.stop="showAddToCollection = true">
            <simple-svg class="AddIcon" fill="white" :filepath="require('~/assets/icon/place/add.svg')"/>
          </div>
        </div>
      </no-ssr>

      <image-size v-if="image" class="Image border-3 index-content" :image="image" :alt="place.name"/>
      <div v-else class="Image border-3 bg-whisper100"/>

      <div class="Content">
        <div class="Name Title large weight-600 b-a80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId" :class="{
               'bg-peach100 weight-600 black-a-80': tag.type === 'price',
               'bg-whisper100 weight-400': tag.type !== 'price'}"
          >
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

    <no-ssr>
      <profile-collection-add-place :place="place" v-if="showAddToCollection" @on-close="showAddToCollection = false"/>
    </no-ssr>
  </nuxt-link>
</template>

<script>
  import {mapGetters} from "vuex";
  import {Hour, HourGroup} from './hour-group'
  import ImageSize from "../core/ImageSize";
  import ProfileCollectionAddPlace from "../profile/ProfileCollectionAddPlace";

  export default {
    name: "PlaceCard",
    components: {ProfileCollectionAddPlace, ImageSize},
    props: {
      place: {
        type: Object,
        required: true,
      },
      small: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        showAddToCollection: false
      }
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
      ...mapGetters(['isProduction']),
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
    position: relative;
  }

  .CollectionBtn {
    position: absolute;
    width: 100%;
    display: flex;
    justify-content: flex-end;

    .AddIcon {
      width: 30px;
      height: 30px;
      padding: 6px;
    }
  }

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
