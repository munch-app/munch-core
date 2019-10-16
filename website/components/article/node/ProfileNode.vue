<template>
  <div class="flex-align-center mt-24 mb-32">
    <nuxt-link :to="`/@${article.profile.username}`" class="flex-no-shrink wh-48px">
      <profile-pic :profile="article.profile"/>
    </nuxt-link>

    <div class="ml-16 flex-shrink text-ellipsis-1l">
      <nuxt-link :to="`/@${article.profile.username}`" class="text-decoration-none">
        <div class="mb-4 weight-500 b-a80">{{article.profile.name}}</div>
      </nuxt-link>
      <div class="tiny b-a75">{{formatMillis(article.publishedAt || article.updatedAt)}}</div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import CdnImg from "../../utils/image/CdnImg";
  import ProfilePic from "../../profile/ProfilePic";

  export default {
    name: "ProfileNode",
    components: {ProfilePic, CdnImg},
    props: {
      article: {
        type: Object,
        required: true
      }
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>
