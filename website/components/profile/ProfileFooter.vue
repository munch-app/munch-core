<template>
  <div class="bg-steam" v-if="hasAny">
    <div class="container pt-24 pb-64">
      <h4>More from {{profile.name}}</h4>

      <div class="mt-16" v-if="articles.length">
        <horizontal-list :items="articles" :options="{size: 4}">
          <template v-slot:default="{item}">
            <article-card :article="item"/>
          </template>
        </horizontal-list>
      </div>

      <div class="mt-16" v-if="medias.length">
        <horizontal-list :items="medias" :options="{size: 5}">
          <template v-slot:default="{item: media}">
            <profile-media :media="media"/>
          </template>
        </horizontal-list>
      </div>
    </div>
  </div>
</template>

<script>
  import HorizontalList from "../utils/HorizontalList";
  import ArticleCard from "../article/ArticleCard";
  import ProfileMedia from "./ProfileMedia";

  export default {
    name: "ProfileFooter",
    components: {ProfileMedia, ArticleCard, HorizontalList},
    props: {
      extra: {
        type: Object,
        required: true
      },
      profile: {
        type: Object,
        required: true
      }
    },
    computed: {
      hasAny() {
        if (this.extra) {
          return this.extra['profile.articles'] || this.extra['profile.medias']
        }
      },
      articles() {
        return this.extra && this.extra['profile.articles']
      },
      medias() {
        return this.extra && this.extra['profile.medias']
      }
    }
  }
</script>
