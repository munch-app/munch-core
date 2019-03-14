<template>
  <content-line/>
</template>

<script>
  import ContentLine from "../../contents/ContentLine";
  export default {
    name: "CreatorEditorLine",
    components: {ContentLine},
    props: ['node', 'updateAttrs', 'editable'],
  }
</script>
