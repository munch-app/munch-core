<template>
  <div class="Place bg-steam border-2 overflow-hidden">
    <nuxt-link :to="slugId" class="flex-row flex-align-stretch text-decoration-none">
      <div class="Image" v-if="place.image">
        <cdn-img class="wh-100" :image="place.image"/>
      </div>

      <div class="flex-grow p-16-24 overflow-hidden">
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
        <div class="Tags m--6 flex-wrap overflow-hidden">
          <div v-for="tag in place.tags" :key="tag.id" class="p-6">
            <div class="block small border-3 flex-no-shrink p-6-12 lh-1 bg-white">{{tag.name}}</div>
          </div>
        </div>

        <div class="Affiliates mt-16">
          <div class="m--4 flex-wrap overflow-hidden">
            <div class="p-4" v-for="affiliate in affiliates" :key="affiliate.uid">
              <div class="border border-3 overflow-hidden" @click="onAffiliate(affiliate)">
                <cdn-img :image="affiliate.brand.image" type="320x320" object-fit="contain" height="48px" width="initial">
                  <div class="hover-bg-a10 hover-pointer"/>
                </cdn-img>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nuxt-link>
  </div>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";

  export default {
    name: "ArticlePlace",
    components: {CdnImg},
    props: {
      node: {
        type: Object,
        required: true
      },
      affiliates: {
        type: Array,
      }
    },
    computed: {
      place() {
        return this.node.attrs.place || {}
      },
      slugId() {
        const {slug, id} = this.place
        if (slug) {
          return `/${slug}-${id}`
        }
        return `/${id}`
      },
    },
    methods: {
      onAffiliate(affiliate) {
        const url = affiliate.url
        const win = window.open(url, '_blank')
        win.focus()
      }
    }
  }
</script>
