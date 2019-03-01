<template>
  <div>
    <div class="input-group">
      <h2>Status</h2>
      <div class="flex StatusList">
        <div class="weight-600 border-3 hover-pointer" v-for="status in statusList" :key="status.type"
             @click="payload.place.status.type = status.type"
             :class="{ 'bg-success white': payload.place.status.type === status.type && status.type === 'open',
                       'bg-error white': payload.place.status.type === status.type && status.type === 'closed',
                       'bg-whisper100 b-a85': payload.place.status.type !== status.type}">
          {{status.name}}
        </div>
      </div>
    </div>

    <div class="input-group">
      <h2>Details</h2>
      <input-text label="Name" v-model="payload.place.name" required/>
      <input-text label="Address" v-model="payload.place.location.address" required/>
      <!-- Price Details Required -->
      <input-text label="Price Per Pax" v-model="payload.place.price.perPax" type="number"/>
      <input-text label="Phone" v-model="payload.place.phone"/>
      <input-text label="Website" v-model="payload.place.website"/>
      <div class="input-text">
        <label>Description</label>
        <textarea rows="4" v-model="payload.place.description"></textarea>
      </div>
    </div>

    <div class="input-group" v-if="false">
      <h2>Hours</h2>
    </div>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";

  export default {
    name: "PlaceSuggestDetail",
    components: {InputText},
    props: {
      payload: {
        type: Object,
        twoWay: true
      }
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
