<template>
  <nuxt-link :to="`/@${media.profile.username}/${media.id}`" class="text-decoration-none">
    <div class="Media aspect r-1-1 border border-4 overflow-hidden hover-pointer">

      <cdn-img :image="media.images[0]">
        <div class="flex-column-justify-end">

          <nuxt-link v-if="mention" :to="`/${mention.place.slug}-${mention.place.id}`"
                     class="Mention cubic-bezier p-8 flex-between text-decoration-none">
            <div class="mlr-4">
              <h6 class="text-ellipsis-1l">{{mention.place.name}}</h6>
              <p class="small text-ellipsis-1l">
                {{mention.place.location.address}}
              </p>
            </div>

            <div class="Icon p-8 bg-translucent border-circle elevation-2">
              <simple-svg class="wh-20px" fill="#000" :filepath="require('~/assets/icon/icons8-map-pin.svg')"/>
            </div>
          </nuxt-link>
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
        return this.media.mentions[0]
      }
    }
  }
</script>

<style scoped lang="less">
  .Mention {
    h6, p {
      opacity: 0;
    }
  }

  .Media:hover .Mention {
    h6, p {
      opacity: 1;
    }

    .Icon {
      background: white;
    }

    background: rgba(240, 240, 240, 0.95);
  }

  .Mention:hover {
    h6, p {
      text-decoration: underline;
    }
  }
</style>
