<template>
  <div>
    <div v-if="editing" class="Editing">
      <div @click="deleting = true" class="hover-pointer">
        <simple-svg class="Icon button-action elevation-1 bg-s300 border-3" fill="white" :filepath="require('~/assets/icon/close.svg')"/>
      </div>
    </div>

    <nuxt-link :to="'/places/' + place.placeId" class="no-select ItemCard" @click.native.capture="$track.view(`RIP`, 'Collection - Card')">
      <image-size v-if="images" class="Image border-3" :image="images" :alt="place.name"/>
      <div v-else class="Image border-3 bg-whisper100"></div>

      <div class="Content">
        <div class="Name Title large weight-600 b-a80">{{place.name}}</div>
        <div class="Tags">
          <div class="Tag border-3" v-for="tag in tags" :key="tag.tagId"
               :class="{'bg-peach100 weight-600 b-a80': tag.type === 'price', 'bg-whisper100 weight-400': tag.type !== 'price'}">
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

      <portal to="dialog-styled" v-if="deleting">
        <h3>Delete '{{place.name}}' from '{{collection.name}}'?</h3>
        <div class="right">
          <button class="elevated" @click="deleting = false">Cancel</button>
          <button class="secondary" @click="onDeleteItem">Delete</button>
        </div>
      </portal>
    </nuxt-link>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  import {Hour, HourGroup} from '../places/hour-group'

  export default {
    name: "UserPlaceCollectionItemCard",
    components: {ImageSize},
    props: {
      place: {type: Object, required: true},
      collection: {type: Object, required: true},
      editing: {type: Boolean},
    },
    data() {
      return {deleting: false}
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
            return {class: 'time-open', text: 'Open Now'}
          case 'closed':
            return {class: 'time-close', text: 'Closed Now'}
          case 'opening':
            return {class: 'time-open', text: 'Opening Soon'}
          case 'closing':
            return {class: 'time-close', text: 'Closing Soon'}
        }
      },
    },
    methods: {
      onDeleteItem() {
        const collectionId = this.collection.collectionId
        const placeId = this.place.placeId
        return this.$store.dispatch('user/collections/deleteItem', {collectionId, placeId})
          .then(() => {
            this.deleting = false
            this.$emit('on-delete')
            const message = `Removed '${this.place.name}' to '${this.collection.name}' collection.`
            this.$store.dispatch('addMessage', {message})
          }).catch(error => {
            this.$store.dispatch('addError', error)
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .Editing {
    margin-bottom: 8px;
    display: flex;
    justify-content: flex-end;

    .Icon {
      width: 32px;
      height: 32px;
      padding: 5px;
    }
  }

  .ItemCard {
    color: initial;
    text-decoration: initial;

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
