<template>
  <div>
    <nuxt-link  :to="`/@${media.profile.username}/${media.id}`" class="text-decoration-none">
      <cdn-img :image="media.images[0]">
      </cdn-img>

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
      count() {
        return this.media.images?.length || 0
      }
    }
  }
</script>

<style scoped lang="less">

</style>
