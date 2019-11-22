<template>
  <div class="mt-24" v-if="items.length > 0">
    <div class="ImageList m--8 flex overflow-hidden">
      <div class="p-8" v-for="item in items" :key="item.id">
        <div class="aspect r-5-3 border-3 overflow-hidden flex-no-shrink">
          <cdn-img :image="item.image" type="640x640">
            <div class="flex-end p-8">
              <div class="Credit">
                <nuxt-link :to="`/@${item.profile.username}`" class="lh-0 text-decoration-none">
                  <small class="white lh-1 tiny">@{{item.profile.username}}</small>
                </nuxt-link>
              </div>
            </div>
          </cdn-img>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "PlaceBanner",
    components: {CdnImg},
    props: {
      place: {
        type: Object,
        required: true
      },
      mentions: {
        type: Array,
        required: true
      }
    },
    computed: {
      items() {
        const images = []
        this.mentions.forEach(mention => {
          if (mention.media && mention.media.images.length > 0) {
            images.push({
              id: mention.id,
              image: mention.media.images[0],
              profile: mention.profile
            })
          }

          if (mention.post && mention.post.images.length > 0) {
            images.push({
              id: mention.id,
              image: mention.post.images[0],
              profile: mention.profile
            })
          }
        })
        return images
      }
    }
  }
</script>

<style scoped lang="less">
  .ImageList {
    > div {
      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 576px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 33.33333%;
        max-width: 33.33333%;
      }

      @media (min-width: 992px) {
        flex: 0 0 25%;
        max-width: 25%;
      }
    }
  }

  .Credit {
    background: rgba(0, 0, 0, 0.66);
    border-radius: 5px;

    padding: 5px 5px;
  }
</style>
