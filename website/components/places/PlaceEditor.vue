<template>
  <div class="flex-column">
    <div v-if="has('NAME')" class="Group">
      <h6>Name:</h6>
      <input v-model.trim="editing.name">
    </div>

    <div v-if="has('PHONE')" class="Group">
      <h6>Phone:</h6>
      <input v-model.trim="editing.phone">
    </div>

    <div v-if="has('WEBSITE')" class="Group">
      <h6>Website:</h6>
      <input v-model.trim="editing.website">
    </div>
    <div v-if="has('DESCRIPTION')" class="Group">
      <h6>Description:</h6>
      <text-auto class="TextAuto" v-model.trim="editing.description"/>
    </div>

    <div v-if="has('PRICE')" class="Group">
      <h6>Price:</h6>
      <input v-model.number="editing.price.perPax" type="number">
    </div>
    <div v-if="has('LOCATION_ADDRESS')" class="Group">
      <h6>Address: </h6>
      <input v-model.trim="editing.location.address">
    </div>
    <div class="flex-1-2 m--8">
      <div v-if="has('LOCATION_UNIT_NUMBER')" class="Group p-8">
        <h6>Unit Number:</h6>
        <input v-model.trim="editing.location.unitNumber">
      </div>
      <div v-if="has('LOCATION_POSTCODE')" class="Group p-8">
        <h6>Postcode:</h6>
        <input v-model.trim="editing.location.postcode">
      </div>
    </div>
    <div v-if="has('LOCATION_LAT_LNG')" class="Group">
      <h6>Lat Lng:</h6>
      <editor-lat-lng v-model="editing.location.latLng"/>
    </div>
    <div v-if="has('STATUS')" class="Group">
      <h6>Status:</h6>
      <editor-status v-model="editing.status"/>
    </div>
    <div v-if="has('SYNONYMS')" class="Group">
      <h6>Synonyms:</h6>
      <editor-synonyms v-model="editing.synonyms"/>
    </div>
    <div v-if="has('TAGS')" class="Group">
      <h6>Tags:</h6>
      <editor-tags class="border border-3 bg-steam p-16" v-model="editing.tags"/>
    </div>
    <div v-if="has('HOURS')" class="Group">
      <h6>Opening Hours:</h6>
      <editor-hours v-model="editing.hours"/>
    </div>
  </div>
</template>

<script>
  import TextAuto from "../utils/TextAuto";
  import AppleMap from "../utils/map/AppleMap";
  import EditorStatus from "../editor/EditorStatus";
  import EditorSynonyms from "../editor/EditorSynonyms";
  import EditorHours from "../editor/EditorHours";
  import EditorTags from "../editor/EditorTags";
  import EditorLatLng from "../editor/EditorLatLng";

  export default {
    name: "PlaceEditor",
    components: {
      EditorLatLng,
      EditorTags, EditorHours, EditorSynonyms, EditorStatus, AppleMap, TextAuto
    },
    props: {
      place: {
        type: Object,
        default() {
          return {}
        }
      },
      editableFields: {
        type: Array,
        default: () => [
          'NAME', 'PHONE', 'WEBSITE', 'DESCRIPTION', 'PRICE',
          'LOCATION_UNIT_NUMBER', 'LOCATION_POSTCODE', 'LOCATION_ADDRESS', 'LOCATION_LAT_LNG',
          'STATUS', 'SYNONYMS', 'TAGS', 'HOURS'
        ]
      }
    },
    data() {
      const editing = JSON.parse(JSON.stringify(this.place))

      if (!editing.price) {
        editing.price = {}
      }

      if (!editing.location) {
        editing.location = {}
      }

      return {editing}
    },
    methods: {
      has(field) {
        return _.includes(this.editableFields, field)
      },

      /**
       * All these method is just done temporary, by-right user should not be able to key in invalid info
       * @param callback
       */
      confirm(callback) {
        const place = JSON.parse(JSON.stringify(this.editing))

        if (!place.name) delete place['name']
        if (!place.phone) delete place['phone']
        if (!place.website) delete place['website']
        if (!place.description) delete place['description']

        // Trimming of objects
        if (place.price) {
          if (place.price.perPax < 0) {
            delete place['price']
          }
          if (!place.price.perPax) {
            delete place['price']
          }
        }

        if (place.location) {
          if (!place.location.address) delete place.location['address']
          if (!place.location.postcode) delete place.location['postcode']
          if (!place.location.unitNumber) delete place.location['unitNumber']
        }

        // Substring of Length via Validation
        if (place.name && place.name.length > 100) {
          place.name = place.name.substring(0, 100)
        }

        if (place.phone && place.phone.length > 100) {
          place.phone = place.phone.substring(0, 100)
        }

        // Website is not trimmed to prevent error in url redirecting

        if (place.description && place.description.length > 250) {
          place.description = place.description.substring(0, 250)
        }

        // Synonyms
        if (place.synonyms) {
          if (place.synonyms.length > 4) {
            place.synonyms.splice(4)
          }

          for (let index = 0; index < 4; index++) {
            if (place.synonyms[index] && place.synonyms[index].length > 100) {
              place.synonyms[index] = place.synonyms[index].substring(0, 100)
            }
          }
        }

        // Tags
        if (place.tags && place.tags.length > 12) {
          place.tags.splice(12)
        }

        // Hours
        if (place.tags && place.tags.length > 24) {
          place.tags.splice(24)
        }

        callback({place: place, editableFields: this.editableFields})
      },
    }
  }
</script>

<style scoped lang="less">
  .Group {
    margin-bottom: 16px;

    h6 {
      margin-bottom: 4px;
    }

    input, .TextAuto {
      outline: none;
      border: none;

      background: #FAFAFA;
      color: black;

      width: 100%;
      font-size: 17px;
      padding: 12px;
      border-radius: 2px;

      &:focus {
        background: #F0F0F0;
      }
    }
  }
</style>
