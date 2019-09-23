<template>
  <div class="Place bg-steam border-2 overflow-hidden">
    <div class="relative flex-row flex-align-stretch">
      <div class="Image hover-pointer" @click="onImage">
        <cdn-img v-if="place.image" class="wh-100" :image="place.image">
          <div class="flex-center hover-bg-a40">
            <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          </div>
        </cdn-img>

        <div v-else class="border wh-100 bg-white flex-center">
          <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
        </div>
      </div>

      <div class="flex-grow p-16-24 overflow-hidden">
        <input class="h3 clear" v-model="place.name" placeholder="Name">
        <div class="flex-align-center mt-8 mb-16">
          <div class="mr-16" v-if="place.price && place.price.perPax">
            <div class="border-3 bg-white p-8 flex-column-align-center">
              <h6 class="pink lh-1">${{place.price.perPax.toFixed(1)}}</h6>
            </div>
          </div>
          <div class="flex-grow">
            <input class="h6 clear" v-model="place.location.address" placeholder="Address">
          </div>
        </div>

        <div class="Tags flex-wrap overflow-hidden">
          <div v-for="tag in place.tags" :key="tag.id" class="p-6">
            <div class="block small border-3 flex-no-shrink p-6-12 lh-1 bg-white">{{tag.name}}</div>
          </div>
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

  export default {
    name: "ArticlePlaceEditing",
    components: {CdnImg},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      editing() {
        return !!this.updateAttrs
      },
      place: {
        get() {
          return this.node.attrs.place || {}
        },
        set(place) {
          this.updateAttrs({place})
        },
      },
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
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    width: 100%;
    padding: 4px 8px;
    margin: -4px -8px;

    &:focus {
      border-radius: 3px;
      background: #FFF;
    }
  }

  .Place {
    &:hover {
      outline: 3px solid #07F;
    }
  }
</style>

