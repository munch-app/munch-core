<template>
  <nuxt-link :to="`/@${article.profile.username}/${article.slug}-${article.id}`" class="block text-decoration-none h-100">

    <div v-if="normal" class="border border-4 elevation-1 bg-white overflow-hidden h-100">
      <div v-if="article.image" class="Image">
        <div class="aspect r-10-3 overflow-hidden">
          <cdn-img :image="article.image"/>
        </div>
      </div>

      <div class="p-24">
        <h2 class="text-ellipsis-2l">{{article.title}}</h2>

        <div class="pt-8">
          <p class="large b-a50 text-ellipsis-3l">{{article.description}}</p>
        </div>

        <div class="flex mt-16">
          <div class="flex-no-shrink wh-40px border-circle overflow-hidden">
            <cdn-img v-if="article.profile.image" :image="article.profile.image" type="320x320"/>
            <div v-else class="wh-100 bg-blue"/>
          </div>

          <div class="ml-16 flex-shrink text-ellipsis-1l">
            <div class="small-bold b-a80">{{article.profile.name}}</div>
            <div class="tiny b-a75">{{formatMillis(article.updatedAt)}}</div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="p-16 border border-4 elevation-1 bg-white h-100">
      <div v-if="article.image">
        <div class="aspect r-5-3 overflow-hidden border-2">
          <cdn-img :image="article.image"/>
        </div>

        <h5 class="mt-8 text-ellipsis-2l">{{article.title}}</h5>
      </div>
      <div v-else class="mb-16">
        <h5 class="text-ellipsis-2l">{{article.title}}</h5>
        <p class="mt-8 regular b-a50 text-ellipsis-3l">{{article.description}}</p>
      </div>

      <div class="flex mt-8 flex-align-center">
        <div class="flex-no-shrink wh-32px border-circle overflow-hidden">
          <cdn-img v-if="article.profile.image" :image="article.profile.image" type="320x320"/>
          <div v-else class="wh-100 bg-blue"/>
        </div>

        <div class="ml-8 flex-shrink text-ellipsis-1l">
          <div class="tiny-bold b-a80">{{article.profile.name}}</div>
          <div class="tiny b-a75">{{formatMillis(article.updatedAt)}}</div>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import dateformat from 'dateformat'
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "ArticleCard",
    components: {CdnImg},
    props: {
      article: {
        type: Object,
        required: true
      },
      type: {
        type: String,
        default: 'normal'
      }
    },
    computed: {
      normal() {
        return this.type === 'normal'
      }
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">
  @media (min-width: 576px) {
    .Image {
      padding: 24px 24px 0 24px;

      > div {
        border-radius: 3px;
      }
    }
  }
</style>
