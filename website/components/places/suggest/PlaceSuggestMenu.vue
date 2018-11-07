<template>
  <div>
    <div class="input-group">
      <input-text label="Menu URL"/>
      <input-text label="Menu Image"/>
    </div>

    <h2>Existing Menu</h2>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";
  export default {
    name: "PlaceSuggestMenu",
    components: {InputText},
    props: {
      payload: {
        type: Object,
        twoWay: true
      }
    }
  }
</script>

<style scoped lang="less">

</style>
