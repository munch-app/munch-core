<template>
  <div :class="className">
    <span v-for="span in item.body.content">
      {{span.text}}
    </span>
  </div>
</template>

<script>
  export default {
    name: "ContentTextBody",
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
