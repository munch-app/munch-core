<template>
  <div @click="onClick" class="hover-pointer">
    <div class="Card relative" :class="{'Small': small}">
      <div class="absolute w-100 index-content-overlay flex-justify-end">
        <div @click.capture.stop.prevent="onClickHeart">
          <simple-svg v-if="isSaved" class="HeartIcon" fill="white"
                      :filepath="require('~/assets/icon/place/heart-filled.svg')"/>
          <simple-svg v-else class="HeartIcon" fill="white" :filepath="require('~/assets/icon/place/heart.svg')"/>
        </div>
      </div>

      <div class="aspect r-5-3 border-3 overflow-hidden">
        <image-sizes v-if="image" :sizes="image.sizes" :alt="place.name"/>
        <div v-else class="bg-whisper100"/>
      </div>

      <div class="mt-8">
        <nuxt-link class="Title text-ellipsis-1l large weight-600 b-a80" :to="`/places/${place.placeId}`">
          {{place.name}}
        </nuxt-link>

        <nuxt-link :to="`/places/${place.placeId}`">
          <div class="Tags flex-wrap mt-8">
            <div class="Tag border-3 mr-8 mb-8" v-for="tag in tags" :key="tag.tagId" :class="{
                 'bg-peach100 weight-600 b-a80': tag.type === 'price',
                 'bg-whisper100 weight-400': tag.type !== 'price'}"
            >
              {{tag.name}}
            </div>
          </div>

          <div class="LocationDistanceTiming mt-8 small text-ellipsis-1l">
            <span v-if="distance">{{distance}}, </span>
            <span class="weight-600 b-a80">{{location}}</span>
            <span v-if="timing" class="b-a75 BulletDivider">•</span>
            <span v-if="timing" :class="timing.class">{{timing.text}}</span>
          </div>
        </nuxt-link>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  import {Hour, HourGroup} from './hour-group'
  import ImageSizes from "../core/ImageSizes";

  export default {
    name: "PlaceCard",
    components: {ImageSizes},
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
        timing: null
      }
    },
    mounted() {
      if (this.place.hours.length === 0) return

      const group = new HourGroup(this.place.hours.map((h) => new Hour(h.day, h.open, h.close)))
      switch (group.isOpen()) {
        case 'open':
          this.timing = {class: 'time-open', text: 'Open Now'}
          break
        case 'closed':
          this.timing = {class: 'time-close', text: 'Closed Now'}
          break
        case 'opening':
          this.timing = {class: 'time-open', text: 'Opening Soon'}
          break
        case 'closing':
          this.timing = {class: 'time-close', text: 'Closing Soon'}
          break
      }
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
      isSaved() {
        return this.$store.getters['user/places/isSaved'](this.place.placeId)
      },
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
    },
    methods: {
      onClick() {
        this.$track.view(`RIP`, 'PlaceCard')
        this.$router.push({path: `/places/${this.place.placeId}`})
      },
      onClickHeart() {
        if (this.isSaved) {
          this.$store.dispatch('user/places/deletePlace', {place: this.place})
        } else {
          if (this.isLoggedIn) {
            this.$store.dispatch('user/places/putPlace', {place: this.place})
          } else {
            this.$store.commit('focus', 'Login')
          }
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Name {
    height: 26px;
    line-height: 26px;
  }

  .Tags {
    overflow: hidden;

    min-height: 24px;
    max-height: 64px;
    margin-bottom: -8px;
  }

  .Tag {
    font-size: 12px;
    line-height: 24px;
    padding: 0 8px;
  }

  .LocationDistanceTiming {
    font-weight: 600;
    font-size: 13px;
  }

  .BulletDivider {
    vertical-align: middle;
    font-size: 12px;
    margin: 0 3px;
  }

  .Small {
    .Name {
      font-size: 15px;
    }

    .Tags {
      min-height: 22px;
      max-height: 56px;
      margin-bottom: -6px;
    }

    .Tag {
      font-size: 10px;
      line-height: 22px;
      padding: 0 7px;
      margin-right: 6px;
      margin-bottom: 6px;
    }

    .LocationDistanceTiming {
      font-size: 11px;
    }
  }

  .HeartIcon {
    width: 32px;
    height: 32px;
    padding: 6px;
  }
</style>
