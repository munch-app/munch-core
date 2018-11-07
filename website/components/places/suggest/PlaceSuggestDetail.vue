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
      <!-- Price Details Required -->
      <input-text label="Price Per Pax" v-model="data.place.price.perPax" type="number"/>
      <input-text label="Phone" v-model="data.place.phone"/>
      <input-text label="Website" v-model="data.place.website"/>
      <div class="input-text">
        <label>Description</label>
        <textarea></textarea>
      </div>
    </div>

    <div class="input-group" v-if="false">
      <h2>Hours</h2>
    </div>

    <div class="input-group">
      <h2>Status</h2>
      <div class="flex StatusList">
        <div class="weight-600 border-3 hover-pointer" v-for="status in statusList" :key="status.type"
             @click="data.place.status.type = status.type"
             :class="{ 'bg-success white': data.place.status.type === status.type && status.type === 'open',
                       'bg-error white': data.place.status.type === status.type && status.type === 'closed',
                       'bg-whisper100 b-a85': data.place.status.type !== status.type}">
          {{status.name}}
        </div>
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
      data: Object,
      payload: Object
    },
    data() {
      return {
        statusList: [
          {
            name: 'Open',
            type: 'open'
          },
          {
            name: 'Permanently Closed',
            type: 'closed'
          }
        ]
      }
    }
  }
</script>

<style scoped lang="less">
  .StatusList {
    > div {
      line-height: 26px;
      font-size: 15px;

      padding: 8px 20px;
      margin-right: 20px;
    }
  }
</style>
