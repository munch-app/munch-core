<template>
  <div>
    <div class="input-group">
      <div class="flex StatusList">
        <h5 class="mr-8">Current Status:</h5>
        <div :class="{ 'bg-success white': payload.place.status.type === status.type && status.type === 'open',
                       'bg-error white': payload.place.status.type === status.type && (status.type === 'closed' || status.type === 'duplicated' || status.type === 'notFoodPlace'),
                       'bg-whisper100 b-a85': payload.place.status.type !== status.type}" :key="status.type"
             class="weight-600 border-3"
             v-for="status in statusList"
             v-if="status.type && status.type === payload.place.status.type">
          {{status.name}}
          <span v-if="status.type === 'duplicated'">: {{payload.place.status.placeNames.join(', ')}}</span>
        </div>
      </div>
      <div class="mt-8">
        <button @click="showStatus" class="tiny primary-outline">Change Status</button>
      </div>

      <no-ssr>
        <portal class="Dialog" to="dialog-styled" v-if="show.status">
          <h2>Change Status</h2>
          <div @click="onStatusChange('open')" class="flex-between hover-pointer">
            <div class="text">Open</div>
            <div class="checkbox"/>
          </div>

          <div @click="onStatusChange('closed')" class="flex-between hover-pointer">
            <div class="text">Permanently Closed</div>
            <div class="checkbox"/>
          </div>

          <div @click="onStatusDuplicate()" class="flex-between hover-pointer">
            <div class="text">Duplicate</div>
            <div class="checkbox"/>
          </div>

          <div @click="onStatusChange('notFoodPlace')" class="flex-between hover-pointer">
            <div class="text">Not Food Place</div>
            <div class="checkbox"/>
          </div>

          <div class="right">
            <button @click="onDialogCancel" class="secondary-outline">Cancel</button>
          </div>
        </portal>
      </no-ssr>

      <no-ssr>
        <portal class="Dialog" to="dialog-styled" v-if="show.duplicate">
          <h3>Duplicated with</h3>
          <suggest-search-bar :dialog="show" :status="payload.place.status"
                              style="margin-bottom: 20px;"></suggest-search-bar>
          <div style="display: flex;" v-for="(name, index) in payload.place.status.placeNames">
            <div class="mt-4 wh-100">
              {{index+1}}. {{name}}
            </div>
          </div>
          <div class="right" style="margin-top: 60px">
            <button @click="onDuplicateDialogBack" class="primary-outline">Back</button>
            <button @click="onDuplicateDialogDone" class="primary">Done</button>
          </div>
        </portal>
      </no-ssr>
    </div>

    <section class="mt-48">
      <div class="input-group">
        <input-text label="Name" required v-model="payload.place.name"/>
        <input-text label="Address" required v-model="payload.place.location.address"/>
        <input-text label="Price Per Pax" type="number" v-model="payload.place.price.perPax"/>
        <input-text label="Phone" v-model="payload.place.phone"/>
        <input-text label="Website" v-model="payload.place.website"/>
        <place-suggest-tags label="Tags" v-model="payload.place.tags"></place-suggest-tags>
        <input-text label="Menu URL" v-model="payload.place.menu.url"/>
        <div class="input-text">
          <label>Description</label>
          <textarea rows="4" v-model="payload.place.description"></textarea>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";
  import PlaceSuggestTags from "../../places/suggest/PlaceSuggestTags"
  import SuggestSearchBar from "../../suggest/SuggestSearchBar"
  import PlaceSuggestOpeningHours from "../../../components/places/suggest/PlaceSuggestOpeningHours";

  export default {
    name: "PlaceSuggestDetail",
    components: {InputText, PlaceSuggestTags, SuggestSearchBar, PlaceSuggestOpeningHours},
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
          },
          {
            name: 'Duplicate',
            type: 'duplicated'
          },
          {
            name: 'Not Food Place',
            type: 'notFoodPlace'
          }
        ],
        statusDialog: null,
        show: {
          status: false,
          duplicate: false
        }
      }
    },
    methods: {
      showStatus() {
        this.show.status = true
      },
      onDialogCancel() {
        this.show.status = false
      },
      onDuplicateDialogBack() {
        this.payload.place.status.placeIds = null
        this.payload.place.status.placeNames = null
        this.show.duplicate = false
      },
      onDuplicateDialogDone() {
        this.payload.place.status.type = 'duplicated'
        this.show.duplicate = false
        this.show.status = false
      },
      onStatusChange(status) {
        this.show.status = false
        this.payload.place.status.type = status
      },
      onStatusDuplicate() {
        if (!this.payload.place.status.placeIds) {
          this.payload.place.status.placeIds = []
          this.payload.place.status.placeNames = []
        }
        this.show.duplicate = true
      }
    }
  }
</script>

<style scoped lang="less">
  .StatusList {
    > div {
      line-height: 8px;
      font-size: 13px;

      padding: 8px 8px;
    }
  }

  .Dialog {
    > div {
      padding-top: 8px;
      padding-bottom: 8px;
      margin-bottom: 0;
      margin-left: 0;
    }
  }
</style>
