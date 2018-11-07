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

      <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
        <beat-loader color="#084E69" v-if="next.sort" size="14px"
                     v-observe-visibility="{callback: (v) => v && onMore(),throttle:300}"
        />
      </no-ssr>
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

  import _ from 'lodash'

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
      const {images} = this.data
      const last = images[images.length - 1]
      const next = {sort: last.sort || null}

      return {
        dialog: null,
        loading: false,
        images, next
      }
    },
    mounted() {
      // Push Place Images
      this.data.place.images.forEach(image => {
        if (!_.filter(this.images, (i) => i.imageId === image.imageId)) {
          this.images.push(image)
        }
      })
    },
    methods: {
      onMore() {
        if (this.loading) return
        if (!this.next.sort) return

        this.loading = true
        const params = {
          size: 20,
          'next.sort': this.next.sort
        }

        return this.$axios.$get(`/api/places/${this.data.place.placeId}/images`, {params})
          .then(({data, next}) => {
            this.images.push(...data)
            this.loading = false
            this.next.sort = next && next.sort || null
          })
      },
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
