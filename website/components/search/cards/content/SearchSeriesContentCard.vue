<template>
  <nuxt-link :to="`/contents/${cid}/${slug}`">
    <div class="aspect border-3 overflow-hidden" :class="imageAspectRatio">
      <image-sizes v-if="imageSizes" :sizes="imageSizes" :alt="content.subtitle"/>
      <div v-else class="bg-s500 flex-end wh-100"/>
      <div class="absolute-0 flex-center p-24 ImageOverlay">
        <h2 class="white text-ellipsis-2l text-center">{{content.title}}</h2>
      </div>
    </div>

    <div class="Content">
      <h5 class="text-ellipsis-1l s700">{{content.subtitle}}</h5>
      <p class="mt-4 text-ellipsis-3l">{{content.body}}</p>
    </div>
  </nuxt-link>
</template>

<script>
  import ImageSizes from "../../../core/ImageSizes";
  import base64 from 'uuid-base64'

  export default {
    name: "SearchSeriesContentCard",
    components: {ImageSizes},
    props: {
      content: {
        type: Object,
        required: true
      },
      options: {
        type: Object,
      }
    },
    computed: {
      imageSizes() {
        return this.content && this.content.image && this.content.image.sizes
      },
      imageCredit() {
        const image = this.content && this.content.image
        return null
      },
      imageAspectRatio() {
        if (this.options && this.options.expand === 'height') {
          return 'r-10-12 ExpandHeight'
        }
        return 'r-5-3 ExpandWidth'
      },
      /**
       * cid is not using the default base64, https://www.npmjs.com/package/d64
       */
      cid() {
        return base64.encode(this.content.contentId)
      },
      slug() {
        let slug = this.content.title.toLowerCase()
        slug = slug.replace(/ /g, '-')
        slug = slug.replace(/[^0-9a-z-]/g, '')
        return slug
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageOverlay {
    background: rgba(0, 0, 0, 0.45);
  }

  .Content {
    margin-top: 12px;
  }

  p {
    font-size: 14px;
  }

  .ExpandHeight {
    .ImageOverlay {
      align-items: flex-end;
    }

    h2 {
      font-size: 18px;
      line-height: 1.3;
    }
  }
</style>
