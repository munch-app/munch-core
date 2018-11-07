<template>
  <div>
    <p>Own an Instagram account, want it published on munch? <a class="text-underline s700 weight-600"
                                                                href="https://partner.munch.app"
                                                                target="_blank">partner.munch.app</a></p>

    <div class="Existing" v-if="images.length > 0">
      <h2>Images</h2>

      <div class="List flex-wrap">
        <div class="Image hover-pointer" v-for="image in images" :key="image.imageId" @click="onDialog(image)">
          <div class="aspect r-1-1">
            <image-sizes class="overflow-hidden border-3" :sizes="image.sizes" width="1" height="1">
              <div class="OverlayA20 relative hover-bg-a60 wh-100 flex-center">
                <simple-svg class="wh-32px" fill="white" :filepath="require('~/assets/icon/place/suggest/flag.svg')"/>
              </div>
            </image-sizes>
          </div>
        </div>
      </div>
    </div>

    <no-ssr>
      <portal to="dialog-styled" v-if="dialog" class="Dialog">
        <h3>Flag Image</h3>
        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotRelated')">
          <div class="text">Not related image</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotPlace')">
          <div class="text">Does not belong to place</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'Explicit')">
          <div class="text">Explicit content</div>
          <div class="checkbox"/>
        </div>

        <div class="right">
          <button class="clear-elevated" @click="dialog = null">Cancel</button>
        </div>
      </portal>
    </no-ssr>
  </div>
</template>

<script>
  import ImageSizes from "../../core/ImageSizes";
  import Vue from 'vue'

  export default {
    name: "PlaceSuggestImage",
    components: {ImageSizes},
    props: {
      data: Object,
      payload: {
        type: Object,
        twoWay: true
      }
    },
    data() {
      return {
        dialog: null
      }
    },
    computed: {
      images() {
        // TODO join images
        // TODO Auto load everything
        return this.data.images
      }
    },
    methods: {
      onDialog(image) {
        this.dialog = image
        // Vue.set(this.changes, image.imageId, image)
      },
      onFlag(image, flag) {
        this.dialog = null

        Vue.set(this.payload.removes.images, image.imageId, {flag, image})
      }
    }
  }
</script>

<style scoped lang="less">
  .Existing {
    margin-top: 32px;

    .List {
      margin: 8px -8px;
    }
  }

  .Image {
    padding: 8px;

    flex: 0 0 20%;

    @media (max-width: 576px) {
      flex: 0 0 33.333%;
    }

    .OverlayA20 {
      background: rgba(0, 0, 0, 0.2);
    }
  }

  .Dialog {
    > div {
      padding-top: 8px;
      padding-bottom: 8px;
      margin-bottom: 0;
    }
  }
</style>
