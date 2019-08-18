<template>
  <br v-if="type === 'hard_break'">
  <b v-else-if="mark.type === 'bold'">
    <text-content :text="text" :marks="nextMarks"/>
  </b>
  <u v-else-if="mark.type === 'underline'">
    <text-content :text="text" :marks="nextMarks"/>
  </u>
  <i v-else-if="mark.type === 'italic'">
    <text-content :text="text" :marks="nextMarks"/>
  </i>
  <a v-else-if="mark.type === 'link'" :href="attrs.href">
    <text-content :text="text" :marks="nextMarks"/>
  </a>
  <span v-else>{{text}}</span>
</template>

<script>
  export default {
    name: "TextContent",
    props: {
      type: String,
      text: String,
      marks: Array,
    },
    computed: {
      mark() {
        return this.marks && this.marks[0] || {}
      },
      attrs() {
        return this.mark?.attrs || {}
      },
      nextMarks() {
        return this.marks.slice(1)
      }
    }
  }
</script>

<style scoped lang="less">

</style>
