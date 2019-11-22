<template>
  <div>
    <div>
      <cdn-img :image="post.images[0]">
        <div class="flex-end p-12">
          <div class="Count">
            <div class="tiny-bold white">1/{{count}}</div>
          </div>
        </div>
      </cdn-img>

      <div class="p-0-16 pt-16" v-if="text">
        <div class="small black-a70 lh-1-3 text-ellipsis-2l">
          {{text}}
        </div>
      </div>
    </div>

    <mention-card-footer :profile="mention.profile"
                         :icon="require('~/assets/icon/icons8-align-text-left.svg')"
                         :millis="post.createdAt"/>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";
  import MentionCardFooter from "./MentionCardFooter";

  export default {
    name: "MentionPostCard",
    components: {MentionCardFooter, CdnImg},
    props: {
      mention: {
        type: Object,
        required: true
      }
    },
    computed: {
      post() {
        return this.mention.post
      },
      text() {
        return this.post?.content
      },
      count() {
        return this.post.images?.length || 0
      }
    }
  }
</script>

<style scoped lang="less">
  .Count {
    background: rgba(0, 0, 0, 0.7);
    border-radius: 6px;

    padding: 2px 8px;
  }
</style>
