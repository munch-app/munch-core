<template>
  <div>
    <div class="input-group">
      <h2>Status</h2>
      <div class="flex StatusList">
        <h5 class="mr-8">Current Status:</h5>
        <div class="weight-600 border-3" v-for="status in statusList" :key="status.type"
             v-if="status.type && status.type === payload.place.status.type"
             :class="{ 'bg-success white': payload.place.status.type === status.type && status.type === 'open',
                       'bg-error white': payload.place.status.type === status.type && (status.type === 'closed' || status.type === 'duplicated' || status.type === 'notFoodPlace'),
                       'bg-whisper100 b-a85': payload.place.status.type !== status.type}">
          {{status.name}}<span v-if="status.type === 'duplicated'">: {{payload.place.status.placeName}}</span>
        </div>
      </div>
      <div class="mt-8">
        <button class="primary-outline" @click="showStatus">Change Status</button>
      </div>

      <no-ssr>
        <portal to="dialog-styled" v-if="show.status" class="Dialog">
          <h3>Status</h3>
          <div class="flex-between hover-pointer" @click="onStatusChange('open')">
            <div class="text">Open</div>
            <div class="checkbox"/>
          </div>

          <div class="flex-between hover-pointer" @click="onStatusChange('closed')">
            <div class="text">Permanently Closed</div>
            <div class="checkbox"/>
          </div>

          <div class="flex-between hover-pointer" @click="onStatusDuplicate()">
            <div class="text">Duplicate</div>
            <div class="checkbox"/>
          </div>

          <div class="flex-between hover-pointer" @click="onStatusChange('notFoodPlace')">
            <div class="text">Not Food Place</div>
            <div class="checkbox"/>
          </div>

          <div class="right">
            <button class="elevated" @click="onDialogCancel">Cancel</button>
          </div>
        </portal>
      </no-ssr>

      <no-ssr>
        <portal to="dialog-styled" v-if="show.duplicate" class="Dialog">
          <h3>Duplicated with</h3>
          <suggest-search-bar :status="payload.place.status" :dialog="show"></suggest-search-bar>
          <div class="right" style="margin-top: 80px">
            <button class="elevated" @click="onDuplicateDialogBack">Back</button>
          </div>
        </portal>
      </no-ssr>
    </div>

    <div class="input-group">
      <h2>Details</h2>
      <input-text label="Name" v-model="payload.place.name" required/>
      <input-text label="Address" v-model="payload.place.location.address" required/>
      <input-text label="Price Per Pax" v-model="payload.place.price.perPax" type="number"/>
      <input-text label="Phone" v-model="payload.place.phone"/>
      <input-text label="Website" v-model="payload.place.website"/>
      <place-suggest-tags v-model="payload.place.tags" label="Tags"></place-suggest-tags>
      <input-text label="Menu URL" v-model="payload.place.menu.url"/>
      <div class="input-text">
        <label @click="verify">Description</label>
        <textarea rows="4" v-model="payload.place.description"></textarea>
      </div>
    </div>
  </div>
</template>

<script>
  import InputText from "../../core/InputText";
  import PlaceSuggestTags from "../../places/suggest/PlaceSuggestTags"
  import SuggestSearchBar from "../../suggest/SuggestSearchBar"

  export default {
    name: "PlaceSuggestDetail",
    components: {InputText, PlaceSuggestTags, SuggestSearchBar},
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
      verify() {
        console.log(this.payload.place.tags.map(tag => {
          return tag.text
        }))
      },
      showStatus() {
        this.payload.place.status.placeId = []
        this.show.status = true
      },
      onDialogCancel() {
        this.show.status = false
      },
      onDuplicateDialogBack() {
        this.show.duplicate = false
      },
      onStatusChange(status) {
        this.show.status = false
        this.payload.place.status.type = status
      },
      onStatusDuplicate() {
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
