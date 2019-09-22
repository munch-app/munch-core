<template>
  <div class="Place bg-steam border-2 overflow-hidden">
    <div v-if="editing" class="flex-row flex-align-stretch">
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

      <div class="Right flex-grow p-16-24 overflow-hidden">
        <h3 class="text-ellipsis-2l m-0 hover-underline">{{place.name}}</h3>
        <div class="flex-align-center mt-8 mb-16">
          <div class="mr-16" v-if="place.price && place.price.perPax">
            <div class="border-3 bg-white p-8 flex-column-align-center">
              <h6 class="pink lh-1">${{place.price.perPax.toFixed(1)}}</h6>
            </div>
          </div>
          <div class="flex-grow">
            <h6 class="text-ellipsis-2l">{{place.location.address}}</h6>
          </div>
        </div>
        <div class="Tags flex-wrap overflow-hidden">
          <div v-for="tag in place.tags" :key="tag.id" class="p-6">
            <div class="block small border-3 flex-no-shrink p-6-12 lh-1 bg-white">{{tag.name}}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- enable slug url redirection? -->
    <nuxt-link v-else :to="`/${place.id}`" class="flex-row flex-align-stretch text-decoration-none">
      <div class="Image" v-if="place.image">
        <cdn-img class="wh-100" :image="place.image"/>
      </div>

      <div class="Right flex-grow p-16-24 overflow-hidden">
        <h3 class="text-ellipsis-2l m-0 hover-underline">{{place.name}}</h3>
        <div class="flex-align-center mt-8 mb-16">
          <div class="mr-16" v-if="place.price && place.price.perPax">
            <div class="border-3 bg-white p-8 flex-column-align-center">
              <h6 class="pink lh-1">${{place.price.perPax.toFixed(1)}}</h6>
            </div>
          </div>
          <div class="flex-grow">
            <h6 class="text-ellipsis-2l">{{place.location.address}}</h6>
          </div>
        </div>
        <div class="Tags flex-wrap overflow-hidden">
          <div v-for="tag in place.tags" :key="tag.id" class="p-6">
            <div class="block small border-3 flex-no-shrink p-6-12 lh-1 bg-white">{{tag.name}}</div>
          </div>
        </div>
      </div>
    </nuxt-link>
  </div>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";
  import ImageUploadDialog from "../../dialog/ImageUploadDialog";

  export default {
    name: "ArticlePlace",
    components: {ImageUploadDialog, CdnImg},
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
      id() {
        return this.place?.id
      }
    },
    data() {
      return {
        state: null
      }
    },
    methods: {
      onImage() {
        this.$store.commit('global/setDialog', {
          name: 'ImageUploadDialog', props: {
            onImage: (image) => {
              this.$store.commit('global/clearDialog');
              this.place = {...this.place,image}
            }
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    outline: none;
    border: none;
    background: none;

    padding: 0;
  }

  .Image {
    flex: 0 0 30%;
    max-width: 30%;

    /*TODO Max width scaling of min-height */
    min-height: 80px;

    @media (min-width: 576px) {
      min-height: 160px;
    }

    @media (min-width: 768px) {
      min-height: 160px;
    }
  }

  .Tags {
    margin: -6px;

    max-height: 38px;

    @media (min-width: 576px) {
      max-height: 76px;
    }
  }
</style>
