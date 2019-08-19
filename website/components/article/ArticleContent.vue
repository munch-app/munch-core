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

  export default {
    name: "ArticleContent",
    components: {ProfileNode, TextContent},
    props: {
      article: {
        type: Object,
        required: true
      }
    },
    computed: {
      content() {
        const content = [...this.article.content]
        if(content[0] && content[0].type === 'heading') {
          content.splice(1, 0, {type: 'profile'})
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
