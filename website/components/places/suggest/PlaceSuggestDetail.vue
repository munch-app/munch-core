<template>
  <div>
    <div class="input-group">
      <input-text label="Name" v-model="data.place.name" required/>
      <div class="input-text">
        <label>Tags</label>
        <input>
      </div>
      <div class="input-text">
        <label>Description</label>
        <textarea></textarea>
      </div>
    </div>

    <div class="input-group">
      <h2>Contact</h2>

      <input-text label="Address" v-model="data.place.location.address" required/>
      <input-text label="Website" v-model="data.place.website"/>
      <input-text label="Phone" v-model="data.place.phone"/>

    </div>

    <div class="input-group">
      <h2>Hours</h2>
    </div>

    <div class="input-group">
      <h2>Others</h2>

      <div class="input-text">
        <label>Comments</label>
        <textarea></textarea>
      </div>
    </div>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";

  export default {
    name: "PlaceSuggestDetail",
    components: {InputText},
    props: {
      data: Object
    }
  }
</script>

<style scoped lang="less">

</style>
