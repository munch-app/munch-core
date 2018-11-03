<template>
  <div>
    <div class="DetailGroup">
      <div class="input-text">
        <label>Name</label>
        <input>
      </div>
      <div class="input-text">
        <label>Tags</label>
        <input>
      </div>
      <div class="input-text">
        <label>Description</label>
        <textarea></textarea>
      </div>
    </div>

    <div class="DetailGroup">
      <h2>Contact</h2>

      <div class="input-text">
        <label>Address</label>
        <input>
      </div>

      <div class="input-text">
        <label>Website</label>
        <input>
      </div>

      <div class="input-text">
        <label>Phone</label>
        <input>
      </div>
    </div>

    <div class="DetailGroup">
      <h2>Hours</h2>
    </div>

    <div class="DetailGroup">
      <h2>Menu</h2>

      <div class="input-text">
        <label>Price Per Pax</label>
        <input>
      </div>

      <div class="input-text">
        <label>Menu URL</label>
        <input type="url">
      </div>

      <div class="input-text">
        <label>Menu Image</label>
        <input type="image">
      </div>
    </div>

    <div class="DetailGroup">
      <h2>Others</h2>

      <div class="input-text">
        <label>Comments</label>
        <textarea></textarea>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceSuggestDetail"
  }
</script>

<style scoped lang="less">
  .DetailGroup {
    margin-top: 32px;
    h2 {
      margin-bottom: 16px;
    }
  }
</style>
