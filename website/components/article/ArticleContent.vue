<template>
  <div class="article-content">
    <template v-for="(node, index) in content">
      <profile-node v-if="node.type === 'profile'" :article="article" :key="index"/>
      <h1 v-else-if="node.type === 'heading' && node.attrs['level'] === 1" :key="index">{{toString(node)}}</h1>
      <h2 v-else-if="node.type === 'heading' && node.attrs['level'] === 2" :key="index">{{toString(node)}}</h2>
      <p v-else-if="node.type === 'paragraph'" :key="index">
        <text-content v-for="(content, index) in node.content" v-bind="content" :key="index"/>
      </p>
      <hr v-else-if="node.type === 'line'" :key="index">
      <article-image-node v-else-if="node.type ==='image'" :key="index" :node="node"/>
      <article-avatar-node v-else-if="node.type ==='avatar'" :key="index" :node="node"/>
      <div v-else :key="index">
        <pre>Unmapped Type</pre>
      </div>
    </template>

    <!--    <code>-->
    <!--      <pre>{{article.content}}</pre>-->
    <!--    </code>-->
  </div>
</template>

<script>
  import TextContent from "./node/TextContent";
  import ProfileNode from "./node/ProfileNode";
  import ArticleImageNode from "./node/ArticleImage.vue";
  import ArticleAvatarNode from "./node/ArticleAvatar.vue";

  export default {
    name: "ArticleContent",
    components: {ArticleAvatarNode, ArticleImageNode, ProfileNode, TextContent},
    props: {
      article: {
        type: Object,
        required: true
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

        return content
      }
    },
    methods: {
      toString(node) {
        return (node.content || []).map(n => n.text).join("")
      }
    }
  }
</script>

<style scoped lang="less">

</style>
