<!--
ArticleContent.vue must be structured as such to follow exactly ProseMirror structure
.article-content > div
-->
<template>
  <div class="article-content">
    <div>
      <template v-for="(node, index) in content">
        <profile-node v-if="node.type === 'profile'" :article="article" :key="index"/>
        <h1 v-else-if="node.type === 'heading' && node.attrs['level'] === 1" :key="index">{{toString(node)}}</h1>
        <h2 v-else-if="node.type === 'heading' && node.attrs['level'] === 2" :key="index">{{toString(node)}}</h2>
        <p v-else-if="node.type === 'paragraph'" :key="index">
          <text-content v-for="(content, index) in node.content" v-bind="content" :key="index"/>
        </p>
        <hr v-else-if="node.type === 'line'" :key="index">
        <article-image v-else-if="node.type ==='image'" :key="index" :node="node"/>
        <article-avatar v-else-if="node.type ==='avatar'" :key="index" :node="node"/>
        <article-place ref="context" v-else-if="node.type ==='place'" :key="index" :node="node"
                       :affiliates="placeAffiliates[node.attrs.place.id]"/>

        <div v-else-if="node.type === 'advert'" :key="index" class="w-100">
          <advert class="Article_InContent"
                  :google="{slot: 8163543825, layout: 'in-article', format: 'fluid'}"
          />
        </div>

        <div v-else :key="index"/>
      </template>
    </div>
  </div>
</template>

<script>
  import TextContent from "./node/TextContent";
  import ProfileNode from "./node/ProfileNode";
  import ArticleImage from "./node/ArticleImage.vue";
  import ArticleAvatar from "./node/ArticleAvatar.vue";
  import ArticlePlace from "./node/ArticlePlace.vue";
  import Advert from "../utils/ads/Advert";

  export default {
    name: "ArticleContent",
    components: {Advert, ArticlePlace, ArticleAvatar, ArticleImage, ProfileNode, TextContent},
    props: {
      article: {
        type: Object,
        required: true
      },
    },
    data() {
      return {
        placeAffiliates: {}
      }
    },
    computed: {
      content() {
        const content = [...this.article.content]

        // First is Heading or Image, Therefore place Profile at Line 1
        if (content[0] && (content[0].type === 'heading' || content[0].type === 'image')) {
          content.splice(1, 0, {type: 'profile'})
        } else {
          content.splice(0, 0, {type: 'profile'})
        }

        if (this.$vs.none('ads-hide')) {
          let pos = 15

          while (pos < (content.length - 5)) {
            content.splice(pos, 0, {type: 'advert'})
            pos += 15
          }
        }

        return content
      },
      placeIds() {
        return this.article.content
          .filter(n => n.type === 'place')
          .map(n => n.attrs.place.id)
          .filter(id => id)
      }
    },
    mounted() {
      const ids = this.placeIds
      if (ids.length) {
        this.$api.get(`/places/affiliates`, {params: {ids: ids.join(',')}})
          .then(({data: affiliates}) => {
            this.placeAffiliates = affiliates
          })
      }
    },
    methods: {
      toString(node) {
        return (node.content || []).map(n => n.text).join("")
      },
      getContexts() {
        return this.$refs['context']
      },
    }
  }
</script>
