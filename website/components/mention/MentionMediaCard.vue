<template>
  <div>
    <nuxt-link :to="`/@${media.profile.username}/${media.id}`" class="text-decoration-none">
      <div class="aspect" :style="{paddingTop}">
        <cdn-img v-if="image" :image="image">
        </cdn-img>
      </div>

      <div class="p-0-16 pt-16">
        <template v-for="(node, index) in media.content">
          <div class="small black-a70 lh-1-3 text-ellipsis-2l" v-if="node.type === 'text'" :key="index">
            {{node.text}}
          </div>
        </template>
      </div>
    </nuxt-link>

    <mention-card-footer :profile="mention.profile"
                         :icon="require('~/assets/icon/icons8-instagram.svg')"
                         :millis="media.createdAt"/>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";
  import MentionCardFooter from "./MentionCardFooter";

  export default {
    name: "MentionMediaCard",
    components: {MentionCardFooter, CdnImg},
    props: {
      mention: {
        type: Object,
        required: true
      }
    },
    computed: {
      media() {
        return this.mention.media
      },
      image() {
        const images = this.media?.images
        return images && images?.length > 0 && images[0]
      },
      count() {
        return this.media.images?.length || 0
      },
      paddingTop() {
        const image = this.image
        return `${image.height / image.width * 100}%`
      },
    }
  }
</script>

<style scoped lang="less">

</style>
