<!-- @deprecated -->
<template>
  <div @click="onClick" class="hover-pointer">
    <div class="Card relative" :class="{'Small': small}">
      <div class="absolute w-100 index-content-overlay flex-justify-end">
      </div>

      <div class="aspect r-5-3 border-3 overflow-hidden">
        <image-sizes v-if="image" :sizes="image.sizes" :alt="place.name"/>
        <div v-else class="bg-whisper100 flex-end wh-100">
          <div class="small-bold mb-4 mr-4">No Image Available</div>
        </div>
      </div>

      <div class="mt-8">
        <!-- Although /places/_placeId is deprecated, it will still support URl routing. -->
        <nuxt-link class="Title text-ellipsis-1l large weight-600 b-a80 text-decoration-none" :to="`/places/${place.placeId}`" >
          {{place.name}}
        </nuxt-link>

        <nuxt-link :to="`/places/${place.placeId}`" class="text-decoration-none">
          <div class="Tags flex-wrap mt-8">
            <div class="Tag border-3 mr-8 mb-8" v-for="tag in tags" :key="tag.tagId" :class="{
                 'bg-peach100 weight-600 b-a80': tag.type === 'price',
                 'bg-whisper100 weight-400 black': tag.type !== 'price'}"
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
      },
      saved: Boolean,
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
          this.timing = {class: 'success', text: 'Open Now'}
          break
        case 'closed':
          this.timing = {class: 'error', text: 'Closed Now'}
          break
        case 'opening':
          this.timing = {class: 'success', text: 'Opening Soon'}
          break
        case 'closing':
          this.timing = {class: 'error', text: 'Closing Soon'}
          break
      }
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
        const priceTag = perPax && [{type: 'price', name: `$${perPax.toFixed(0)}`}] || []

        const tags = this.place.tags.length === 0
          ? [{name: "Restaurant", tagId: '216e7264-f4c9-40a4-86a2-d49793fb49c9', type: 'Establishment'}]
          : this.place.tags

        return [
          ...priceTag,
          ...tags
        ]
      },
      distance() {
        return null
      },
    },
    methods: {
      onClick() {
        this.$router.push({path: `/places/${this.place.placeId}`})
      },
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
    width: 40px;
    height: 40px;
    padding: 8px;
  }
</style>
