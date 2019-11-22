<template>
  <div class="border border-3 overflow-hidden">
    <div v-if="mention.type === 'MEDIA'">
      <mention-media-card :mention="mention"/>
    </div>

    <div v-else-if="mention.type === 'ARTICLE'">
      <mention-article-card :mention="mention"/>
    </div>

    <div v-else-if="mention.type === 'POST'">
      <mention-post-card :mention="mention"/>
    </div>
  </div>
</template>

<script>
  import MentionMediaCard from "./MentionMediaCard";
  import MentionArticleCard from "./MentionArticleCard";
  import MentionPostCard from "./MentionPostCard";

  export default {
    name: "MentionCard",
    components: {MentionPostCard, MentionArticleCard, MentionMediaCard},
    props: {
      mention: {
        type: Object,
        required: true
      }
    },
  }
</script>
