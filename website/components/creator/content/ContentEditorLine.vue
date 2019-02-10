<template>
  <div class="ContentLine">
    <hr>
  </div>
</template>

<script>
  export default {
    name: "CreatorEditorLine",
    props: ['node', 'updateAttrs', 'editable'],
  }
</script>

<style scoped lang="less">
  .ContentLine {
    padding-top: 32px;
    padding-bottom: 32px;
  }

  hr {
    max-width: 320px;
    border-top: 2px dashed rgba(0, 0, 0, .1);
  }
</style>
