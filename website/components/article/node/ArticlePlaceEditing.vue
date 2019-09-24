<template>
  <div class="Place bg-steam border-2 overflow-hidden">
    <div class="relative flex-row flex-align-stretch">
      <div class="Image hover-pointer" @click="onImage">
        <cdn-img v-if="place.image" class="wh-100" :image="place.image">
          <div class="flex-center hover-bg-a40">
            <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          </div>
        </cdn-img>

        <div v-else class="border wh-100 bg-white">
          <div class="wh-100 flex-center hover-bg-a40">
            <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          </div>
        </div>
      </div>

      <div class="flex-grow p-16-24 overflow-hidden">
        <input class="Name h3 clear" v-model="place.name" placeholder="Name">
        <div class="flex-align-center mt-8 mb-16">
          <div>
            <input class="Price text-center border-3 h6 pink clear" placeholder="$$$"
                   :value="pricePerPax" @input="onPriceInput">
          </div>
          <div class="ml-16 flex-grow">
            <input class="Address h6 clear" v-model="place.location.address" placeholder="Restaurant Address">
          </div>
        </div>

        <div>
          <article-place-editor-tags v-model="place.tags"/>
        </div>
      </div>

      <div class="absolute position-r-0 mt-16 mr-16">
        <button class="blue-outline tiny flex-align-center" @click="onEnlarge">
          <simple-svg class="wh-16px" fill="#07F" :filepath="require('~/assets/icon/icons8-enlarge.svg')"/>
          <span>EDIT</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";
  import ArticlePlaceEditorTags from "./ArticlePlaceEditorTags";

  export default {
    name: "ArticlePlaceEditing",
    components: {ArticlePlaceEditorTags, CdnImg},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      place: {
        get() {
          return this.node.attrs.place || {}
        },
        set(place) {
          this.updateAttrs({place})
        },
      },
    },
    data() {
      return {pricePerPax: null}
    },
    mounted() {
      if (this.place.price?.perPax) {
        this.pricePerPax = `$${this.place.price.perPax.toFixed(1)}`
      }
    },
    methods: {
      onImage() {
        this.$store.commit('global/setDialog', {
          name: 'ImageUploadDialog', props: {
            onImage: (image) => {
              this.$store.commit('global/clearDialog');
              this.place = {...this.place, image}
            }
          }
        })
      },
      onEnlarge() {
        this.$store.commit('global/setDialog', {
          name: 'PlaceEditorDialog', props: {
            place: this.place,
            onSubmit: (place) => {
              this.place = place
              this.$store.commit('global/clearDialog')
            }
          }
        })
      },
      onPriceInput({target: {value}}) {
        if (value.substring(0, 1) === '$') {
          value = value.substring(1)
        }

        const perPax = parseInt(value)
        if (!isNaN(perPax)) {
          this.place.price = {perPax}
          this.pricePerPax = `$${perPax.toFixed(1)}`
        } else {
          delete this.place['price']
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .Name, .Address {
    width: 100%;
    padding: 4px 8px;
    margin: -4px -8px;
    border-radius: 3px;

    &:hover, &:focus {
      background: #FFF;
    }
  }

  .Price {
    background: #FFF;
    height: 30px;
    width: 60px;
  }

  .Place {
    &:hover {
      outline: 3px solid #07F;
    }
  }
</style>

