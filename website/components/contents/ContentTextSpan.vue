<template>
  <br v-if="type === 'hard_break'">
  <span v-else-if="!marks || marks.length === 0">{{text}}</span>

  <a v-else-if="marks[0].type === 'link'" :href="attrs.href" target="_blank" class="p500 text-underline">
    <content-text-span :marks="marks.slice(1)" :text="text"/>
  </a>
  <strong v-else-if="marks[0].type === 'bold'">
    <content-text-span :marks="marks.slice(1)" :text="text"/>
  </strong>
  <em v-else-if="marks[0].type === 'italic'">
    <content-text-span :marks="marks.slice(1)" :text="text"/>
  </em>
  <u v-else-if="marks[0].type === 'underline'">
    <content-text-span :marks="marks.slice(1)" :text="text"/>
  </u>
</template>

<script>
  export default {
    name: "ContentTextSpan",
    props: {
      marks: {
        required: true,
      },
      text: {
        required: true
      },
      type: {

      }
    },
    computed: {
      attrs() {
        return this.marks
          && this.marks[0]
          && this.marks[0].attrs
          || {}
      },
      class() {
        return this.marks
          .map(m => m.type)
          .join(' ')
      }
    }
  }
</script>
