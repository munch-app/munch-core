<template>
  <div class="flex-align-center mt-24 mb-32">
    <nuxt-link :to="`/@${article.profile.username}`"
               class="text-decoration-none flex-no-shrink wh-48px border-circle overflow-hidden">
      <cdn-img v-if="article.profile.image" :image="article.profile.image" type="320x320"/>
      <div v-else class="wh-100 bg-blue"/>
    </nuxt-link>

    <div class="ml-16 flex-shrink text-ellipsis-1l">
      <nuxt-link :to="`/@${article.profile.username}`" class="text-decoration-none">
        <div class="mb-4 weight-500 b-a80">{{article.profile.name}}</div>
      </nuxt-link>
      <div class="tiny b-a75">{{formatMillis(article.updatedAt)}}</div>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import CdnImg from "../../utils/image/CdnImg";

  export default {
    name: "ProfileNode",
    components: {CdnImg},
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
