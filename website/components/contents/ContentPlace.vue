<template>
  <div class="ContentPlace mt-32 mb-24 relative">
    <nuxt-link :to="`/places/${placeId}`" v-if="selectedImage">
      <div class="aspect r-5-3 border-4 overflow-hidden">
        <image-sizes :sizes="selectedImage.sizes" :alt="place.name" :height="1000" :width="1000">

          <div class="wh-100 hover-opacity">
            <div class="ImageGallery flex-align-end">
              <div class="Item hover-pointer elevation-1" v-for="(gi, index) in galleryImages" :key="index"
                   @click.stop.prevent="selectedIndex = index">
                <div class="aspect r-1-1 border-4 overflow-hidden">
                  <image-sizes :sizes="gi.sizes" width="200" height="200"/>
                </div>
              </div>
            </div>
          </div>
        </image-sizes>
      </div>
    </nuxt-link>

    <div class="ContentCredit flex-end absolute w-100" v-if="selectedCreditName">
      <div class="small-bold lh-1">
        Image by: {{selectedCreditName}}
      </div>
    </div>

    <div v-if="place" class="ContentInfo mt-24 relative">
      <nuxt-link :to="`/places/${placeId}`">
        <h2 class="m-0">{{place.name}}</h2>
      </nuxt-link>
      <place-status class="mt-16" :place="place"/>
      <nuxt-link :to="`/places/${placeId}`">
        <div class="subtext mtb-4"><b>{{location.neighbourhood}}</b> <b>·</b> {{location.address}}</div>
      </nuxt-link>

      <div class="Tags flex-wrap mt-16 overflow-hidden">
        <div class="Tag text weight-600 border-3 mr-8 mb-8 flex-no-shrink flex-center" v-for="tag in tags"
             :key="tag.tagId" :class="{
                 'Price bg-peach100': tag.type === 'price',
                 'bg-whisper100 b-a75': tag.type !== 'price'}"
        >
          <span>{{tag.name}}</span>
        </div>
      </div>
    </div>

    <div v-else class="p-24 bg-peach100 border-5">
      <h2>{{placeName}}</h2>
      <div class="large m-0">This place has permanently closed or removed from Munch.</div>
      <h6 class="m-0">Know this place? <a :href="`/places/suggest?placeId=${placeId}`" target="_blank">Suggest an edit.</a></h6>
    </div>
  </div>
</template>

<script>
  import _ from 'lodash'
  import ImageSizes from "../core/ImageSizes";
  import PlaceTagList from "../places/PlaceTagList";
  import PlaceHourList from "../places/PlaceHourList";
  import PlaceStatus from "../places/PlaceStatus";

  export default {
    name: "ContentPlace",
    components: {PlaceStatus, PlaceHourList, PlaceTagList, ImageSizes},
    props: {
      placeName: {
        type: String,
        required: true,
      },
      placeId: {
        type: String,
        required: true,
      },
      place: {
        required: true,
      }
    },
    computed: {
      location() {
        return this.place.location
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
        ].splice(0, 8)
      },
      galleryImages() {
        return _.uniqBy(this.images, 'imageId').splice(0, 4)
      },
      selectedImage() {
        if (this.galleryImages.length === 0) return null
        return this.galleryImages[this.selectedIndex]
      },
      selectedCreditName() {
        if (this.galleryImages.length === 0) return null
        const image = this.galleryImages[this.selectedIndex]
        if (image.profile && image.profile.name) return image.profile.name

        if (image.instagram && image.instagram.username) return image.instagram.username
        if (image.domain && image.domain.name) return image.domain.name
      },
    },
    data() {
      if (this.place) {
        const images = this.place.images
        if (images.length > 0) {
          return {images: [images[0]], selectedIndex: 0}
        }
      }

      return {
        images: [], selectedIndex: 0
      }
    },
    mounted() {
      if (this.place) {
        this.$api.get(`/places/${this.place.placeId}/images`, {params: {size: 4}}).then(({data, next}) => {
          this.images.push(...data)
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentPlace {
    max-width: 640px;
  }

  .ContentCredit {
    margin-top: 6px;
  }

  .ContentInfo {
    padding-bottom: 24px;
  }

  .ImageGallery {
    width: 75%;
    height: 100%;
    padding-left: 12px;
    padding-bottom: 12px;

    margin-left: -6px;
    margin-right: -6px;

    .Item {
      flex: 0 0 12.5%;
      max-width: 12.5%;

      border-radius: 3px;
      border: 1px solid white;

      margin: 0 6px;
    }

    @media (max-width: 991.98px) {
      padding-left: 8px;
      padding-bottom: 8px;

      margin-left: -4px;
      margin-right: -4px;

      .Item {
        flex: 0 0 20%;
        max-width: 20%;
        margin: 0 4px;
      }
    }
  }

  .Tags {
    margin-bottom: -10px;
    max-height: 68px;
  }

  .Tag {
    font-size: 12px;

    padding: 0 10px;
    height: 28px;

    margin-right: 10px;
    margin-bottom: 10px;

    &.Price {
      font-size: 13px;
    }
  }
</style>
