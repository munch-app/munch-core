<template>
  <content-line/>
</template>

<script>
  import ContentLine from "../../../pages/contents/ContentLine";
  export default {
    name: "CreatorEditorLine",
    components: {ContentLine},
    props: ['node', 'updateAttrs', 'editable'],
  }
</script>
