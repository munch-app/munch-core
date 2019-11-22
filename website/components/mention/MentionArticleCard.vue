<template>
  <div>
    <nuxt-link :to="`/@${article.profile.username}/${article.slug}-${article.id}`" class="text-decoration-none">
      <cdn-bg-img :image="article.image" height="auto">
        <div class="p-16 bg-black-a50">
          <h3 class="white text-ellipsis-2l">{{article.title}}</h3>
          <p class="mt-8 white text-ellipsis-3l">{{article.description}}</p>
        </div>
      </cdn-bg-img>
    </nuxt-link>

    <mention-card-footer :profile="mention.profile"
                         :icon="require('~/assets/icon/icons8-note.svg')"
                         :millis="article.publishedAt"/>
  </div>
</template>

<script>
  import CdnBgImg from "../utils/image/CdnBgImg";
  import MentionCardFooter from "./MentionCardFooter";

  export default {
    name: "MentionArticleCard",
    components: {MentionCardFooter, CdnBgImg},
    props: {
      mention: {
        type: Object,
        required: true
      }
    },
    computed: {
      article() {
        return this.mention.article
      },
    }
  }
</script>
