<template>
  <p :class="className">
    <content-text-span v-for="span in item.body.content" :marks="span.marks" :text="span.text" :type="span.type"/>
  </p>

  <!--  Improved: Use the correct h1, h2, p -->
</template>

<script>
  import ContentTextSpan from "./ContentTextSpan";

  export default {
    name: "ContentTextBody",
    components: {ContentTextSpan},
    props: {
      item: {
        type: Object,
        required: true,
      }
    },
    computed: {
      className() {
        switch (this.item.type) {
          case 'title':
            return 'h1 Title'
          case 'h1':
            return 'h1 Heading1'
          case 'h2':
            return 'h2 Heading2'

          default:
            return 'large Body'
        }
      }
    },
    methods: {
      getClasses(span) {
        if (span.marks) {
          _.map()
          _.join(span.marks, '')
        }
      },
      hasLink(span) {
        for (const mark in span.marks) {
          mark.type === 'link'
        }
        return false
      }
    }
  }
</script>

<style scoped lang="less">
  .Title {
    margin-bottom: 16px;
  }

  .Heading1 {
    margin-top: 64px;
    margin-bottom: 16px;
  }

  .Heading2 {
    margin-bottom: 16px;
  }

  .Body {
    font-size: 19px;
    line-height: 1.8;
    color: rgba(0, 0, 0, 0.8);

    margin-top: 16px;
    margin-bottom: 32px;
  }
</style>
