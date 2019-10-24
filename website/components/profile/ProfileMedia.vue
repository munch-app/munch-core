<template>
  <nuxt-link :to="`/@${media.profile.username}/${media.id}`" class="text-decoration-none">
    <div class="Media aspect r-1-1 bg-steam border-4 overflow-hidden hover-pointer">

      <cdn-img :image="media.images[0]">
        <div class="flex-column-justify-end">
          <nuxt-link v-if="mention" :to="`/${mention.place.slug}-${mention.place.id}`"
                     class="Bottom cubic-bezier p-8 flex-between text-decoration-none">
            <div class="mlr-4">
              <h6 class="text-ellipsis-1l">{{mention.place.name}}</h6>
              <p class="small text-ellipsis-1l">
                {{mention.place.location.address}}
              </p>
            </div>

            <div class="Icon p-8 bg-pink border-circle elevation-2">
              <simple-svg class="wh-20px" fill="#FFF" :filepath="require('~/assets/icon/icons8-map-pin.svg')"/>
            </div>
          </nuxt-link>

          <div v-else class="Bottom cubic-bezier p-8 flex-between text-decoration-none">
            <div class="mlr-4">
              <p class="small text-ellipsis-2l">
                {{text}}
              </p>
            </div>
          </div>
        </div>
      </cdn-img>
    </div>
  </nuxt-link>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "ProfileMedia",
    components: {CdnImg},
    props: {
      media: {
        type: Object,
        required: true
      }
    },
    computed: {
      mention() {
        if (this.media?.mentions) {
          return this.media.mentions[0]
        }
      },
      text() {
        const textNodes = this.media.content
          .filter(n => n.type === 'text')

        if (textNodes.length) {
          return textNodes[0].text.substring(0, 80).trim()
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Bottom {
    h6, p {
      opacity: 0;
    }
  }

  .Media:hover .Bottom {
    h6, p {
      opacity: 1;
    }

    background: rgba(240, 240, 240, 0.95);
  }

  .Bottom:hover {
    h6, p {
      text-decoration: underline;
    }
  }
</style>
