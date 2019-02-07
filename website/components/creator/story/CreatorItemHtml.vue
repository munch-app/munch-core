<template>
  <div>
    <text-auto class="TextAuto bg-whisper100" placeholder="HTML" v-model="item.text"/>
  </div>
</template>

<script>
  import TextAuto from "../../core/TextAuto";
  export default {
    name: "CreatorItemHtml",
    components: {TextAuto},
    props: {
      item: {
        type: Object,
        required: true
      }
    },
    methods: {
      onChange() {
        this.$emit('change')
      }
    }
  }
</script>

<style scoped lang="less">
  .TextAuto {
    margin-bottom: 0;
    font-family: Courier, serif;
    font-size: 15px;
  }
</style>
