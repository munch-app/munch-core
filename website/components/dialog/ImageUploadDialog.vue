<template>
  <portal-target class="ImageUploadDialog dialog-large" name="ImageUploadDialog"/>
</template>

<script>
  export default {
    name: "ImageUploadDialog",
    data() {
      return {
        tab: 'Upload'
      }
    },
  }
</script>

<style scoped lang="less">
  .ImageUploadDialog {
    height: 480px;
    padding: 0;
  }
</style>
