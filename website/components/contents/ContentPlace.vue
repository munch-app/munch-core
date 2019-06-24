<template>
  <div class="ContentPlace mtb-24 relative">
    <div class="Option1 bg-whisper050 border-3 overflow-hidden" v-if="!options.style || options.style === 1">
      <nuxt-link :to="`/places/${placeId}`">
        <div class="Flex flex-align-stretch">
          <div class="Image" v-if="selectedImage">
            <div class="aspect r-1-1">
              <image-sizes :sizes="selectedImage.sizes" :alt="place.name" :height="500" :width="500"/>
            </div>
          </div>

          <div class="Detail p-16-24">
            <div>
              <h4 class="m-0">{{place.name}}</h4>
              <div class="subtext mtb-4"><b>{{location.neighbourhood}}</b> <b>·</b> {{location.address}}</div>
              <div class="TagList flex-wrap mt-8 overflow-hidden">
                <div class="Tag text weight-600 border-3 mr-8 mb-8 flex-no-shrink flex-center bg-white b-a75"
                     v-for="tag in tags"
                     :key="tag.tagId">
                  <span>{{tag.name}}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </nuxt-link>
    </div>

    <div class="Option2" v-else-if="options.style === 2">
      <nuxt-link :to="`/places/${placeId}`" v-if="selectedImage">
        <div class="aspect r-5-3 border-4 overflow-hidden">
          <image-sizes :sizes="selectedImage.sizes" :alt="place.name" :height="1000" :width="1000"/>
        </div>
      </nuxt-link>

      <div v-if="place" class="Detail mt-24 relative">
        <nuxt-link :to="`/places/${placeId}`">
          <h3 class="m-0">{{place.name}}</h3>
        </nuxt-link>
        <place-status class="mt-16" :place="place"/>
        <nuxt-link :to="`/places/${placeId}`">
          <div class="subtext mtb-4"><b>{{location.neighbourhood}}</b> <b>·</b> {{location.address}}</div>
        </nuxt-link>

        <div class="TagList flex-wrap mt-16 overflow-hidden">
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
        <h6 class="m-0">Know this place? <a :href="`/places/suggest?placeId=${placeId}`" target="_blank">Suggest an
          edit.</a></h6>
      </div>
    </div>
  </div>
</template>

<script>
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
      },
      options: {
        type: Object,
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
      selectedImage() {
        if (this.options && this.options.image) {
          return this.options.image
        }

        if (this.place.images.length > 0) {
          return this.place.images[0]
        }
      },
    },
  }
</script>

<style scoped lang="less">
  a {
    text-decoration: none !important;
  }

  .Option1 {
    @media (max-width: 575.98px) {
      .Flex {
        flex-direction: column;
      }

      .aspect {
        padding-top: 50%;
      }
    }

    @media (min-width: 576px) {
      .Image {
        flex: 0 0 30%;
        max-width: 30%;
      }
    }
  }

  .TagList {
    max-height: 68px;
  }

  .Tag {
    font-size: 12px;

    padding: 0 10px;
    height: 28px;

    margin-right: 10px;
    margin-bottom: 10px;
  }
</style>
