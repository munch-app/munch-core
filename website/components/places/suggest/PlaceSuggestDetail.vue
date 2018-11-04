<template>
  <div>
    <div class="input-group">
      <input-text label="Name" v-model="data.place.name" required/>
      <input-text label="Address" v-model="data.place.location.address" required/>

      <!--<div class="input-text">-->
        <!--<label>Tags</label>-->
        <!--<input>-->
      <!--</div>-->
      <!---->
    </div>

    <div class="input-group">
      <h2>Details</h2>
      <input-text label="Price Per Pax" v-model="data.place.price.perPax"/>
      <input-text label="Phone" v-model="data.place.phone"/>
      <input-text label="Website" v-model="data.place.website"/>
      <div class="input-text">
        <label>Description</label>
        <textarea></textarea>
      </div>
    </div>

    <div class="input-group" v-if="false">
      <!-- TODO: Hours Implementation-->
      <h2>Hours</h2>
    </div>

    <div class="input-group">
      <h2>Status</h2>
      <p>Permanently Closed</p>
      <p>Moved Permanently</p>
      <p>Renovation</p>
    </div>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";

  export default {
    name: "PlaceSuggestDetail",
    components: {InputText},
    props: {
      data: Object,
      changes: Object
    }
  }
</script>

<style scoped lang="less">

</style>
